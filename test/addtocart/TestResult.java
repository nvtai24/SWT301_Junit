/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package addtocart;

/**
 *
 * @author Nvtai
 */
public class TestResult {
    String testCaseName;
    String status;
    String message;
    long executionTime;

    public TestResult(String testCaseName, String status, String message, long executionTime) {
        this.testCaseName = testCaseName;
        this.status = status;
        this.message = message;
        this.executionTime = executionTime;
    }
}

