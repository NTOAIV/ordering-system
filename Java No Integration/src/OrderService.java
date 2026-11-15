package ordering;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Customer-facing menu browsing and order capture. */
public final class OrderService {
    private static final int MAX_ITEMS = 5;

    private final Console console;
    private final Menu menu;
    private final OrderRepository orders;

    public OrderService(Console console, Menu menu, OrderRepository orders) {
        this.console = console;
        this.menu = menu;
        this.orders = orders;
    }

    public Order placeOrder(int customerId) {
        List<OrderItem> items = new ArrayList<>();

        while (true) {
            printOrderMenu(items);
            char choice = console.readChoice();

            if (choice == 'X') {
                if (items.isEmpty()) {
                    System.out.println("No items selected. Returning to the main menu without creating an order.");
                    return null;
                }
                Order order = new Order(orders.nextOrderId(), customerId, items);
                orders.add(order);
                System.out.println("Order " + order.orderId() + " saved with status "
                        + order.status() + ". Choose [5] Check Out when you are ready to pay.");
                return order;
            }

            if (choice == 'V') {
                printCurrentOrder(items);
                continue;
            }

            String category = categoryFor(choice);
            if (category == null) {
                System.out.println("Invalid option.");
                continue;
            }
            addItem(category, items);
        }
    }

    private void printOrderMenu(List<OrderItem> items) {
        System.out.println("\nORDER MENU");
        System.out.println("[A] " + Menu.label(Menu.MEALS));
        System.out.println("[B] " + Menu.label(Menu.DRINKS));
        System.out.println("[C] " + Menu.label(Menu.DESSERTS));
        System.out.println("[D] " + Menu.label(Menu.SIDES));
        System.out.println("[E] " + Menu.label(Menu.BUNDLES));
        System.out.println("[V] View Current Order");
        System.out.println("[X] Finish & Save Order");
        System.out.println("Items in order: " + totalQuantity(items) + "/" + MAX_ITEMS + "\n");
    }

    private void addItem(String category, List<OrderItem> items) {
        List<MenuItem> available = menu.items(category);
        System.out.println("\n" + Menu.label(category).toUpperCase(Locale.US));
        if (available.isEmpty()) {
            System.out.println("No items are available in this category. Please ask the admin to add some.");
            return;
        }

        for (int i = 0; i < available.size(); i++) {
            MenuItem item = available.get(i);
            System.out.printf(Locale.US, "[%d] %-52s PHP %.2f%n",
                    i + 1, item.name(), item.price());
        }

        int choice = console.readInt("Choose an item (0 to go back): ");
        if (choice == 0) {
            return;
        }
        if (choice < 1 || choice > available.size()) {
            System.out.println("Invalid item number.");
            return;
        }

        MenuItem selected = available.get(choice - 1);
        int quantity = console.readPositiveInt("Quantity: ");
        if (totalQuantity(items) + quantity > MAX_ITEMS) {
            System.out.println("You can order at most " + MAX_ITEMS + " items in total.");
            return;
        }

        mergeOrAdd(items, selected, quantity);
        System.out.println("Added " + selected.name() + " x" + quantity + ".");
    }

    private void printCurrentOrder(List<OrderItem> items) {
        System.out.println("\nCURRENT ORDER");
        if (items.isEmpty()) {
            System.out.println("No items selected yet.");
            return;
        }
        for (OrderItem item : items) {
            System.out.printf(Locale.US, "  %s x%d -> PHP %.2f%n",
                    item.name(), item.quantity(), item.lineTotal());
        }
        System.out.printf(Locale.US, "  Subtotal: PHP %.2f%n", OrderCalculator.subtotal(items));
    }

    private static void mergeOrAdd(List<OrderItem> items, MenuItem selected, int quantity) {
        for (int i = 0; i < items.size(); i++) {
            OrderItem existing = items.get(i);
            if (existing.name().equals(selected.name())) {
                items.set(i, new OrderItem(existing.name(),
                        existing.quantity() + quantity, existing.price()));
                return;
            }
        }
        items.add(new OrderItem(selected.name(), quantity, selected.price()));
    }

    private static int totalQuantity(List<OrderItem> items) {
        int total = 0;
        for (OrderItem item : items) {
            total += item.quantity();
        }
        return total;
    }

    private static String categoryFor(char choice) {
        switch (choice) {
            case 'A':
                return Menu.MEALS;
            case 'B':
                return Menu.DRINKS;
            case 'C':
                return Menu.DESSERTS;
            case 'D':
                return Menu.SIDES;
            case 'E':
                return Menu.BUNDLES;
            default:
                return null;
        }
    }
}
