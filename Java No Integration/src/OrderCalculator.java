package ordering;

import java.util.List;

/** Shared money calculations. */
public final class OrderCalculator {
    private OrderCalculator() {
    }

    public static double subtotal(List<OrderItem> items) {
        double total = 0.0;
        for (OrderItem item : items) {
            total += item.lineTotal();
        }
        return round2(total);
    }

    public static double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
