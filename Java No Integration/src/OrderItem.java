package ordering;

/** One line item inside an order. */
public final class OrderItem {
    private final String name;
    private final int quantity;
    private final double price;

    public OrderItem(String name, int quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String name() {
        return name;
    }

    public int quantity() {
        return quantity;
    }

    public double price() {
        return price;
    }

    public double lineTotal() {
        return price * quantity;
    }
}
