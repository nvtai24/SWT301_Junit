package demo;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Nvtai
 */
public class CalculatorTest {

    Calculator cal = new Calculator();

    @Test
    public void testAdd() {
        int rs = cal.add(1, 2);
        int exp = 3;
        assertEquals(exp, rs);
    }

    @Test
    public void testSub() {
        int rs = cal.sub(10, 1);
        int exp = 9;
        assertEquals(exp, rs);
    }

    @Test
    public void testMul() {
        int rs = cal.mul(10, 10);
        int exp = 100;
        assertEquals(exp, rs);
    }

    @Test
    public void testDiv() {
        int rs = cal.div(36, 2);
        int exp = 18;
        assertEquals(exp, rs);
    }

}
