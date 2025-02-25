package controller;

import dao.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.User;

public class ManagerUserController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Số dòng dữ liệu trên mỗi trang
        int pageSize = 10;

        // Trang hiện tại, mặc định là 1 nếu không có tham số truyền vào
        int currentPage = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            currentPage = Integer.parseInt(pageParam);
        }

        UserDAO userDao = new UserDAO();

        // Lấy tổng số lượng người dùng
        int totalUsers = userDao.getTotalUsers();

        // Tính số trang
        int totalPages = (int) Math.ceil((double) totalUsers / pageSize);

        // Lấy danh sách người dùng cho trang hiện tại
        List<User> listUser = userDao.getUsersByPage(currentPage, pageSize);

        request.setAttribute("listUser", listUser);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", currentPage);

        request.getRequestDispatcher("managerUser.jsp").forward(request, response);

    }

}
