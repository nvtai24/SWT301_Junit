/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package controller;


import static org.mockito.Mockito.*;

import dao.BookDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Book;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import dao.BookDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Book;
import model.Category;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BookDetailControllerTest {

    private BookDetailController controller;
    
    @Mock
    private HttpServletRequest request;
    
    @Mock
    private HttpServletResponse response;
    
    @Mock
    private RequestDispatcher dispatcher;
    
    @Mock
    private BookDAO bookDao;
    
    @Mock
    private Book book;
    
    @Mock
    private Category category;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = new BookDetailController();
    }

    @Test
    public void testDoGet_BookFound() throws ServletException, IOException {
        when(request.getParameter("bookId")).thenReturn("1");
        when(request.getRequestDispatcher("bookDetail.jsp")).thenReturn(dispatcher);
        when(bookDao.getBookById(1)).thenReturn(book);
        when(book.getCategoryId()).thenReturn(category);
        when(category.getCategoryId()).thenReturn(2);
        List<Book> top10Books = new ArrayList<>();
        when(bookDao.getTop10SoldBooksByCategory(2)).thenReturn(top10Books);
        
        controller.doGet(request, response);
        
        verify(request).setAttribute("book", book);
        verify(request).setAttribute("listTop10Book", top10Books);
        verify(dispatcher).forward(request, response);
    }
    
    @Test(expected = NumberFormatException.class)
    public void testDoGet_InvalidBookId() throws ServletException, IOException {
        when(request.getParameter("bookId")).thenReturn("abc");
        controller.doGet(request, response);
    }
}
