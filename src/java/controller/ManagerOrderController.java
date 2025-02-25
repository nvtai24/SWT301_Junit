package controller;

import dao.CartDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Order;

public class ManagerOrderController extends HttpServlet {
    @Override
   protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    CartDAO orderDao = new CartDAO();

    // Pagination variables
    int page = 1;
    int recordsPerPage = 10; // Number of records per page
    if (request.getParameter("page") != null) {
        page = Integer.parseInt(request.getParameter("page"));
    }

    List<Order> listAllOrder = orderDao.getAllOrders((page - 1) * recordsPerPage, recordsPerPage);
    request.setAttribute("listOrders", listAllOrder);

    // Get total number of orders for pagination
    int totalOrders = orderDao.getNumberOfOrders();
    int totalPages = (int) Math.ceil((double) totalOrders / recordsPerPage);
    request.setAttribute("totalPages", totalPages);
    request.setAttribute("currentPage", page);

    request.getRequestDispatcher("managerOrder.jsp").forward(request, response);
}
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        String status = request.getParameter("status");
        CartDAO cartDao = new CartDAO();
        cartDao.updateStatus(orderId, status);
        
        
        doGet(request, response);

    }
}
