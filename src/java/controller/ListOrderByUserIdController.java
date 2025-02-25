package controller;

import dao.CartDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Order;
import model.User;

public class ListOrderByUserIdController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("username");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        CartDAO cartDao = new CartDAO();
        List<Order> listOrder = cartDao.getAllOrderByUserId(user.getUserId());
        request.setAttribute("listOrder", listOrder);

        request.getRequestDispatcher("listOrder.jsp").forward(request, response);

    }
}
