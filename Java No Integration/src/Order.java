package ordering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * One customer order and its status lifecycle:
 * PENDING -> PAID (or CANCELLED when the customer replaces it with a new order).
 */
public final class Order {
    public static final String PENDING = "PENDING";
    public static final String PAID = "PAID";
    public static final String CANCELLED = "CANCELLED";

    private final String orderId;
    private final int customerId;
    private final List<OrderItem> items;
    private final String summary;
    private final double subtotal;
    private final double total;
    private double payment;
    private double change;
    private String status;

    public Order(String orderId, int customerId, List<OrderItem> items) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.items = new ArrayList<>(items);
        this.summary = buildSummary(items);
        this.subtotal = OrderCalculator.subtotal(items);
        this.total = subtotal;
        this.payment = 0.0;
        this.change = 0.0;
        this.status = PENDING;
    }

    private Order(String orderId, int customerId, List<OrderItem> items, String summary,
                  double subtotal, double total, double payment, double change, String status) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.items = new ArrayList<>(items);
        this.summary = summary;
        this.subtotal = subtotal;
        this.total = total;
        this.payment = payment;
        this.change = change;
        this.status = status;
    }

    /** Rebuilds an order from orders.txt. Item details are stored only as a summary. */
    public static Order restore(String orderId, int customerId, String summary,
                                double total, double payment, double change, String status) {
        return new Order(orderId, customerId, new ArrayList<>(), summary,
                total, total, payment, change, status);
    }

    public String orderId() {
        return orderId;
    }

    public int customerId() {
        return customerId;
    }

    public List<OrderItem> items() {
        return Collections.unmodifiableList(items);
    }

    public String summary() {
        return summary;
    }

    public double subtotal() {
        return subtotal;
    }

    public double total() {
        return total;
    }

    public double payment() {
        return payment;
    }

    public double change() {
        return change;
    }

    public String status() {
        return status;
    }

    public boolean isPending() {
        return PENDING.equals(status);
    }

    public boolean isPaid() {
        return PAID.equals(status);
    }

    public boolean hasItems() {
        return !summary.isEmpty();
    }

    public void markPaid(double amount) {
        this.payment = OrderCalculator.round2(amount);
        this.change = OrderCalculator.round2(amount - total);
        this.status = PAID;
    }

    public void markCancelled() {
        if (isPending()) {
            this.status = CANCELLED;
        }
    }

    private static String buildSummary(List<OrderItem> items) {
        StringBuilder builder = new StringBuilder();
        for (OrderItem item : items) {
            if (builder.length() > 0) {
                builder.append("; ");
            }
            builder.append(item.name()).append(" x").append(item.quantity());
        }
        return builder.toString();
    }
}
