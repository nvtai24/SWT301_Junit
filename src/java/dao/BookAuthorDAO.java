package dao;

import dal.DBConnect;
import dal.ExceptionHandlers;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Author;

public class BookAuthorDAO extends DBConnect {

    public List<Author> getAuthorsByBookId(int bookId) {
        List<Author> authors = new ArrayList<>();
        String query = "SELECT * FROM Authors "
                + "JOIN BookAuthors ON Authors.author_id = BookAuthors.author_id "
                + "WHERE BookAuthors.book_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, bookId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Author author = new Author();
                author.setAuthorId(resultSet.getInt("author_id"));
                author.setImage(resultSet.getString("image"));
                author.setDescription(resultSet.getString("description"));
                author.setAuthorName(resultSet.getString("author_name"));
                authors.add(author);
            }
        } catch (SQLException e) {
            ExceptionHandlers.handleException(e);
        }

        return authors;
    }

    public List<Author> getAllAuthor() {
        List<Author> authors = new ArrayList<>();
        String query = "SELECT * FROM Authors";

        try (PreparedStatement statement = conn.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Author author = new Author();
                author.setAuthorId(resultSet.getInt("author_id"));
                author.setImage(resultSet.getString("image"));
                author.setDescription(resultSet.getString("description"));
                author.setAuthorName(resultSet.getString("author_name"));
                authors.add(author);
            }
        } catch (SQLException e) {
            ExceptionHandlers.handleException(e);
        }

        return authors;
    }
}