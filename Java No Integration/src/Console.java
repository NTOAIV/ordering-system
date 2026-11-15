package ordering;

import java.util.Locale;
import java.util.Scanner;

/** Shared console input helpers used by every module. */
public final class Console {
    private final Scanner scanner;

    public Console(Scanner scanner) {
        this.scanner = scanner;
    }

    public String readLine() {
        if (!scanner.hasNextLine()) {
            throw new EndOfInputException();
        }
        return scanner.nextLine().trim();
    }

    public char readChoice() {
        while (true) {
            String line = readLine();
            if (!line.isEmpty()) {
                return Character.toUpperCase(line.charAt(0));
            }
            System.out.print("Please enter a choice: ");
        }
    }

    public int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = readLine();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    public int readPositiveInt(String prompt) {
        while (true) {
            int value = readInt(prompt);
            if (value > 0) {
                return value;
            }
            System.out.println("Please enter a positive whole number.");
        }
    }

    public double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = readLine();
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a valid amount.");
            }
        }
    }

    public double readPrice(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = readLine();
            try {
                double value = Double.parseDouble(line);
                if (Double.isFinite(value) && value > 0) {
                    return OrderCalculator.round2(value);
                }
            } catch (NumberFormatException ignored) {
                // Fall through to the error message.
            }
            System.out.println("Please enter a positive price.");
        }
    }

    public double readPriceOrDefault(String prompt, double current) {
        while (true) {
            System.out.printf(Locale.US, "%s [%.2f]: ", prompt, current);
            String line = readLine();
            if (line.isEmpty()) {
                return current;
            }
            try {
                double value = Double.parseDouble(line);
                if (Double.isFinite(value) && value > 0) {
                    return OrderCalculator.round2(value);
                }
            } catch (NumberFormatException ignored) {
                // Fall through to the error message.
            }
            System.out.println("Please enter a positive price, or leave it blank to keep the current price.");
        }
    }

    public String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = readLine();
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("This field cannot be empty.");
        }
    }

    public String readTextOrDefault(String prompt, String current) {
        System.out.print(prompt + " [" + current + "]: ");
        String line = readLine();
        return line.isEmpty() ? current : line;
    }

    /** Raised when the input stream closes unexpectedly. */
    public static final class EndOfInputException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        private EndOfInputException() {
            super("End of input.");
        }
    }
}
