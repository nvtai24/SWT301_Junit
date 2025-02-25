package controller;

import dao.CartDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.text.DecimalFormat;
import model.Order;
import model.User;

public class CheckoutController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("username");

        if (user == null) {
            response.sendRedirect("login"); // Redirect if user not logged in
            return;
        }

        CartDAO cartDao = new CartDAO();
        Order order = cartDao.getOrderNew(user.getUserId());

        if (order != null) {

            double totalAmount = Double.parseDouble(request.getParameter("totalAmount"));

            // Format totalAmount to two decimal places
            DecimalFormat df = new DecimalFormat("#.##");
            String formattedTotalAmount = df.format(totalAmount);

            request.setAttribute("order", order);
            request.setAttribute("totalAmount", formattedTotalAmount);
            request.getRequestDispatcher("checkout.jsp").forward(request, response);
        } else {
            // Handle scenario where there are no items in the cart
            request.setAttribute("mess", "No items in the cart");
            request.getRequestDispatcher("checkout.jsp").forward(request, response);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("username");

        if (user == null) {
            response.sendRedirect("login"); // Redirect if user not logged in
            return;
        }

        double totalAmount = Double.parseDouble(request.getParameter("totalAmount"));
        int orderId = Integer.parseInt(request.getParameter("orderId"));

        // chuyển status sang trạng thái khác
        CartDAO cartDao = new CartDAO();
        String status = "checkout";
        cartDao.updateOrder(orderId, totalAmount, status);

        request.setAttribute("mess", "Checkout successfully");

        request.getRequestDispatcher("checkout.jsp").forward(request, response);

    }
}
