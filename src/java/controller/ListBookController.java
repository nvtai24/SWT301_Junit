package controller;

import dao.BookDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Book;

// list-book
public class ListBookController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int page = 1;
        int pageSize = 8;
        String sort = "default";

        // Sort parameter
        String sortParam = request.getParameter("sort");
        if (sortParam != null && !sortParam.isEmpty()) {
            sort = sortParam;
        }

        String categoryId = "0";
        String categoryIdParam = request.getParameter("categoryId");
        if (categoryIdParam != null && !categoryIdParam.isEmpty()) {
            categoryId = categoryIdParam;
        }

        int authorId = 0;
        String authorIdPharam = request.getParameter("authorId");
        if (authorIdPharam != null && !authorIdPharam.isEmpty()) {
            authorId = Integer.parseInt(authorIdPharam);
        }

        String search = request.getParameter("search");

        // Pagination parameters
        String pageStr = request.getParameter("page");
        if (pageStr != null) {
            page = Integer.parseInt(pageStr);
        }

        String pageSizeStr = request.getParameter("pageSize");
        if (pageSizeStr != null) {
            pageSize = Integer.parseInt(pageSizeStr);
        }

        BookDAO bookDao = new BookDAO();
        int totalBooks = bookDao.getTotalBookCount();
        int totalPages = (int) Math.ceil((double) totalBooks / pageSize);

        List<Book> listBook;

        // Handle sorting
        if (search != null) {
            listBook = bookDao.getBooksByName(search, page, pageSize);
        } else if ("priceAsc".equals(sort)) {
            listBook = bookDao.getBooksSortedByPriceAsc(page, pageSize, Integer.parseInt(categoryId));
        } else if ("priceDesc".equals(sort)) {
            listBook = bookDao.getBooksSortedByPriceDesc(page, pageSize, Integer.parseInt(categoryId));
        } else if (authorId != 0) {
            listBook = bookDao.getAllBooksByAuthorId(authorId);
        } else {
            listBook = bookDao.getBooksByPage(page, pageSize, Integer.parseInt(categoryId));
        }

        // Set attributes for JSP
        request.setAttribute("listBook", listBook);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("sort", sort);
        request.setAttribute("categoryId", Integer.valueOf(categoryId));

        if (listBook == null || listBook.isEmpty()) {
            request.setAttribute("mess", "No books found.");
        }

        request.getRequestDispatcher("listBook.jsp").forward(request, response);
    }
}
