package controller;

import dao.BookDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Book;

public class BookDetailController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        int bookId = Integer.parseInt(request.getParameter("bookId"));
        BookDAO  bookDao = new BookDAO();
        Book book = bookDao.getBookById(bookId);
        request.setAttribute("book", book);
        // tự động cộng view 
        if(request.getParameter("bookId")!= null){
          bookDao.updateViewBookById(bookId);
        }
        
        List<Book> listTop10Book = bookDao.getTop10SoldBooksByCategory(book.getCategoryId().getCategoryId());
        request.setAttribute("listTop10Book", listTop10Book);
        
        request.getRequestDispatcher("bookDetail.jsp").forward(request, response);
    } 
}
