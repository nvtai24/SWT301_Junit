package dao;

import dal.DBConnect;
import dal.ExceptionHandlers;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Book;
import model.Order;
import model.OrderDetail;
import model.User;
import java.sql.Timestamp;

public class CartDAO extends DBConnect {

    public Order getOrderNew(int userId) {
        Order order = null;
        String query = "SELECT TOP 1 * "
                + "FROM Orders "
                + "WHERE user_id = ? AND [status] = 'pending' "
                + "ORDER BY order_date DESC";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int orderId = rs.getInt("order_id");

                UserDAO userDao = new UserDAO();
                User user = userDao.getUserByUserId(userId);

                java.sql.Date orderDate = rs.getDate("order_date");
                double totalAmount = rs.getDouble("total_amount");
                String status = rs.getString("status");

                List<OrderDetail> listOrderDetail = getListOrderDetailByOrderId(orderId);

                order = new Order(orderId, user, orderDate, totalAmount, status, listOrderDetail);
            }
        } catch (SQLException e) {
            ExceptionHandlers.handleException(e);
        }
        return order;
    }

    public List<OrderDetail> getListOrderDetailByOrderId(int orderId) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        String query = "SELECT * FROM OrderDetails WHERE order_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int orderDetailId = rs.getInt("order_detail_id");
                int bookId = rs.getInt("book_id");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");

                BookDAO bookDAO = new BookDAO();
                Book book = bookDAO.getBookById(bookId);

                OrderDetail orderDetail = new OrderDetail(orderDetailId, null, book, quantity, price); // Notice the 'null' placeholder for Order

                orderDetails.add(orderDetail);
            }
        } catch (SQLException e) {
            ExceptionHandlers.handleException(e);
        }
        return orderDetails;
    }

    public Order getOrderById(int orderId) {
        Order order = null;
        String query = "SELECT * FROM Orders WHERE order_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("user_id");
                UserDAO userDao = new UserDAO();
                User user = userDao.getUserByUserId(userId);

                java.sql.Date orderDate = rs.getDate("order_date");
                double totalAmount = rs.getDouble("total_amount");
                String status = rs.getString("status");

                List<OrderDetail> orderDetails = getListOrderDetailByOrderId(orderId);

                order = new Order(orderId, user, orderDate, totalAmount, status, orderDetails);
            }
        } catch (SQLException e) {
            ExceptionHandlers.handleException(e);
        }
        return order;
    }

    public void createNewOrder(int userId) {
        String insertOrderQuery = "INSERT INTO Orders (user_id, order_date, total_amount, status) "
                + "VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(insertOrderQuery)) {
            // Set parameters for the query
            ps.setInt(1, userId);
            ps.setTimestamp(2, new Timestamp(System.currentTimeMillis())); // current timestamp for order_date
            ps.setDouble(3, 0.0);
            ps.setString(4, "pending");

            // Execute the update query
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("New order created successfully.");
            } else {
                System.out.println("Failed to create a new order.");
            }
        } catch (SQLException e) {
            ExceptionHandlers.handleException(e);
        }
    }

    public int countDistinctBooksInCart(int userId) {
        int count = 0;
        String query = "SELECT COUNT(DISTINCT od.book_id) AS totalBooks FROM OrderDetails od "
                + "JOIN Orders o ON od.order_id = o.order_id "
                + "WHERE o.user_id = ? AND o.status = 'pending'";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt("totalBooks");
            }
        } catch (SQLException e) {
            ExceptionHandlers.handleException(e);
        }
        return count;
    }

    public boolean addOrUpdateOrderDetail(int orderId, int bookId, int quantity, double price) {
        String checkOrderDetailQuery = "SELECT quantity FROM OrderDetails WHERE order_id = ? AND book_id = ?";
        String updateOrderDetailQuery = "UPDATE OrderDetails SET quantity = ?, price = ? WHERE order_id = ? AND book_id = ?";
        String insertOrderDetailQuery = "INSERT INTO OrderDetails (order_id, book_id, quantity, price) VALUES (?, ?, ?, ?)";

        try {
            // Check if the order detail exists
            try (PreparedStatement checkPs = conn.prepareStatement(checkOrderDetailQuery)) {
                checkPs.setInt(1, orderId);
                checkPs.setInt(2, bookId);
                ResultSet rs = checkPs.executeQuery();

                if (rs.next()) {
                    // If it exists, update the quantity and price
                    int existingQuantity = rs.getInt("quantity");
                    try (PreparedStatement updatePs = conn.prepareStatement(updateOrderDetailQuery)) {
                        updatePs.setInt(1, existingQuantity + quantity); // Add to the existing quantity
                        updatePs.setDouble(2, price * (existingQuantity + quantity)); // Update the price for the new quantity
                        updatePs.setInt(3, orderId);
                        updatePs.setInt(4, bookId);

                        // tính quá giới hạn trong kho 
                        BookDAO bookDao = new BookDAO();
                        Book book = bookDao.getBookById(bookId);

                        if (book.getQuantityInStock() < (existingQuantity + quantity)) {
                            return false;
                        }

                        int rowsUpdated = updatePs.executeUpdate();
                        if (rowsUpdated > 0) {
                            System.out.println("Order detail updated successfully.");
                            return true;
                        } else {
                            System.out.println("Failed to update order detail.");
                        }
                    }
                } else {
                    // If it doesn't exist, insert a new order detail
                    try (PreparedStatement insertPs = conn.prepareStatement(insertOrderDetailQuery)) {
                        insertPs.setInt(1, orderId);
                        insertPs.setInt(2, bookId);
                        insertPs.setInt(3, quantity);
                        insertPs.setDouble(4, price * quantity); // Set price based on quantity

                        int rowsInserted = insertPs.executeUpdate();
                        if (rowsInserted > 0) {
                            System.out.println("Order detail added successfully.");
                            return true;
                        } else {
                            System.out.println("Failed to add order detail.");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            ExceptionHandlers.handleException(e);
        }
        return false;
    }

    public boolean removeCart(int orderId) {
        String deleteOrderDetailsQuery = "DELETE FROM OrderDetails WHERE order_id = ?";
        String deleteOrderQuery = "DELETE FROM Orders WHERE order_id = ?";

        try {
            // Begin transaction
            conn.setAutoCommit(false);

            // Delete OrderDetails
            try (PreparedStatement deleteOrderDetailsStmt = conn.prepareStatement(deleteOrderDetailsQuery)) {
                deleteOrderDetailsStmt.setInt(1, orderId);
                deleteOrderDetailsStmt.executeUpdate();
            }

            // Delete Order
            try (PreparedStatement deleteOrderStmt = conn.prepareStatement(deleteOrderQuery)) {
                deleteOrderStmt.setInt(1, orderId);
                deleteOrderStmt.executeUpdate();
            }

            // Commit transaction
            conn.commit();
            return true;
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException rollbackException) {
                // Log rollback exception, if any
                ExceptionHandlers.handleException(rollbackException);
            }
            // Log or handle the original exception
            ExceptionHandlers.handleException(e);
        }
        return false;
    }

    public boolean updateTotalAmount(double totalAmount, int orderId) {
        String query = "UPDATE Orders SET total_amount = ? WHERE order_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            // Set the parameters for the query
            ps.setDouble(1, totalAmount);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        } catch (SQLException e) {
            ExceptionHandlers.handleException(e);
        }
        return false;
    }

    public boolean updateOrder(int orderId, double totalAmount, String status) {
        String sql = "UPDATE orders SET total_amount = ?, status = ? WHERE order_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, totalAmount);
            stmt.setString(2, status);
            stmt.setInt(3, orderId);

            int rowsUpdated = stmt.executeUpdate();
            CartDAO cartDao = new CartDAO();

            if (status.equals("checkout")) {
                cartDao.updateBookQuantities(orderId);

            }

            return rowsUpdated > 0;
        } catch (SQLException e) {
            ExceptionHandlers.handleException(e);
            return false;
        }
    }

   public boolean updateBookQuantities(int orderId) {
    String sql = "SELECT book_id, quantity FROM OrderDetails WHERE order_id = ?";
    
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, orderId);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            int bookId = rs.getInt("book_id");
            int quantityOrdered = rs.getInt("quantity");
            
            if (!updateBookStock(bookId, quantityOrdered)) {
                return false; // Nếu có lỗi khi cập nhật, dừng và trả về false
            }
        }
    } catch (SQLException e) {
        ExceptionHandlers.handleException(e);
        return false;
    }
    
    return true; // Cập nhật thành công tất cả sách
}

