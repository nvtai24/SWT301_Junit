package controller;

import dao.BookDAO;
import dao.CartDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.text.DecimalFormat;
import model.Book;
import model.Order;
import model.OrderDetail;
import model.User;

public class CartController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        CartDAO cartDao = new CartDAO();
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("username");
        
        if (user == null) {
            response.sendRedirect("login");
            return;
        }
        
        Order order = cartDao.getOrderNew(user.getUserId());
        
        if (order != null) {
            request.setAttribute("order", order);
        } else {
            cartDao.createNewOrder(user.getUserId());
            request.setAttribute("mess", "No book in the cart");
        }

        // Calculate total amount 
        if (order != null) {
            double totalAmount = 0;
            for (OrderDetail orderdetail : order.getOrderDetail()) {
                totalAmount += orderdetail.getPrice() * orderdetail.getQuantity();
            }

            // Format totalAmount to two decimal places
            DecimalFormat df = new DecimalFormat("#.##");
            String formattedTotalAmount = df.format(totalAmount);
            request.setAttribute("totalAmount", formattedTotalAmount);
        }
        
        request.getRequestDispatcher("cart.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("username");
        
        if (user == null) {
            response.sendRedirect("login");
            return;
        }
        
        int bookId = Integer.parseInt(request.getParameter("bookId"));
        int quantity = 1;
        if (request.getParameter("quantity") != null) {
            quantity = Integer.parseInt(request.getParameter("quantity"));
        }
        CartDAO cartDao = new CartDAO();
        
        BookDAO bookDao = new BookDAO();
        
        Book book = bookDao.getBookById(bookId);
        
        Order order = cartDao.getOrderNew(user.getUserId());
        double price = book.getPrice();
        
        boolean addCart = cartDao.addOrUpdateOrderDetail(order.getOrderId(), bookId, quantity, price);


        if (!addCart) {
            String mess  = "vượt quá số lượng trong kho cuốn sách " + book.getTitle();
            request.setAttribute("mess", mess);
            request.getRequestDispatcher("cart.jsp").forward(request, response);
            return;
        }
        
        response.sendRedirect("cart");
    }
}
