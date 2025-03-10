package controller;

import dao.BookDAO;
import dao.CartDAO;
import dao.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Delete extends HttpServlet {

    private UserDAO userDao;

    public Delete(UserDAO userDao) {
        this.userDao = userDao;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String deleteAction = request.getParameter("delete");
        String userIdParam = request.getParameter("userId");

        // Kiểm tra hành động delete
        if ("deleteUser".equals(deleteAction)) {
            if (userIdParam != null && !userIdParam.isEmpty()) {
                int userId = Integer.parseInt(userIdParam);
                try {
                    this.userDao.deleteUserData(userId);
                } catch (SQLException ex) {
                    Logger.getLogger(Delete.class.getName()).log(Level.SEVERE, null, ex);
                }
                response.sendRedirect("manager-user");
            } else {
                response.sendRedirect("manager-user"); // Không có userId, chuyển hướng
            }
        } else {
            // Nếu deleteAction không phải là deleteUser, redirect về manager-user
            response.sendRedirect("manager-user");
        }

        if (deleteAction.equals("deleteOrder")) {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            CartDAO cartDao = new CartDAO();
            cartDao.deleteOrder(orderId);
            response.sendRedirect("manager-order");
        }
        if (deleteAction.equals("deleteAuthor")) {
            int authorId = Integer.parseInt(request.getParameter("authorId"));
            UserDAO userDao = new UserDAO();
            userDao.deleteAuthor(authorId);
            response.sendRedirect("manager-author");
        }
        if (deleteAction.equals("deleteBook")) {
            int bookId = Integer.parseInt(request.getParameter("bookId"));
            BookDAO bookDao = new BookDAO();
            bookDao.deleteBookById(bookId);
            response.sendRedirect("manager-book");
        }

    }
}
