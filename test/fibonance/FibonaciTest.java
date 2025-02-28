
package fibonance;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author VICTUS
 */
public class FibonaciTest {
    
    @Test
    public void testFibonacciBaseCases() {
        assertEquals(0, Fibonaci.fibonacci(0));
        assertEquals(1, Fibonaci.fibonacci(1));
    }

    @Test
    public void testFibonacciSmallNumbers() {
        assertEquals(1, Fibonaci.fibonacci(2));
        assertEquals(2, Fibonaci.fibonacci(3));
    }

    @Test
    public void testFibonacciLargeNumbers() {
        assertEquals(6765, Fibonaci.fibonacci(20));
    }

   @Test(expected = IllegalArgumentException.class)
    public void testFibonacciNegativeInput() {
        Fibonaci.fibonacci(-5);
    }

    @Test(expected = ArithmeticException.class)
    public void testFibonacciOverflow() {
        Fibonaci.fibonacci(47); 
    }
    
    @Test
    public void testPrivateConstructor() throws Exception {
        Constructor<Fibonaci> constructor = Fibonaci.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        try {
            constructor.newInstance();
            fail("Should have thrown IllegalStateException");
        } catch (InvocationTargetException e) {
            assertTrue(e.getCause() instanceof IllegalStateException);
            assertEquals("Utility class", e.getCause().getMessage());
        }
    }
    
}
