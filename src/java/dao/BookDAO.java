package dao;

import dal.DBConnect;
import dal.ExceptionHandlers;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Author;
import model.Book;
import model.Category;

public class BookDAO extends DBConnect {

    public List<Book> getTop10SoldBook() {
        List<Book> topSoldBooks = new ArrayList<>();
        String query = "SELECT TOP 10 * FROM Books ORDER BY quantity_sold DESC";

        try (PreparedStatement statement = conn.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Book book = new Book();
                book.setBookId(resultSet.getInt("book_id"));

                CategoryDAO categoryDao = new CategoryDAO();
                Category category = categoryDao.getCategoryById(resultSet.getInt("category_id"));
                book.setCategoryId(category);

                book.setTitle(resultSet.getString("title"));
                book.setRating(resultSet.getInt("rating"));
                book.setPrice(resultSet.getDouble("price"));
                book.setViewCount(resultSet.getInt("view_count"));
                book.setQuantityInStock(resultSet.getInt("quantity_in_stock"));
                book.setQuantitySold(resultSet.getInt("quantity_sold"));
                book.setReleaseDate(resultSet.getDate("release_date"));
                book.setDescription(resultSet.getString("description"));
                book.setImage(resultSet.getString("image"));

                // Lấy danh sách các tác giả
                BookAuthorDAO bookAuthorDAO = new BookAuthorDAO();
                List<Author> authors = bookAuthorDAO.getAuthorsByBookId(book.getBookId());
                book.setListAuthor(authors);

                topSoldBooks.add(book);
            }
        } catch (SQLException e) {
            ExceptionHandlers.handleException(e);
        }

