package ordering;

import java.util.Scanner;

/** Entry point for the standalone, single-language Java ordering system. */
public final class Main {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Console CONSOLE = new Console(SCANNER);

    private Main() {
    }

    public static void main(String[] args) {
        try {
            run();
        } catch (Console.EndOfInputException exception) {
            System.out.println("\nInput stream closed. Exiting.");
        } finally {
            SCANNER.close();
        }
    }

    private static void run() {
        Menu menu = Menu.load();
        CustomerRepository customers = new CustomerRepository();
        ReservationRepository reservations = new ReservationRepository();
        OrderRepository orders = new OrderRepository();

        String role = selectRole();
        System.out.println("Logged in as: " + ("ADMIN".equals(role) ? "Admin" : "Customer") + "\n");

        if ("ADMIN".equals(role)) {
            new AdminService(CONSOLE, menu, customers, reservations, orders).run();
        } else {
            new CustomerPortal(CONSOLE, menu, customers, reservations, orders).run();
        }
    }

    private static String selectRole() {
        System.out.println("SELECT USER TYPE");
        System.out.println("[1] Customer");
        System.out.println("[2] Admin");
        while (true) {
            System.out.print("Choice: ");
            String choice = CONSOLE.readLine();
            if (choice.equals("1")) {
                return "CUSTOMER";
            }
            if (choice.equals("2")) {
                return "ADMIN";
            }
            System.out.println("Please enter [1] for Customer or [2] for Admin.");
        }
    }
}
