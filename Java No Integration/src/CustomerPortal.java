package ordering;

/** Customer portal with only the five customer actions (plus exit). */
public final class CustomerPortal {
    private final Console console;
    private final Menu menu;
    private final CustomerRepository customers;
    private final ReservationRepository reservations;
    private final OrderRepository orders;
    private final CustomerRecordService customerService;
    private final ReservationService reservationService;
    private final OrderService orderService;
    private final BillingService billingService;

    private int currentCustomerId = -1;
    private Order pendingOrder;

    public CustomerPortal(Console console, Menu menu, CustomerRepository customers,
                          ReservationRepository reservations, OrderRepository orders) {
        this.console = console;
        this.menu = menu;
        this.customers = customers;
        this.reservations = reservations;
        this.orders = orders;
        this.customerService = new CustomerRecordService(console, customers);
        this.reservationService = new ReservationService(console, reservations);
        this.orderService = new OrderService(console, menu, orders);
        this.billingService = new BillingService(console, customers, orders);
    }

    public void run() {
        while (true) {
            printPortal();
            char choice = console.readChoice();
            switch (choice) {
                case '1':
                    register();
                    break;
                case '2':
                    updateAccount();
                    break;
                case '3':
                    placeOrder();
                    break;
                case '4':
                    setReservation();
                    break;
                case '5':
                    checkOut();
                    break;
                case 'X':
                    exit();
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void printPortal() {
        System.out.println("\nCUSTOMER PORTAL");
        System.out.println("[1] Register Account");
        System.out.println("[2] Update Account Details");
        System.out.println("[3] View Menu & Place Order");
        System.out.println("[4] Set Reservation");
        System.out.println("[5] Check Out");
        System.out.println("[X] Exit System");
        if (currentCustomerId > 0) {
            CustomerRecord record = customers.findById(currentCustomerId);
            if (record != null) {
                System.out.println("Logged in as: " + record.name() + " (ID " + currentCustomerId + ")");
            }
        }
    }

    private void register() {
        int id = customerService.register();
        if (id > 0) {
            currentCustomerId = id;
        }
    }

    private void updateAccount() {
        int id = customerService.update(currentCustomerId);
        if (id > 0) {
            currentCustomerId = id;
        }
    }

    private void placeOrder() {
        int id = requireCustomer();
        if (id < 0) {
            return;
        }
        Order order = orderService.placeOrder(id);
        if (order != null) {
            pendingOrder = order;
        }
    }

    private void setReservation() {
        int id = requireCustomer();
        if (id < 0) {
            return;
        }
        reservationService.setReservation(id);
    }

    private void checkOut() {
        int id = requireCustomer();
        if (id < 0) {
            return;
        }

        Order order = pendingOrder;
        if (order == null || order.customerId() != id || !order.isPending()) {
            order = orders.findLatestOpenByCustomer(id);
        }
        if (order == null) {
            System.out.println("\nYou have no pending order to check out. Returning to the main menu.");
            return;
        }

        if (billingService.checkout(order)) {
            pendingOrder = null;
        }
    }

    private int requireCustomer() {
        if (currentCustomerId > 0 && customers.exists(currentCustomerId)) {
            return currentCustomerId;
        }
        int id = customerService.promptExistingId();
        if (id > 0) {
            currentCustomerId = id;
        }
        return id;
    }

    private void exit() {
        if (pendingOrder != null && pendingOrder.isPending()) {
            System.out.println("You still have unpaid order " + pendingOrder.orderId()
                    + ". It stays visible to the admin until you check out.");
        }
        System.out.println("Thank you for using the ordering system.");
    }
}
