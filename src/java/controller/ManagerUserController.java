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

    private UserDAO userDao;

    public ManagerUserController(UserDAO userDao) {
        this.userDao = userDao;
    }

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

        // ⚠️ Sửa lỗi: Dùng this.userDao thay vì tạo new UserDAO() mới
        int totalUsers = this.userDao.getTotalUsers();
        int totalPages = (int) Math.ceil((double) totalUsers / pageSize);
        List<User> listUser = this.userDao.getUsersByPage(currentPage, pageSize);

        request.setAttribute("listUser", listUser);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", currentPage);

        request.getRequestDispatcher("managerUser.jsp").forward(request, response);
    }

}