// ✅ Phương thức riêng để cập nhật số lượng sách
private boolean updateBookStock(int bookId, int quantityOrdered) {
    String sqlSelectBook = "SELECT quantity_in_stock, quantity_sold FROM Books WHERE book_id = ?";
    String sqlUpdateBook = "UPDATE Books SET quantity_in_stock = ?, quantity_sold = ? WHERE book_id = ?";
    
    try (PreparedStatement stmtSelectBook = conn.prepareStatement(sqlSelectBook)) {
        stmtSelectBook.setInt(1, bookId);
        ResultSet rs = stmtSelectBook.executeQuery();
        
        if (rs.next()) {
            int quantityInStock = rs.getInt("quantity_in_stock");
            int quantitySold = rs.getInt("quantity_sold");
            
            int newQuantityInStock = quantityInStock - quantityOrdered;
            int newQuantitySold = quantitySold + quantityOrdered;

            try (PreparedStatement stmtUpdateBook = conn.prepareStatement(sqlUpdateBook)) {
                stmtUpdateBook.setInt(1, newQuantityInStock);
                stmtUpdateBook.setInt(2, newQuantitySold);
                stmtUpdateBook.setInt(3, bookId);

                int rowsUpdated = stmtUpdateBook.executeUpdate();
                return rowsUpdated > 0; // Trả về true nếu cập nhật thành công
            }
        }
    } catch (SQLException e) {
        ExceptionHandlers.handleException(e);
    }
    
    return false; // Trả về false nếu có lỗi
}

    public List<Order> getAllOrderByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM Orders WHERE user_id = ? AND status != 'pending'";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                java.sql.Date orderDate = rs.getDate("order_date");
                double totalAmount = rs.getDouble("total_amount");
                String status = rs.getString("status");

                List<OrderDetail> orderDetails = getListOrderDetailByOrderId(orderId);

                UserDAO userDao = new UserDAO();
                User user = userDao.getUserByUserId(userId);

                Order order = new Order(orderId, user, orderDate, totalAmount, status, orderDetails);
                orders.add(order);
            }
        } catch (SQLException e) {
            ExceptionHandlers.handleException(e);
        }
        return orders;
    }

    public List<Order> getAllOrders(int offset, int limit) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM Orders WHERE status != 'pending' ORDER BY order_id DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, offset);
            ps.setInt(2, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                java.sql.Date orderDate = rs.getDate("order_date");
                double totalAmount = rs.getDouble("total_amount");
                String status = rs.getString("status");

                List<OrderDetail> orderDetails = getListOrderDetailByOrderId(orderId);

                UserDAO userDao = new UserDAO();
                User user = userDao.getUserByUserId(rs.getInt("user_id"));

                Order order = new Order(orderId, user, orderDate, totalAmount, status, orderDetails);
                orders.add(order);
            }
        } catch (SQLException e) {
            ExceptionHandlers.handleException(e);
        }
        return orders;
    }

    public int getNumberOfOrders() {
        int count = 0;
        String query = "SELECT COUNT(*) AS count FROM Orders WHERE status != 'pending'";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt("count");
            }
        } catch (SQLException e) {
            ExceptionHandlers.handleException(e);
        }
        return count;
    }

    public boolean updateStatus(int orderId, String status) {
        String sql = "UPDATE orders SET status = ? WHERE order_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, orderId);

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            ExceptionHandlers.handleException(e);
            return false;
        }
    }

    public boolean deleteOrder(int orderId) {
        String deleteOrderDetailsQuery = "DELETE FROM OrderDetails WHERE order_id = ?";
        String deleteOrderQuery = "DELETE FROM Orders WHERE order_id = ?";

        try (
                PreparedStatement deleteOrderDetailsStmt = conn.prepareStatement(deleteOrderDetailsQuery); PreparedStatement deleteOrderStmt = conn.prepareStatement(deleteOrderQuery)) {
            // Tắt chế độ auto-commit để kiểm soát giao dịch
            conn.setAutoCommit(false);

            // Xóa chi tiết đơn hàng trước
            deleteOrderDetailsStmt.setInt(1, orderId);
            deleteOrderDetailsStmt.executeUpdate();

            // Xóa đơn hàng
            deleteOrderStmt.setInt(1, orderId);
            int affectedRows = deleteOrderStmt.executeUpdate();

            if (affectedRows == 1) {
                conn.commit(); // Xác nhận giao dịch
                return true;
            } else {
                conn.rollback(); // Hoàn tác nếu có vấn đề
                return false;
            }
        } catch (SQLException e) {
            try {
                conn.rollback(); // Hoàn tác nếu có lỗi
            } catch (SQLException rollbackEx) {
                ExceptionHandlers.handleException(rollbackEx);
            }
            ExceptionHandlers.handleException(e);
            return false;
        } finally {
            try {
                conn.setAutoCommit(true); // Đặt lại auto-commit về mặc định
            } catch (SQLException ex) {
                ExceptionHandlers.handleException(ex);
            }
        }
    }

}