        return topSoldBooks;
    }

    public List<Book> getTop3ReleaseDate() {
        List<Book> topReleaseBooks = new ArrayList<>();
        String query = "SELECT TOP 3 * FROM Books ORDER BY release_date DESC";

        try (PreparedStatement statement = conn.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Book book = new Book();
                book.setBookId(resultSet.getInt("book_id"));

                CategoryDAO categoryDao = new CategoryDAO();
                Category category = categoryDao.getCategoryById(resultSet.getInt("category_id"));
                book.setCategoryId(category);

                book.setTitle(resultSet.getString("title"));
                book.setRating(resultSet.getInt("rating"));
                book.setPrice(resultSet.getDouble("price"));
                book.setViewCount(resultSet.getInt("view_count"));
                book.setQuantityInStock(resultSet.getInt("quantity_in_stock"));
                book.setQuantitySold(resultSet.getInt("quantity_sold"));
                book.setReleaseDate(resultSet.getDate("release_date"));
                book.setDescription(resultSet.getString("description"));
                book.setImage(resultSet.getString("image"));

                // Retrieve authors
                BookAuthorDAO bookAuthorDAO = new BookAuthorDAO();
                List<Author> authors = bookAuthorDAO.getAuthorsByBookId(book.getBookId());
                book.setListAuthor(authors);

                topReleaseBooks.add(book);
            }
        } catch (SQLException e) {
            ExceptionHandlers.handleException(e);
        }

        return topReleaseBooks;
    }

    public List<Book> getTop10View() {
        List<Book> topViewedBooks = new ArrayList<>();
        String query = "SELECT TOP 10 * FROM Books ORDER BY view_count DESC";

        try (PreparedStatement statement = conn.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Book book = new Book();
                book.setBookId(resultSet.getInt("book_id"));

                CategoryDAO categoryDao = new CategoryDAO();
                Category category = categoryDao.getCategoryById(resultSet.getInt("category_id"));
                book.setCategoryId(category);

                book.setTitle(resultSet.getString("title"));
                book.setRating(resultSet.getInt("rating"));
                book.setPrice(resultSet.getDouble("price"));
                book.setViewCount(resultSet.getInt("view_count"));
                book.setQuantityInStock(resultSet.getInt("quantity_in_stock"));
                book.setQuantitySold(resultSet.getInt("quantity_sold"));
                book.setReleaseDate(resultSet.getDate("release_date"));
                book.setDescription(resultSet.getString("description"));
                book.setImage(resultSet.getString("image"));

                // Retrieve authors
                BookAuthorDAO bookAuthorDAO = new BookAuthorDAO();
                List<Author> authors = bookAuthorDAO.getAuthorsByBookId(book.getBookId());
                book.setListAuthor(authors);

                topViewedBooks.add(book);
            }
        } catch (SQLException e) {
            ExceptionHandlers.handleException(e);
        }

        return topViewedBooks;
    }

    public List<Book> getAllBooks() {
        List<Book> allBooks = new ArrayList<>();
        String query = "SELECT * FROM Books";

        try (PreparedStatement statement = conn.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Book book = new Book();
                book.setBookId(resultSet.getInt("book_id"));

                CategoryDAO categoryDao = new CategoryDAO();
                Category category = categoryDao.getCategoryById(resultSet.getInt("category_id"));
                book.setCategoryId(category);

                book.setTitle(resultSet.getString("title"));
                book.setRating(resultSet.getInt("rating"));
                book.setPrice(resultSet.getDouble("price"));
                book.setViewCount(resultSet.getInt("view_count"));
                book.setQuantityInStock(resultSet.getInt("quantity_in_stock"));
                book.setQuantitySold(resultSet.getInt("quantity_sold"));
                book.setReleaseDate(resultSet.getDate("release_date"));
                book.setDescription(resultSet.getString("description"));
                book.setImage(resultSet.getString("image"));

                // Retrieve authors
                BookAuthorDAO bookAuthorDAO = new BookAuthorDAO();
                List<Author> authors = bookAuthorDAO.getAuthorsByBookId(book.getBookId());
                book.setListAuthor(authors);

                allBooks.add(book);
            }
        } catch (SQLException e) {
            ExceptionHandlers.handleException(e);
        }

        return allBooks;
    }

    public int getTotalBookCount() {
        String query = "SELECT COUNT(*) FROM Books";
        try (PreparedStatement statement = conn.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            ExceptionHandlers.handleException(e);
        }
        return 0;
    }

    public List<Book> getBooksByPage(int page, int pageSize, int categoryId) {
        List<Book> books = new ArrayList<>();
        int offset = (page - 1) * pageSize;

        String query;
        if (categoryId == 0) {
            query = "SELECT * FROM Books ORDER BY book_id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        } else {
            query = "SELECT * FROM Books WHERE category_id = ? ORDER BY book_id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        }

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            if (categoryId == 0) {
                statement.setInt(1, offset);     // Bind the offset
                statement.setInt(2, pageSize);   // Bind the fetch size
            } else {
                statement.setInt(1, categoryId); // Bind the category_id
                statement.setInt(2, offset);     // Bind the offset
                statement.setInt(3, pageSize);   // Bind the fetch size
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Book book = new Book();
                    book.setBookId(resultSet.getInt("book_id"));

                    CategoryDAO categoryDao = new CategoryDAO();
                    Category category = categoryDao.getCategoryById(resultSet.getInt("category_id"));
                    book.setCategoryId(category);

                    book.setTitle(resultSet.getString("title"));
                    book.setRating(resultSet.getInt("rating"));
                    book.setPrice(resultSet.getDouble("price"));
                    book.setViewCount(resultSet.getInt("view_count"));
                    book.setQuantityInStock(resultSet.getInt("quantity_in_stock"));
                    book.setQuantitySold(resultSet.getInt("quantity_sold"));
                    book.setReleaseDate(resultSet.getDate("release_date"));
                    book.setDescription(resultSet.getString("description"));
                    book.setImage(resultSet.getString("image"));

                    // Retrieve authors
                    BookAuthorDAO bookAuthorDAO = new BookAuthorDAO();
                    List<Author> authors = bookAuthorDAO.getAuthorsByBookId(book.getBookId());
                    book.setListAuthor(authors);

                    books.add(book);
                }
            }
        } catch (SQLException e) {
            ExceptionHandlers.handleException(e);
        }
        return books;
    }

    public List<Book> getBooksSortedByPriceAsc(int page, int pageSize, int categoryId) {
        List<Book> books = new ArrayList<>();
        int offset = (page - 1) * pageSize;

        String query;
        if (categoryId == 0) {
            query = "SELECT * FROM Books ORDER BY [price] Asc OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        } else {
            query = "SELECT * FROM Books WHERE category_id = ? ORDER BY [price] Asc OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        }

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            if (categoryId == 0) {
                statement.setInt(1, offset);     // Bind the offset
                statement.setInt(2, pageSize);   // Bind the fetch size
            } else {
                statement.setInt(1, categoryId); // Bind the category_id
                statement.setInt(2, offset);     // Bind the offset
                statement.setInt(3, pageSize);   // Bind the fetch size
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Book book = new Book();
                    book.setBookId(resultSet.getInt("book_id"));

                    CategoryDAO categoryDao = new CategoryDAO();
                    Category category = categoryDao.getCategoryById(resultSet.getInt("category_id"));
                    book.setCategoryId(category);

                    book.setTitle(resultSet.getString("title"));
                    book.setRating(resultSet.getInt("rating"));
                    book.setPrice(resultSet.getDouble("price"));
                    book.setViewCount(resultSet.getInt("view_count"));
                    book.setQuantityInStock(resultSet.getInt("quantity_in_stock"));
                    book.setQuantitySold(resultSet.getInt("quantity_sold"));
                    book.setReleaseDate(resultSet.getDate("release_date"));
                    book.setDescription(resultSet.getString("description"));
                    book.setImage(resultSet.getString("image"));

                    // Retrieve authors
                    BookAuthorDAO bookAuthorDAO = new BookAuthorDAO();
                    List<Author> authors = bookAuthorDAO.getAuthorsByBookId(book.getBookId());
                    book.setListAuthor(authors);
                    books.add(book);
                }
            }
        } catch (SQLException e) {
            ExceptionHandlers.handleException(e);
        }
        return books;
    }

    public List<Book> getBooksSortedByPriceDesc(int page, int pageSize, int categoryId) {
        List<Book> books = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        String query;
        if (categoryId == 0) {
            query = "SELECT * FROM Books ORDER BY [price] DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        } else {
            query = "SELECT * FROM Books WHERE category_id = ? ORDER BY [price] DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        }

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            if (categoryId == 0) {
                statement.setInt(1, offset);     // Bind the offset
                statement.setInt(2, pageSize);   // Bind the fetch size
            } else {
                statement.setInt(1, categoryId); // Bind the category_id
                statement.setInt(2, offset);     // Bind the offset
                statement.setInt(3, pageSize);   // Bind the fetch size
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Book book = new Book();
                    book.setBookId(resultSet.getInt("book_id"));

                    CategoryDAO categoryDao = new CategoryDAO();
                    Category category = categoryDao.getCategoryById(resultSet.getInt("category_id"));
                    book.setCategoryId(category);

                    book.setTitle(resultSet.getString("title"));
                    book.setRating(resultSet.getInt("rating"));
                    book.setPrice(resultSet.getDouble("price"));
                    book.setViewCount(resultSet.getInt("view_count"));
                    book.setQuantityInStock(resultSet.getInt("quantity_in_stock"));
                    book.setQuantitySold(resultSet.getInt("quantity_sold"));
                    book.setReleaseDate(resultSet.getDate("release_date"));
                    book.setDescription(resultSet.getString("description"));
                    book.setImage(resultSet.getString("image"));

                    // Retrieve authors
                    BookAuthorDAO bookAuthorDAO = new BookAuthorDAO();
                    List<Author> authors = bookAuthorDAO.getAuthorsByBookId(book.getBookId());
                    book.setListAuthor(authors);
                    books.add(book);
                }
            }
        } catch (SQLException e) {
            ExceptionHandlers.handleException(e);
        }
        return books;
    }

    public Book getBookById(int bookId) {
        Book book = null;
        String query = "SELECT * FROM Books WHERE book_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, bookId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    book = new Book();
                    book.setBookId(resultSet.getInt("book_id"));

                    CategoryDAO categoryDao = new CategoryDAO();
                    Category category = categoryDao.getCategoryById(resultSet.getInt("category_id"));
                    book.setCategoryId(category);

                    book.setTitle(resultSet.getString("title"));
                    book.setRating(resultSet.getInt("rating"));
                    book.setPrice(resultSet.getDouble("price"));
                    book.setViewCount(resultSet.getInt("view_count"));
                    book.setQuantityInStock(resultSet.getInt("quantity_in_stock"));
                    book.setQuantitySold(resultSet.getInt("quantity_sold"));
                    book.setReleaseDate(resultSet.getDate("release_date"));
                    book.setDescription(resultSet.getString("description"));
                    book.setImage(resultSet.getString("image"));

                    // Retrieve authors
                    BookAuthorDAO bookAuthorDAO = new BookAuthorDAO();
                    List<Author> authors = bookAuthorDAO.getAuthorsByBookId(book.getBookId());
                    book.setListAuthor(authors);
                }
            }
        } catch (SQLException e) {
            ExceptionHandlers.handleException(e);
        }

        return book;
    }

    public List<Book> getAllBooksByAuthorId(int authorId) {
        List<Book> books = new ArrayList<>();
        String query = "SELECT Books.* FROM Books "
                + "JOIN BookAuthors ON Books.book_id = BookAuthors.book_id "
                + "WHERE BookAuthors.author_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, authorId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Book book = new Book();
                book.setBookId(resultSet.getInt("book_id"));

                CategoryDAO categoryDao = new CategoryDAO();
                Category category = categoryDao.getCategoryById(resultSet.getInt("category_id"));
                book.setCategoryId(category);

                book.setTitle(resultSet.getString("title"));
                book.setRating(resultSet.getInt("rating"));
                book.setPrice(resultSet.getDouble("price"));
                book.setViewCount(resultSet.getInt("view_count"));
                book.setQuantityInStock(resultSet.getInt("quantity_in_stock"));
                book.setQuantitySold(resultSet.getInt("quantity_sold"));
                book.setReleaseDate(resultSet.getDate("release_date"));
                book.setDescription(resultSet.getString("description"));
                book.setImage(resultSet.getString("image"));

                // Retrieve authors
                BookAuthorDAO bookAuthorDAO = new BookAuthorDAO();
                List<Author> authors = bookAuthorDAO.getAuthorsByBookId(book.getBookId());
                book.setListAuthor(authors);

                books.add(book);
            }
        } catch (SQLException e) {
            ExceptionHandlers.handleException(e);
        }

        return books;
    }

    public void updateViewBookById(int bookId) {
        String query = "UPDATE Books SET view_count = view_count + 1 WHERE book_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, bookId);
             statement.executeUpdate();
        } catch (SQLException e) {
            ExceptionHandlers.handleException(e);
        }
    }

    public List<Book> getBooksByName(String bookName, int page, int pageSize) {
        List<Book> books = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        String query = "SELECT * FROM Books WHERE title LIKE ? ORDER BY book_id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, "%" + bookName + "%"); // Bind book name for search
            statement.setInt(2, offset);     // Bind the offset
            statement.setInt(3, pageSize);   // Bind the fetch size

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Book book = new Book();
                    book.setBookId(resultSet.getInt("book_id"));

                    CategoryDAO categoryDao = new CategoryDAO();
                    Category category = categoryDao.getCategoryById(resultSet.getInt("category_id"));
                    book.setCategoryId(category);

                    book.setTitle(resultSet.getString("title"));
                    book.setRating(resultSet.getInt("rating"));
                    book.setPrice(resultSet.getDouble("price"));
                    book.setViewCount(resultSet.getInt("view_count"));
                    book.setQuantityInStock(resultSet.getInt("quantity_in_stock"));
                    book.setQuantitySold(resultSet.getInt("quantity_sold"));
                    book.setReleaseDate(resultSet.getDate("release_date"));
                    book.setDescription(resultSet.getString("description"));
                    book.setImage(resultSet.getString("image"));

                    // Retrieve authors
                    BookAuthorDAO bookAuthorDAO = new BookAuthorDAO();
                    List<Author> authors = bookAuthorDAO.getAuthorsByBookId(book.getBookId());
                    book.setListAuthor(authors);

                    books.add(book);
                }
            }
        } catch (SQLException e) {
            ExceptionHandlers.handleException(e);
        }
        return books;
    }

    public boolean addAuthorsForBook(int bookId, List<Author> authorList) {
        String insertAuthorsQuery = "INSERT INTO BookAuthors (book_id, author_id) VALUES (?, ?)";
        boolean allAuthorsAdded = true;

        try (PreparedStatement statement = conn.prepareStatement(insertAuthorsQuery)) {
            for (Author author : authorList) {
                statement.setInt(1, bookId);
                statement.setInt(2, author.getAuthorId());
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted <= 0) {
                    allAuthorsAdded = false;
                    break;
                }
            }
        } catch (SQLException e) {
            ExceptionHandlers.handleException(e);
            allAuthorsAdded = false;
        }

        return allAuthorsAdded;
    }

    public boolean addNewBook(int categoryId, String title, int rating, double price, String description, String image, int quantity_in_stock) {
        String query = "INSERT INTO Books (category_id, title, rating, price, description, image, quantity_in_stock) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, categoryId);
            statement.setString(2, title);
            statement.setInt(3, rating);
            statement.setDouble(4, price);
            statement.setString(5, description);
            statement.setString(6, image);
            statement.setInt(7, quantity_in_stock);

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            ExceptionHandlers.handleException(e);
        }
        return false;
    }

    public Book getNewestBook() {
        String query = "SELECT TOP 1 * FROM Books ORDER BY book_id DESC";

        try (PreparedStatement statement = conn.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                Book book = new Book();
                book.setBookId(resultSet.getInt("book_id"));

                CategoryDAO categoryDao = new CategoryDAO();
                Category category = categoryDao.getCategoryById(resultSet.getInt("category_id"));
                book.setCategoryId(category);

                book.setTitle(resultSet.getString("title"));
                book.setRating(resultSet.getInt("rating"));
                book.setPrice(resultSet.getDouble("price"));
                book.setViewCount(resultSet.getInt("view_count"));
                book.setQuantityInStock(resultSet.getInt("quantity_in_stock"));
                book.setQuantitySold(resultSet.getInt("quantity_sold"));
                book.setReleaseDate(resultSet.getDate("release_date"));
                book.setDescription(resultSet.getString("description"));
                book.setImage(resultSet.getString("image"));

                // Retrieve authors
                BookAuthorDAO bookAuthorDAO = new BookAuthorDAO();
                List<Author> authors = bookAuthorDAO.getAuthorsByBookId(book.getBookId());
                book.setListAuthor(authors);

                return book;

            }
        } catch (SQLException e) {
            ExceptionHandlers.handleException(e);
        }
        return null;
    }

    public boolean updateBook(int bookId, int categoryId, String title, int rating, double price, String description, String image, int quantity_in_stock) {
        String query = "UPDATE Books SET category_id = ?, title = ?, rating = ?, price = ?, description = ?, image = ?, quantity_in_stock = ? WHERE book_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, categoryId);
            statement.setString(2, title);
            statement.setInt(3, rating);
            statement.setDouble(4, price);
            statement.setString(5, description);
            statement.setString(6, image);
            statement.setInt(7, quantity_in_stock);
            statement.setInt(8, bookId);

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            ExceptionHandlers.handleException(e);
        }
        return false;
    }

    public boolean deleteAuthorsForBook(int bookId) {
        String deleteQuery = "DELETE FROM BookAuthors WHERE book_id = ?";
        boolean success = false;

        try (PreparedStatement statement = conn.prepareStatement(deleteQuery)) {
            statement.setInt(1, bookId);

            int rowsDeleted = statement.executeUpdate();
            success = rowsDeleted > 0;
        } catch (SQLException e) {
            ExceptionHandlers.handleException(e);
        }

        return success;
    }

    public boolean deleteOrderDetailsForBook(int bookId) {
        String deleteQuery = "DELETE FROM OrderDetails WHERE book_id = ?";
        boolean success = false;

        try (PreparedStatement statement = conn.prepareStatement(deleteQuery)) {
            statement.setInt(1, bookId);

            int rowsDeleted = statement.executeUpdate();
            success = rowsDeleted > 0;
        } catch (SQLException e) {
            ExceptionHandlers.handleException(e);
        }

        return success;
    }

    public boolean deleteBookById(int bookId) {
        boolean success = false;

        // Delete from OrderDetails
        boolean orderDetailsDeleted = deleteOrderDetailsForBook(bookId);

        deleteAuthorsForBook(bookId);

        // Delete from Books
        String deleteQuery = "DELETE FROM Books WHERE book_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(deleteQuery)) {
            statement.setInt(1, bookId);
            statement.executeUpdate();
            success = true;
        } catch (SQLException e) {
            ExceptionHandlers.handleException(e);
        }

        return success && orderDetailsDeleted;
    }

    public List<Book> getTop10SoldBooksByCategory(int categoryId) {
        List<Book> topSoldBooks = new ArrayList<>();
            String query = "  SELECT TOP 10 * FROM Books WHERE category_id = ? ORDER BY [book_id] DESC"; // Standard SQL syntax

        try (PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setInt(1, categoryId);
            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Book book = new Book();
                    book.setBookId(resultSet.getInt("book_id"));

                    CategoryDAO categoryDao = new CategoryDAO();
                    Category category = categoryDao.getCategoryById(resultSet.getInt("category_id"));
                    book.setCategoryId(category);

                    book.setTitle(resultSet.getString("title"));
                    book.setRating(resultSet.getInt("rating"));
                    book.setPrice(resultSet.getDouble("price"));
                    book.setViewCount(resultSet.getInt("view_count"));
                    book.setQuantityInStock(resultSet.getInt("quantity_in_stock"));
                    book.setQuantitySold(resultSet.getInt("quantity_sold"));
                    book.setReleaseDate(resultSet.getDate("release_date"));
                    book.setDescription(resultSet.getString("description"));
                    book.setImage(resultSet.getString("image"));

                    // Retrieve authors
                    BookAuthorDAO bookAuthorDAO = new BookAuthorDAO();
                    List<Author> authors = bookAuthorDAO.getAuthorsByBookId(book.getBookId());
                    book.setListAuthor(authors);

                    topSoldBooks.add(book);
                }
            }
        } catch (SQLException e) {
            ExceptionHandlers.handleException(e);
        }

        return topSoldBooks;
    }
}
