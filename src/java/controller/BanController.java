package controller;

import dao.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class BanController extends HttpServlet {
    
    private UserDAO userDao;

    public BanController(UserDAO userDao) {
        this.userDao = userDao;
    }
    
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int userId = Integer.parseInt(request.getParameter("UserId"));
        this.userDao.banUserId(userId);
        response.sendRedirect("manager-user");
    }
}
