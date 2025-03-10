package dao;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import model.User;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.sql.*;
import java.util.Arrays;
import model.Role;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class UserDAOTest {

    private UserDAO userDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @Before
    public void setUp() throws Exception {
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        mockResultSet = Mockito.mock(ResultSet.class);
        userDAO = new UserDAO();
        userDAO.conn = mockConnection;

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @Test
    public void testUpdateAuthor_Success() throws SQLException {
        String updateQuery = "UPDATE Authors SET author_name = ?, image = ?, description = ? WHERE author_id = ?";
        Mockito.when(mockConnection.prepareStatement(updateQuery)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        userDAO.updateAuthor(1, "New Author", "new_image.jpg", "Updated description");

        Mockito.verify(mockPreparedStatement).setString(1, "New Author");
        Mockito.verify(mockPreparedStatement).setString(2, "new_image.jpg");
        Mockito.verify(mockPreparedStatement).setString(3, "Updated description");
        Mockito.verify(mockPreparedStatement).setInt(4, 1);
        Mockito.verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testUpdateAuthor_Failure() throws SQLException {
        String updateQuery = "UPDATE Authors SET author_name = ?, image = ?, description = ? WHERE author_id = ?";
        Mockito.when(mockConnection.prepareStatement(updateQuery)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        userDAO.updateAuthor(1, "New Author", "new_image.jpg", "Updated description");

        Mockito.verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testGetUsersByPage_Success() throws SQLException {
        String query = "SELECT * FROM Users ORDER BY user_id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        when(mockConnection.prepareStatement(query)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true, false); // Chỉ có 1 user
        when(mockResultSet.getInt("user_id")).thenReturn(1);
        when(mockResultSet.getString("username")).thenReturn("user1");
        when(mockResultSet.getString("email")).thenReturn("email1@example.com");
        when(mockResultSet.getString("full_name")).thenReturn("User One");
        when(mockResultSet.getInt("role_id")).thenReturn(1);
        when(mockResultSet.getDate("registration_date")).thenReturn(new java.sql.Date(System.currentTimeMillis()));
        when(mockResultSet.getString("status")).thenReturn("active");

        List<User> userList = userDAO.getUsersByPage(1, 10);

        assertNotNull(userList);
        assertEquals(1, userList.size());

        User user = userList.get(0);
        assertEquals(1, user.getUserId());
        assertEquals("user1", user.getUsername());
        assertEquals("email1@example.com", user.getEmail());
        assertEquals("User One", user.getFullName());
        assertEquals("active", user.getStatus());

        verify(mockPreparedStatement).setInt(1, 0);
        verify(mockPreparedStatement).setInt(2, 10);
        verify(mockResultSet, times(2)).next();
    }

    @Test
    public void testGetUsersByPage_EmptyResultSet() throws SQLException {
        String query = "SELECT * FROM Users ORDER BY user_id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        when(mockConnection.prepareStatement(query)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(false); // Không có dữ liệu

        List<User> userList = userDAO.getUsersByPage(1, 10);

        assertNotNull(userList);
        assertEquals(0, userList.size());
    }

    @Test
    public void testGetUsersByPage_SQLException() throws SQLException {
        String query = "SELECT * FROM Users ORDER BY user_id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        when(mockConnection.prepareStatement(query)).thenThrow(new SQLException("Database error"));

        SQLException thrownException = assertThrows(SQLException.class, () -> userDAO.getUsersByPage(1, 10));

        assertEquals("Database error", thrownException.getMessage());
    }

    @Test
    public void testGetUsersByPage_InvalidCurrentPage() {
        IllegalArgumentException thrownException1 = assertThrows(
                IllegalArgumentException.class,
                () -> userDAO.getUsersByPage(0, 10)
        );
        assertEquals("currentPage must be greater than 0", thrownException1.getMessage());

        IllegalArgumentException thrownException2 = assertThrows(
                IllegalArgumentException.class,
                () -> userDAO.getUsersByPage(-1, 10)
        );
        assertEquals("currentPage must be greater than 0", thrownException2.getMessage());
    }

    @Test
    public void testGetUsersByPage_InvalidPageSize() {
        IllegalArgumentException thrownException1 = assertThrows(
                IllegalArgumentException.class,
                () -> userDAO.getUsersByPage(1, 0)
        );
        assertEquals("pageSize must be greater than 0", thrownException1.getMessage());

        IllegalArgumentException thrownException2 = assertThrows(
                IllegalArgumentException.class,
                () -> userDAO.getUsersByPage(1, -5)
        );
        assertEquals("pageSize must be greater than 0", thrownException2.getMessage());
    }

    @Test
    public void testDeleteUserData_Success() throws SQLException {
        int userId = 1;

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        boolean result = userDAO.deleteUserData(userId);

        assertTrue(result);

        verify(mockPreparedStatement, times(3)).executeUpdate();
    }

    @Test
    public void testDeleteUserData_Failure() throws SQLException {
        int userId = -2;

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0); // Giả lập không có dòng nào bị ảnh hưởng (xóa thất bại)

        boolean result = userDAO.deleteUserData(userId);

        assertFalse(result);
    }

    @Test
    public void testDeleteUserData_ExceptionMessage() throws SQLException {
        int userId = 3;

        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        SQLException thrownException = assertThrows(SQLException.class, () -> userDAO.deleteUserData(userId));

        assertEquals("Database error", thrownException.getMessage());
    }

}
