package taxifarecalculator;

public class TaxiFareCalculator {

    public static double calculateFare(double distance, int waitingTime, boolean isNight) {
        if (distance < 0) {
            throw new IllegalArgumentException("Distance must be non-negative.");
        }
        if (waitingTime < 0) {
            throw new IllegalArgumentException("Waiting time must be non-negative.");
        }

        double fare = 0;

        if (distance == 0) {
            return fare;
        }

        if (distance <= 1) {
            fare = 10000;
        } else if (distance <= 10) {
            fare = 10000 + (distance - 1) * 8500;
        } else {
            fare = 10000 + 9 * 8500 + (distance - 10) * 7000;
        }

        int waitingCharge = (waitingTime / 5) * 5000;
        fare += waitingCharge;

        if (isNight) {
            fare *= 1.2;
        }

        return fare;
    }

}
