/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package Javacore;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author DELL
 */
import static org.junit.Assert.*;
import org.junit.Test;

public class CarInsuranceCalculatorTest {

    @Test
    public void testBasePremiumOnly() {
        assertEquals(1000.0, CarInsuranceCalculator.calculatePremium(1000, 30, 5, false, false, 15000), 0.001);
    }

    @Test
    public void testLuxuryCar() {
        assertEquals(1200.0, CarInsuranceCalculator.calculatePremium(1000, 30, 5, true, false, 15000), 0.001);
    }

    @Test
    public void testAccidentHistory() {
        assertEquals(1300.0, CarInsuranceCalculator.calculatePremium(1000, 30, 5, false, true, 15000), 0.001);
    }

    @Test
    public void testYoungDriver() {
        assertEquals(1150.0, CarInsuranceCalculator.calculatePremium(1000, 22, 5, false, false, 15000), 0.001);
    }

    @Test
    public void testHighAnnualMileage() {
        assertEquals(1100.0, CarInsuranceCalculator.calculatePremium(1000, 30, 5, false, false, 25000), 0.001);
    }

    @Test
    public void testMinimumPremiumEnforcement() {
        assertEquals(500.0, CarInsuranceCalculator.calculatePremium(400, 30, 5, false, false, 15000), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnderageDriver() {
        CarInsuranceCalculator.calculatePremium(1000, 16, 5, false, false, 15000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOldCar() {
        CarInsuranceCalculator.calculatePremium(1000, 30, 16, false, false, 15000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeBasePremium() {
        CarInsuranceCalculator.calculatePremium(-1000, 30, 5, false, false, 15000);
    }

    @Test
    public void testMultipleRiskFactors() {
        assertEquals(1973.3999999999, CarInsuranceCalculator.calculatePremium(1000, 23, 5, true, true, 25000), 0.001);
    }
}
