package controller;

import dao.BookDAO;
import dao.CartDAO;
import dao.CategoryDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Book;
import model.Category;
import model.Order;
import model.User;

public class HomeController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // lấy ra 10 quyển có     quantity_sold nhiều nhất
        BookDAO bookDao = new BookDAO();
        List<Book> listTop10SoldBook = bookDao.getTop10SoldBook();
        request.setAttribute("listTop10SoldBook", listTop10SoldBook);

        // lấy ra top3 quyển mới nhất
        List<Book> listTop3ReleaseDate = bookDao.getTop3ReleaseDate();
        request.setAttribute("listTop3ReleaseDate", listTop3ReleaseDate);

        // lấy ra danh số sách mỗi loại 
        CategoryDAO categoryDao = new CategoryDAO();
        List<Category> countBookInCategory = categoryDao.countBooksInCategories();
        session.setAttribute("countBookInCategory", countBookInCategory);

        // lấy ra top10 lượt view nhiều nhất
        List<Book> listTop10View = bookDao.getTop10View();
        request.setAttribute("listTop10View", listTop10View);

        // lấy ra danh sách category
        List<Category> listCategory = categoryDao.getAllCategories();
        session.setAttribute("listCategory", listCategory);

        User user = (User) session.getAttribute("username");
        if (user != null) {
            // lấy ra số book trong cart 
            CartDAO cartDao = new CartDAO();
            int countBookInCart;
            Order order = cartDao.getOrderNew(user.getUserId());
            if (order != null) {
                countBookInCart = cartDao.countDistinctBooksInCart(user.getUserId());
                session.setAttribute("countBookInCart", countBookInCart);
            } else {
                cartDao.createNewOrder(user.getUserId());
                request.setAttribute("mess", "No book in the cart");
            }

        }

        request.getRequestDispatcher("home.jsp").forward(request, response);
    }
}
