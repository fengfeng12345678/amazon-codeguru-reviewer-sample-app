import java.util.ArrayList;
import java.util.List;

public class ComplexCode {

    public static void main(String[] args) {
        ComplexCode complexCode = new ComplexCode();
        List<String> names = new ArrayList<>();
        names.add("Alice");
        names.add("Bob");
        names.add("Charlie");

        // Call a method with a potential bug
        complexCode.printNames(names);

        // Call a method with a potential bug
        complexCode.processNames(names);
    }

    // Method to print names from a list
    public void printNames(List<String> names) {
        for (int i = 0; i <= names.size(); i++) { // Bug: Index out of bounds
            System.out.println(names.get(i));
        }
    }

    // Method to process names and add suffix
    public void processNames(List<String> names) {
        for (String name : names) {
            if (name.equals("Alice")) {
                name += " Doe";
            } else if (name.equals("Bob")) {
                name += " Smith";
            } else {
                name += " Unknown"; // Bug: Incorrect logic
            }
            System.out.println(name);
        }
    }

    // Method to calculate factorial (intentionally complex)
    public int calculateFactorial(int n) {
        if (n == 0) {
            return 1;
        } else {
            return n * calculateFactorial(n - 1);
        }
    }

    // Method to perform a complex calculation (intentionally complex)
    public double complexCalculation(double a, double b, int n) {
        double result = 0.0;
        for (int i = 0; i < n; i++) {
            result += Math.pow(a, i) * Math.pow(b, n - i) / calculateFactorial(i); // Bug: Potential arithmetic error
        }
        return result;
    }
}