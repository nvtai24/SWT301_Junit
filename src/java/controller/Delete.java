package controller;

import dao.BookDAO;
import dao.CartDAO;
import dao.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class Delete extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (request.getParameter("delete").equals("deleteUser")) {
            int userId = Integer.parseInt(request.getParameter("userId"));
            UserDAO userDao = new UserDAO();
            userDao.deleteUserData(userId);
            response.sendRedirect("manager-user");

        }
        if (request.getParameter("delete").equals("deleteOrder")) {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            CartDAO cartDao = new CartDAO();
            cartDao.deleteOrder(orderId);
            response.sendRedirect("manager-order");

        }
        if (request.getParameter("delete").equals("deleteAuthor")) {
            int authorId = Integer.parseInt(request.getParameter("authorId"));
            UserDAO userDao = new UserDAO();
             userDao.deleteAuthor(authorId);
            response.sendRedirect("manager-author");

        }
        if (request.getParameter("delete").equals("deleteBook")) {
            int bookId = Integer.parseInt(request.getParameter("bookId"));
            BookDAO bookDao = new BookDAO();
             bookDao.deleteBookById(bookId);
            response.sendRedirect("manager-book");

        }

    }
}
