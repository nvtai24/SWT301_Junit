package taxifarecalculator;

import static org.junit.Assert.*;
import org.junit.Test;

public class TaxiFareCalculatorTest {

    @Test
    public void testNegativeDistance() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            TaxiFareCalculator.calculateFare(-1, 0, false);
        });
        assertEquals("Distance must be non-negative.", exception.getMessage());
    }

    @Test
    public void testNegativeWaitingTime() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            TaxiFareCalculator.calculateFare(0, -1, false);
        });
        assertEquals("Waiting time must be non-negative.", exception.getMessage());
    }
    
    
     @Test
    public void testNoDistance() {
        assertEquals(0, TaxiFareCalculator.calculateFare(0, 0, false), 0.1);
    }

    @Test
    public void testShortDistanceWaitingDay() {
        assertEquals(10000, TaxiFareCalculator.calculateFare(1, 0, false), 0.1);
    }

    @Test
    public void testShortDistanceWaitingNight() {
        assertEquals(12000, TaxiFareCalculator.calculateFare(1, 0, true), 0.1);
    }

    @Test
    public void testShortDistanceNoWaitingDay() {
        assertEquals(10000, TaxiFareCalculator.calculateFare(1, 0, false), 0.1);
    }

    @Test
    public void testShortDistanceNoWaitingNight() {
        assertEquals(12000, TaxiFareCalculator.calculateFare(1, 0, true), 0.1);
    }

    @Test
    public void testMediumDistanceNoWaitingDay() {
        assertEquals(86500, TaxiFareCalculator.calculateFare(10, 0, false), 0.1);
    }

    @Test
    public void testMediumDistanceNoWaitingNight() {
        assertEquals(103800, TaxiFareCalculator.calculateFare(10, 0, true), 0.1);
    }

    @Test
    public void testMediumDistanceSomeWaitingDay() {
        assertEquals(91500, TaxiFareCalculator.calculateFare(10, 5, false), 0.1);
    }

    @Test
    public void testMediumDistanceSomeWaitingNight() {
        assertEquals(109800, TaxiFareCalculator.calculateFare(10, 5, true), 0.1);
    }

    @Test
    public void testLongDistanceNoWaitingDay() {
        assertEquals(156500, TaxiFareCalculator.calculateFare(20, 0, false), 0.1);
    }

    @Test
    public void testLongDistanceNoWaitingNight() {
        assertEquals(187800, TaxiFareCalculator.calculateFare(20, 0, true), 0.1);
    }

    @Test
    public void testLongDistanceSomeWaitingDay() {
        assertEquals(161500, TaxiFareCalculator.calculateFare(20, 5, false), 0.1);
    }

    @Test
    public void testLongDistanceSomeWaitingNight() {
        assertEquals(193800, TaxiFareCalculator.calculateFare(20, 5, true), 0.1);
    }
}
