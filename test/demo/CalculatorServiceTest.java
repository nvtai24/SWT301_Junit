package demo;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author Nvtai
 */
public class CalculatorServiceTest {
    
    @Mock
    private DatabaseService mockDatabaseService;
    
    private CalculatorService calculatorService;
    
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        calculatorService = new CalculatorService(mockDatabaseService);
    }
    

    @Test
    public void testGetStoredValue() {
        
        Mockito.when(mockDatabaseService.getValue()).thenReturn(100);
        
        assertEquals(100, calculatorService.getStoredValue());
        
        Mockito.verify(mockDatabaseService, Mockito.times(1)).getValue();
    }
    
}
