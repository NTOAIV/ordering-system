package ordering;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Order storage in orders.txt. The admin's "View Orders & Reservations"
 * screen reads this file, so every order placed is visible to the admin.
 *
 * File format: orderId|customerId|status|total|payment|change|summary
 */
public final class OrderRepository {
    public static final String FILE = "orders.txt";

    private final List<Order> orders = new ArrayList<>();

    public OrderRepository() {
        load();
    }

    public List<Order> findAll() {
        return Collections.unmodifiableList(orders);
    }

    public Order findLatestOpenByCustomer(int customerId) {
        for (int i = orders.size() - 1; i >= 0; i--) {
            Order order = orders.get(i);
            if (order.customerId() == customerId && order.isPending()) {
                return order;
            }
        }
        return null;
    }

    /**
     * Adds a new order. Any older pending order for the same customer is
     * cancelled so the customer only has one open order at a time.
     */
    public void add(Order order) {
        for (Order existing : orders) {
            if (existing.customerId() == order.customerId() && existing.isPending()) {
                existing.markCancelled();
            }
        }
        orders.add(order);
        saveAll();
    }

    public void save(Order order) {
        int index = indexOf(order.orderId());
        if (index >= 0) {
            orders.set(index, order);
        } else {
            orders.add(order);
        }
        saveAll();
    }

    public String nextOrderId() {
        int highest = 1000;
        for (Order order : orders) {
            String id = order.orderId();
            if (!id.startsWith("TXN-")) {
                continue;
            }
            try {
                highest = Math.max(highest, Integer.parseInt(id.substring(4)));
            } catch (NumberFormatException ignored) {
                // Non-numeric suffix; keep scanning.
            }
        }
        return "TXN-" + (highest + 1);
    }

    private int indexOf(String orderId) {
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).orderId().equals(orderId)) {
                return i;
            }
        }
        return -1;
    }

    private void load() {
        File file = new File(FILE);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }
                String[] parts = trimmed.split("\\|", -1);
                if (parts.length < 7) {
                    System.out.println("[Orders] Skipping malformed line: " + trimmed);
                    continue;
                }
                int customerId;
                double total;
                double payment;
                double change;
                try {
                    customerId = Integer.parseInt(parts[1].trim());
                    total = Double.parseDouble(parts[3].trim());
                    payment = Double.parseDouble(parts[4].trim());
                    change = Double.parseDouble(parts[5].trim());
                } catch (NumberFormatException exception) {
                    System.out.println("[Orders] Skipping invalid numbers: " + trimmed);
                    continue;
                }
                String orderId = parts[0].trim();
                String status = parts[2].trim().toUpperCase(Locale.US);
                String summary = parts[6].trim();
                if (orderId.isEmpty() || customerId <= 0
                        || !(Order.PENDING.equals(status)
                        || Order.PAID.equals(status)
                        || Order.CANCELLED.equals(status))) {
                    System.out.println("[Orders] Skipping invalid order: " + trimmed);
                    continue;
                }
                orders.add(Order.restore(orderId, customerId, summary,
                        total, payment, change, status));
            }
        } catch (IOException exception) {
            System.out.println("[Orders] Could not read " + FILE + ": " + exception.getMessage());
        }
    }

    private void saveAll() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE))) {
            for (Order order : orders) {
                writer.write(String.format(Locale.US, "%s|%d|%s|%.2f|%.2f|%.2f|%s\n",
                        order.orderId(), order.customerId(), order.status(),
                        order.total(), order.payment(), order.change(), order.summary()));
            }
        } catch (IOException exception) {
            System.out.println("[Orders] Could not save " + FILE + ": " + exception.getMessage());
        }
    }
}
