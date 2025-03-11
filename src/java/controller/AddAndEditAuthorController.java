package controller;

import dao.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
public class AddAndEditAuthorController extends HttpServlet {
      private UserDAO authorDAO;

    public AddAndEditAuthorController(UserDAO userDAO) {
        this.authorDAO = userDAO;
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if(action.equals("edit")){
        int authorId = Integer.parseInt(request.getParameter("authorId"));
        String authorName = request.getParameter("authorName");
        String authorImage = request.getParameter("authorImage");
        String authorDescription = request.getParameter("authorDescription");


        authorDAO.updateAuthor(authorId, authorName, authorImage, authorDescription);
        response.sendRedirect("manager-author"); 
        }
        else  if(action.equals("add")){
        String authorName = request.getParameter("authorName");
        String authorImage = request.getParameter("authorImage");
        String authorDescription = request.getParameter("authorDescription");


        authorDAO.addAuthor(authorName, authorImage, authorDescription);
        response.sendRedirect("manager-author");

        }
         else {
        // If action is invalid, still redirect
        response.sendRedirect("manager-author");
    }

    }
}
