/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Javacore;

/**
 *
 * @author DELL
 */
public class CarInsuranceCalculator {
    public static double calculatePremium(double basePremium, int age, int carAge, boolean isLuxuryCar, boolean hasAccidentHistory, int annualMileage) {
        if (age < 18) {
            throw new IllegalArgumentException("The vehicle owner is not old enough to purchase insurance.");
        }
        if (carAge > 15) {
            throw new IllegalArgumentException("The vehicle is too old to be insured.");
        }
        if (basePremium < 0) {
            throw new IllegalArgumentException("The insurance fee is not valid.");
        }

        double premium = basePremium;

        if (isLuxuryCar) {
            premium *= 1.2; 
        }
        if (hasAccidentHistory) {
            premium *= 1.3; 
        }
        if (age < 25) {
            premium *= 1.15; 
        }
        if (annualMileage > 20000) {
            premium *= 1.1; 
        }

        return Math.max(premium, 500.0);
    }
}

