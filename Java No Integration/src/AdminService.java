package ordering;

import java.util.List;
import java.util.Locale;

/**
 * Admin portal. The admin can view orders, reservations, and customer accounts,
 * and can manage the menu. The admin cannot order or edit customer data.
 */
public final class AdminService {
    private final Console console;
    private final Menu menu;
    private final CustomerRepository customers;
    private final ReservationRepository reservations;
    private final OrderRepository orders;
    private final CustomerRecordService customerService;

    public AdminService(Console console, Menu menu, CustomerRepository customers,
                        ReservationRepository reservations, OrderRepository orders) {
        this.console = console;
        this.menu = menu;
        this.customers = customers;
        this.reservations = reservations;
        this.orders = orders;
        this.customerService = new CustomerRecordService(console, customers);
    }

    public void run() {
        while (true) {
            printPortal();
            char choice = console.readChoice();
            switch (choice) {
                case '1':
                    viewOrdersAndReservations();
                    break;
                case '2':
                    customerService.viewAll();
                    break;
                case '3':
                    manageMenu();
                    break;
                case 'X':
                    System.out.println("Admin session ended.");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void printPortal() {
        System.out.println("\nADMIN PORTAL");
        System.out.println("[1] View Orders & Reservations");
        System.out.println("[2] View Customer Accounts");
        System.out.println("[3] Manage Menu");
        System.out.println("[X] Exit System");
    }

    private void viewOrdersAndReservations() {
        ReceiptRenderer.printOrders(orders.findAll(), customers);
        ReceiptRenderer.printReservations(reservations.findAll(), customers);
    }

    private void manageMenu() {
        while (true) {
            System.out.println("\nMENU MANAGEMENT");
            System.out.println("[1] View Menu");
            System.out.println("[2] Add Menu Item");
            System.out.println("[3] Update Menu Item");
            System.out.println("[4] Delete Menu Item");
            System.out.println("[X] Back to Admin Portal");

            char choice = console.readChoice();
            switch (choice) {
                case '1':
                    viewMenu();
                    break;
                case '2':
                    addItem();
                    break;
                case '3':
                    updateItem();
                    break;
                case '4':
                    deleteItem();
                    break;
                case 'X':
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void viewMenu() {
        for (String category : Menu.CATEGORIES) {
            System.out.println("\n" + Menu.label(category).toUpperCase(Locale.US));
            List<MenuItem> items = menu.items(category);
            if (items.isEmpty()) {
                System.out.println("  (no items)");
                continue;
            }
            for (int i = 0; i < items.size(); i++) {
                MenuItem item = items.get(i);
                System.out.printf(Locale.US, "  [%d] %-52s PHP %.2f%n",
                        i + 1, item.name(), item.price());
            }
        }
    }

    private void addItem() {
        String category = chooseCategory();
        if (category == null) {
            return;
        }

        String name = readMenuName();
        double price = console.readPrice("Price: PHP ");
        menu.add(category, name, price);
        menu.save();
        System.out.println("Added " + name + " to " + Menu.label(category) + ".");
    }

    private void updateItem() {
        String category = chooseCategory();
        if (category == null) {
            return;
        }

        List<MenuItem> items = menu.items(category);
        if (items.isEmpty()) {
            System.out.println("There are no items in " + Menu.label(category) + " to update.");
            return;
        }

        printCategory(category);
        int number = console.readInt("Item number to update (0 to go back): ");
        if (number == 0) {
            return;
        }
        if (number < 1 || number > items.size()) {
            System.out.println("Invalid item number.");
            return;
        }

        MenuItem current = items.get(number - 1);
        String name = console.readTextOrDefault("New name", current.name());
        if (!Menu.isValidName(name)) {
            System.out.println("The item name cannot be empty or contain '|'.");
            return;
        }
        double price = console.readPriceOrDefault("New price (PHP)", current.price());

        menu.update(category, number - 1, name, price);
        menu.save();
        System.out.println("Updated " + Menu.label(category) + " item " + number + ".");
    }

    private void deleteItem() {
        String category = chooseCategory();
        if (category == null) {
            return;
        }

        List<MenuItem> items = menu.items(category);
        if (items.isEmpty()) {
            System.out.println("There are no items in " + Menu.label(category) + " to delete.");
            return;
        }

        printCategory(category);
        int number = console.readInt("Item number to delete (0 to go back): ");
        if (number == 0) {
            return;
        }
        if (number < 1 || number > items.size()) {
            System.out.println("Invalid item number.");
            return;
        }

        MenuItem current = items.get(number - 1);
        System.out.print("Delete " + current.name() + "? [Y/N]: ");
        char confirmation = console.readChoice();
        if (confirmation != 'Y') {
            System.out.println("Delete cancelled.");
            return;
        }

        menu.remove(category, number - 1);
        menu.save();
        System.out.println("Deleted " + current.name() + " from " + Menu.label(category) + ".");
    }

    private String chooseCategory() {
        System.out.println("\nChoose a category:");
        System.out.println("[A] " + Menu.label(Menu.MEALS));
        System.out.println("[B] " + Menu.label(Menu.DRINKS));
        System.out.println("[C] " + Menu.label(Menu.DESSERTS));
        System.out.println("[D] " + Menu.label(Menu.SIDES));
        System.out.println("[E] " + Menu.label(Menu.BUNDLES));
        System.out.println("[X] Back");

        char choice = console.readChoice();
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
            case 'X':
                return null;
            default:
                System.out.println("Invalid category.");
                return null;
        }
    }

    private void printCategory(String category) {
        System.out.println("\n" + Menu.label(category).toUpperCase(Locale.US));
        List<MenuItem> items = menu.items(category);
        for (int i = 0; i < items.size(); i++) {
            MenuItem item = items.get(i);
            System.out.printf(Locale.US, "  [%d] %-52s PHP %.2f%n",
                    i + 1, item.name(), item.price());
        }
    }

    private String readMenuName() {
        while (true) {
            String name = console.readText("Item name: ");
            if (Menu.isValidName(name)) {
                return name;
            }
            System.out.println("The item name cannot be empty or contain '|'.");
        }
    }
}
