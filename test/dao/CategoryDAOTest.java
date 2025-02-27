/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package dao;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Category;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CategoryDAOTest {

    private CategoryDAO categoryDAO;

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        categoryDAO = new CategoryDAO();
        categoryDAO.conn = mockConnection; // Inject mock connection
    }

    @Test
    public void testGetCategoryById_ValidId_ShouldReturnCategory() throws Exception {
        int categoryId = 1;
        String categoryName = "Science";

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).then Return(mockResultSet);
        when(mockResultSet.next()).thenReturn(true); // Có dữ liệu
        when(mockResultSet.getInt("category_id")).thenReturn(categoryId);
        when(mockResultSet.getString("category_name")).thenReturn(categoryName);

        Category result = categoryDAO.getCategoryById(categoryId);

        assertNotNull(result);
        assertEquals(categoryId, result.getCategoryId());
        assertEquals(categoryName, result.getCategoryName());
    }

    @Test
    public void testGetCategoryById_InvalidId_ShouldReturnNull() throws Exception {
        int categoryId = 99; // Giả định không có ID này trong DB

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // Không có dữ liệu

        Category result = categoryDAO.getCategoryById(categoryId);

        assertNull(result);
    }

    @Test
    public void testGetCategoryById_SQLException_ShouldHandleException() throws Exception {
        int categoryId = 1;

        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        Category result = categoryDAO.getCategoryById(categoryId);

        assertNull(result); // Khi có lỗi SQL, hàm nên trả về null
    }

    @Test
    public void testGetAllCategories_WithCategories_ShouldReturnList() throws Exception {
        List<Category> expectedCategories = new ArrayList<>();
        expectedCategories.add(new Category(1, "Fiction"));
        expectedCategories.add(new Category(2, "Non-fiction"));
        expectedCategories.add(new Category(3, "Science"));

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, true, false); // 3 categories
        when(mockResultSet.getInt("category_id")).thenReturn(1, 2, 3);
        when(mockResultSet.getString("category_name")).thenReturn("Fiction", "Non-fiction", "Science");

        List<Category> result = categoryDAO.getAllCategories();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(expectedCategories.get(0).getCategoryId(), result.get(0).getCategoryId());
        assertEquals(expectedCategories.get(1).getCategoryId(), result.get(1).getCategoryId());
        assertEquals(expectedCategories.get(2).getCategoryId(), result.get(2).getCategoryId());
    }

    @Test
    public void testGetAllCategories_EmptyResultSet_ShouldReturnEmptyList() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // Không có dữ liệu

        List<Category> result = categoryDAO.getAllCategories();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAllCategories_SQLException_ShouldHandleExceptionAndReturnEmptyList() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        List<Category> result = categoryDAO.getAllCategories();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAddCategory_ValidCategory_ShouldReturnTrue() throws Exception {
        Category category = new Category(0, "New Category");

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1); // 1 dòng bị ảnh hưởng -> thành công

        boolean result = categoryDAO.addCategory(category);

        assertTrue(result);
        verify(mockPreparedStatement).setString(1, category.getCategoryName());
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testAddCategory_NoRowsAffected_ShouldReturnFalse() throws Exception {
        Category category = new Category(0, "New Category");

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0); // Không có dòng nào bị ảnh hưởng

        boolean result = categoryDAO.addCategory(category);

        assertFalse(result);
    }

    @Test
    public void testAddCategory_SQLException_ShouldReturnFalse() throws Exception {
        Category category = new Category();

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        boolean result = categoryDAO.addCategory(category);

        assertFalse(result);
    }

    @Test
    public void testUpdateCategory_ValidCategory_ShouldReturnTrue() throws Exception {
        Category category = new Category(1, "Updated Category");

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1); // 1 dòng bị ảnh hưởng -> thành công

        boolean result = categoryDAO.updateCategory(category);

        assertTrue(result);
        verify(mockPreparedStatement).setString(1, category.getCategoryName());
        verify(mockPreparedStatement).setInt(2, category.getCategoryId());
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testUpdateCategory_NoRowsAffected_ShouldReturnFalse() throws Exception {
        Category category = new Category(99, "Updated Category"); // Giả định ID này không tồn tại

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0); // Không có dòng nào bị ảnh hưởng

        boolean result = categoryDAO.updateCategory(category);

        assertFalse(result);
    }

    @Test
    public void testUpdateCategory_SQLException_ShouldReturnFalse() throws Exception {
        Category category = new Category(1, "Updated Category");

        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        boolean result = categoryDAO.updateCategory(category);

        assertFalse(result);
    }

    @Test
    public void testDeleteCategory_ValidId_ShouldReturnTrue() throws Exception {
        int categoryId = 1;

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        boolean result = categoryDAO.deleteCategory(categoryId);

        assertTrue(result);
        verify(mockPreparedStatement).setInt(1, categoryId);
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testDeleteCategory_NoRowsAffected_ShouldReturnFalse() throws Exception {
        int categoryId = 99;

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        boolean result = categoryDAO.deleteCategory(categoryId);

        assertFalse(result);
    }

    @Test
    public void testDeleteCategory_SQLException_ShouldReturnFalse() throws Exception {
        int categoryId = 1;

        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        boolean result = categoryDAO.deleteCategory(categoryId);

        assertFalse(result);
    }

    @Test
    public void testCountBooksInCategories_WithCategories_ShouldReturnList() throws Exception {
        List<Category> expectedCategories = new ArrayList<>();
        expectedCategories.add(new Category(1, "Fiction", 5));
        expectedCategories.add(new Category(2, "Non-fiction", 8));
        expectedCategories.add(new Category(3, "Science", 3));

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, true, false); // 3 categories
        when(mockResultSet.getInt("category_id")).thenReturn(1, 2, 3);
        when(mockResultSet.getString("category_name")).thenReturn("Fiction", "Non-fiction", "Science");
        when(mockResultSet.getInt("book_count")).thenReturn(5, 8, 3);

        List<Category> result = categoryDAO.countBooksInCategories();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(expectedCategories.get(0).getCategoryId(), result.get(0).getCategoryId());
        assertEquals(expectedCategories.get(1).getCategoryId(), result.get(1).getCategoryId());
        assertEquals(expectedCategories.get(2).getCategoryId(), result.get(2).getCategoryId());

        assertEquals(expectedCategories.get(0).getBookCount(), result.get(0).getBookCount());
        assertEquals(expectedCategories.get(1).getBookCount(), result.get(1).getBookCount());
        assertEquals(expectedCategories.get(2).getBookCount(), result.get(2).getBookCount());
    }

    @Test
    public void testCountBooksInCategories_EmptyResultSet_ShouldReturnEmptyList() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // Không có dữ liệu

        List<Category> result = categoryDAO.countBooksInCategories();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCountBooksInCategories_SQLException_ShouldHandleExceptionAndReturnEmptyList() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        List<Category> result = categoryDAO.countBooksInCategories();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}
