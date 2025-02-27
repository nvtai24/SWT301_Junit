package demo;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Nvtai
 */
public class CalculatorService {
    
    private DatabaseService databaseService;

    public CalculatorService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }
    
    public int getStoredValue() {
        return databaseService.getValue();
    }
    
}
