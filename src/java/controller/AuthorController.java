package controller;

import dao.BookAuthorDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Author;

public class AuthorController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        BookAuthorDAO bookAuthorDao = new BookAuthorDAO();
        List<Author> listAuthor = bookAuthorDao.getAllAuthor();
        request.setAttribute("listAuthor", listAuthor);
        request.getRequestDispatcher("author.jsp").forward(request, response);
    }
}
