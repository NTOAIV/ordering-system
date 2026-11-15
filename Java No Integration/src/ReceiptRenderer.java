package ordering;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/** Receipt output and the admin's read-only order/reservation views. */
public final class ReceiptRenderer {
    private ReceiptRenderer() {
    }

    public static void printCustomerReceipt(Order order, CustomerRecord customer) {
        System.out.println();
        System.out.print(receiptText(order, customer));
    }

    public static void writeCustomerReceipt(Order order, CustomerRecord customer) {
        String fileName = "Customer Receipt " + order.customerId() + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(receiptText(order, customer));
            System.out.println("Receipt saved to " + fileName + ".");
        } catch (IOException exception) {
            System.out.println("Could not write the receipt file: " + exception.getMessage());
        }
    }

    public static void printOrders(List<Order> orders, CustomerRepository customers) {
        System.out.println("\nORDERS");
        if (orders.isEmpty()) {
            System.out.println("No orders have been placed yet.");
            return;
        }

        System.out.printf("%-12s %-28s %-10s %-14s %s%n",
                "Order ID", "Customer", "Status", "Total", "Items");
        System.out.println("--------------------------------------------------------------------------------");
        for (Order order : orders) {
            CustomerRecord customer = customers.findById(order.customerId());
            String name = customer == null
                    ? "ID " + order.customerId()
                    : customer.name() + " (" + order.customerId() + ")";
            System.out.printf("%-12s %-28s %-10s PHP %-9.2f %s%n",
                    order.orderId(), name, order.status(), order.total(), order.summary());
        }
    }

    public static void printReservations(List<Reservation> reservations, CustomerRepository customers) {
        System.out.println("\nRESERVATIONS");
        if (reservations.isEmpty()) {
            System.out.println("No reservations have been set yet.");
            return;
        }

        System.out.printf("%-6s %-28s %-12s %-14s %s%n",
                "ID", "Customer", "Date", "Time", "Guests");
        System.out.println("--------------------------------------------------------------------------------");
        for (Reservation reservation : reservations) {
            CustomerRecord customer = customers.findById(reservation.customerId());
            String name = customer == null ? "(unknown account)" : customer.name();
            System.out.printf("%-6d %-28s %-12s %-14s %d%n",
                    reservation.customerId(), name, reservation.date(),
                    reservation.time(), reservation.guests());
        }
    }

    private static String receiptText(Order order, CustomerRecord customer) {
        StringBuilder text = new StringBuilder();
        text.append("=================================\n");
        text.append("        OFFICIAL RECEIPT\n");
        text.append("=================================\n");
        text.append(" Order ID:   ").append(order.orderId()).append('\n');
        text.append(" Customer:   ").append(customerName(customer, order.customerId())).append('\n');
        text.append("---------------------------------\n");

        if (order.items().isEmpty()) {
            text.append(" Items:      ").append(order.summary()).append('\n');
        } else {
            for (OrderItem item : order.items()) {
                text.append(String.format(Locale.US, " %s x%d -> PHP %.2f\n",
                        item.name(), item.quantity(), item.lineTotal()));
            }
        }

        text.append("---------------------------------\n");
        text.append(String.format(Locale.US, " Subtotal:   PHP %.2f\n", order.subtotal()));
        text.append(String.format(Locale.US, " Total:      PHP %.2f\n", order.total()));
        text.append(String.format(Locale.US, " Payment:    PHP %.2f\n", order.payment()));
        text.append(String.format(Locale.US, " Change:     PHP %.2f\n", order.change()));
        text.append("=================================\n");
        text.append(" Thank you for ordering!\n");
        return text.toString();
    }

    private static String customerName(CustomerRecord customer, int id) {
        return customer == null ? "ID " + id : customer.name() + " (ID " + id + ")";
    }
}
