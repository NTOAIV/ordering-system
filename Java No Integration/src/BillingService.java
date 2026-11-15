package ordering;

import java.util.Locale;

/**
 * Customer checkout. The customer can enter [X] at the payment prompt to
 * return to the main menu without paying.
 */
public final class BillingService {
    private final Console console;
    private final CustomerRepository customers;
    private final OrderRepository orders;

    public BillingService(Console console, CustomerRepository customers, OrderRepository orders) {
        this.console = console;
        this.customers = customers;
        this.orders = orders;
    }

    public boolean checkout(Order order) {
        if (order == null) {
            System.out.println("Nothing to check out. Returning to the main menu.");
            return false;
        }
        if (!order.isPending()) {
            System.out.println("This order is already " + order.status()
                    + ". Returning to the main menu.");
            return false;
        }

        double total = order.total();
        if (total <= 0) {
            System.out.println("This order has no amount due. Returning to the main menu.");
            return false;
        }

        CustomerRecord customer = customers.findById(order.customerId());
        System.out.println("\nCHECK OUT");
        System.out.println("Order ID: " + order.orderId());
        System.out.println("Customer: " + customerName(customer, order.customerId()));
        System.out.println("Items: " + order.summary());
        System.out.printf(Locale.US, "Total amount due: PHP %.2f%n", total);

        while (true) {
            System.out.print("Enter payment amount (or [X] to return to the main menu): PHP ");
            String line = console.readLine();
            if (line.equalsIgnoreCase("X")) {
                System.out.println("Checkout cancelled. Returning to the main menu.");
                return false;
            }

            double payment;
            try {
                payment = Double.parseDouble(line);
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a valid amount, or [X] to go back.");
                continue;
            }
            if (!Double.isFinite(payment) || payment <= 0) {
                System.out.println("Please enter a positive payment amount.");
                continue;
            }
            if (payment < total) {
                System.out.printf(Locale.US, "Insufficient payment. You still need PHP %.2f.%n",
                        total - payment);
                continue;
            }

            order.markPaid(payment);
            orders.save(order);
            ReceiptRenderer.writeCustomerReceipt(order, customer);
            ReceiptRenderer.printCustomerReceipt(order, customer);
            return true;
        }
    }

    private static String customerName(CustomerRecord customer, int id) {
        return customer == null ? "ID " + id : customer.name() + " (ID " + id + ")";
    }
}
