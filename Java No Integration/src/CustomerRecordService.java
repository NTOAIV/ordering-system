package ordering;

import java.util.List;

/** Customer-facing account actions, plus the admin's read-only account view. */
public final class CustomerRecordService {
    private final Console console;
    private final CustomerRepository customers;

    public CustomerRecordService(Console console, CustomerRepository customers) {
        this.console = console;
        this.customers = customers;
    }

    public int register() {
        System.out.println("\nREGISTER ACCOUNT");
        int id;
        while (true) {
            id = console.readInt("Customer ID (0 to cancel): ");
            if (id == 0) {
                System.out.println("Registration cancelled.");
                return -1;
            }
            if (id < 0) {
                System.out.println("Customer ID must be a positive number.");
                continue;
            }
            if (customers.exists(id)) {
                System.out.println("That customer ID is already registered.");
                continue;
            }
            break;
        }

        String name = readField("Full name: ");
        String contact = readField("Contact number: ");
        customers.add(new CustomerRecord(id, name, contact));
        System.out.println("Account created for " + name + " (ID " + id + ").");
        return id;
    }

    public int update(int currentId) {
        System.out.println("\nUPDATE ACCOUNT DETAILS");
        int id = currentId;
        if (id <= 0) {
            id = promptExistingId();
            if (id < 0) {
                return currentId;
            }
        }

        CustomerRecord record = customers.findById(id);
        if (record == null) {
            System.out.println("No customer account found with ID " + id + ".");
            return currentId;
        }

        System.out.println("Current details: ID " + id + " | " + record.name()
                + " | " + record.contact());
        System.out.println("Leave a field blank to keep its current value.");
        String name = console.readTextOrDefault("Full name", record.name());
        String contact = console.readTextOrDefault("Contact number", record.contact());
        if (name.contains("|") || contact.contains("|")) {
            System.out.println("Account fields cannot contain the '|' character.");
            return currentId;
        }

        customers.update(new CustomerRecord(id, name, contact));
        System.out.println("Account details updated for ID " + id + ".");
        return id;
    }

    public void viewAll() {
        List<CustomerRecord> all = customers.findAll();
        System.out.println("\nCUSTOMER ACCOUNTS");
        if (all.isEmpty()) {
            System.out.println("No customer accounts have been registered yet.");
            return;
        }
        System.out.printf("%-6s %-30s %s%n", "ID", "Name", "Contact");
        System.out.println("--------------------------------------------------");
        for (CustomerRecord record : all) {
            System.out.printf("%-6d %-30s %s%n", record.id(), record.name(), record.contact());
        }
    }

    public int promptExistingId() {
        if (customers.findAll().isEmpty()) {
            System.out.println("No customer accounts exist yet. Please register an account first.");
            return -1;
        }
        while (true) {
            int id = console.readInt("Customer ID (0 to return to the main menu): ");
            if (id == 0) {
                return -1;
            }
            if (customers.exists(id)) {
                return id;
            }
            System.out.println("No account found with ID " + id + ".");
        }
    }

    private String readField(String prompt) {
        while (true) {
            String value = console.readText(prompt);
            if (!value.contains("|")) {
                return value;
            }
            System.out.println("This field cannot contain the '|' character.");
        }
    }
}
