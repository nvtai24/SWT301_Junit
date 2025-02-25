package controller;

import dao.CartDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Order;
import model.User;


public class ClearCartController extends HttpServlet {

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

        // xóa cart
        String clear = request.getParameter("clear");
        if (clear != null) {
            cartDao.removeCart(order.getOrderId());
        }
        response.sendRedirect("cart");

    }
}
