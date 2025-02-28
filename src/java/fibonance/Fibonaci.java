package fibonance;

public class Fibonaci {

    private Fibonaci() {
        throw new IllegalStateException("Utility class");
    }

    public static int fibonacci(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Input must be non-negative");
        }
        if (n > 46) {
            throw new ArithmeticException("Result exceeds maximum int value");
        }

        if (n <= 1) {
            return n;
        }
        int a = 0, b = 1;
        for (int i = 2; i <= n; i++) {
            int temp = b;
            b = Math.addExact(a, b);
            a = temp;
        }
        return b;
    }
}
