#include "OrderService.h"
#include "Console.h"

#include <iostream>
#include <iomanip>
#include <vector>
#include <sstream>

using namespace std;

OrderService::OrderService() {
    orderRepository.initialize();
    customerRepository.initialize();
    menu.initialize();
}

void OrderService::placeOrder() {
    cout << "\n========== PLACE ORDER ==========\n";

    int customerId = Console::getInt("Enter your Customer ID: ");

    // Verify that the customer exists.
    vector<CustomerRecord> customers =
        customerRepository.loadCustomers();

    bool customerExists = false;

    for (const CustomerRecord& customer : customers) {
        if (customer.getCustomerId() == customerId) {
            customerExists = true;
            break;
        }
    }

    if (!customerExists) {
        cout << "\nCustomer ID not found. Please register first.\n";
        Console::pause();
        return;
    }

    // Refresh the menu before displaying it.
    menu.loadMenu();

    const vector<MenuItem>& menuItems = menu.getItems();

    if (menuItems.empty()) {
        cout << "\nThere are currently no menu items available.\n";
        Console::pause();
        return;
    }

    vector<OrderItem> selectedItems;

    int totalQuantity = 0;
    int itemNumber;

    while (totalQuantity < 5) {
        cout << "\n========== AVAILABLE MENU ==========\n";
        menu.displayMenu();

        cout << "\nItems selected: " << totalQuantity << "/5\n";
        cout << "Enter 0 when you are finished selecting items.\n";

        itemNumber = Console::getInt("Select item number: ");

        if (itemNumber == 0) {
            break;
        }

        if (itemNumber < 1 ||
            itemNumber > static_cast<int>(menuItems.size())) {

            cout << "\nInvalid menu item number.\n";
            continue;
        }

        int remaining = 5 - totalQuantity;

        int quantity = Console::getInt(
            "Enter quantity (1-" + to_string(remaining) + "): "
        );

        if (quantity < 1 || quantity > remaining) {
            cout << "\nInvalid quantity. You can only order "
                 << remaining << " more item(s).\n";
            continue;
        }

        const MenuItem& selectedMenuItem = menuItems[itemNumber - 1];

        selectedItems.emplace_back(
            selectedMenuItem.getName(),
            selectedMenuItem.getPrice(),
            quantity
        );

        totalQuantity += quantity;

        cout << "\nAdded: " << selectedMenuItem.getName()
             << " x" << quantity << "\n";
    }

    if (selectedItems.empty()) {
        cout << "\nNo items were selected. Order was not created.\n";
        Console::pause();
        return;
    }

    // Calculate the total.
    double total = OrderCalculator::calculateTotal(selectedItems);

    // Build the item summary for file storage.
    stringstream summaryStream;

    for (size_t i = 0; i < selectedItems.size(); i++) {
        const OrderItem& item = selectedItems[i];

        if (i > 0) {
            summaryStream << "; ";
        }

        summaryStream << item.getName()
                      << " x" << item.getQuantity();
    }

    string summary = summaryStream.str();

    // Display order summary before saving.
    cout << "\n========== ORDER SUMMARY ==========\n";

    cout << fixed << setprecision(2);

    for (const OrderItem& item : selectedItems) {
        cout << item.getName()
             << " x" << item.getQuantity()
             << " = PHP "
             << item.getSubtotal() << "\n";
    }

    cout << "-----------------------------------\n";
    cout << "Total: PHP " << total << "\n";

    // Load existing orders.
    vector<Order> orders = orderRepository.loadOrders();

    // Cancel the customer's previous pending order, if one exists.
    for (Order& order : orders) {
        if (order.getCustomerId() == customerId &&
            order.getStatus() == "PENDING") {

            order.setStatus("CANCELLED");
        }
    }

    // Generate a new order ID.
    string orderId = orderRepository.generateNextId(orders);

    // Create the new pending order.
    Order newOrder(
        orderId,
        customerId,
        "PENDING",
        total,
        0.0,
        0.0,
        summary
    );

    orders.push_back(newOrder);

    if (orderRepository.saveOrders(orders)) {
        cout << "\nOrder successfully saved!\n";
        cout << "Order ID: " << orderId << "\n";
        cout << "Status: PENDING\n";
        cout << "Your order is waiting for checkout.\n";
    }
    else {
        cout << "\nError saving order.\n";
    }

    Console::pause();
}

void OrderService::checkout() {
    cout << "\n========== CUSTOMER CHECKOUT ==========\n";

    int customerId = Console::getInt("Enter your Customer ID: ");

    vector<Order> orders = orderRepository.loadOrders();

    int pendingIndex = -1;

    // Find this customer's pending order.
    for (size_t i = 0; i < orders.size(); i++) {
        if (orders[i].getCustomerId() == customerId &&
            orders[i].getStatus() == "PENDING") {

            pendingIndex = static_cast<int>(i);
            break;
        }
    }

    if (pendingIndex == -1) {
        cout << "\nNo pending order was found for this customer.\n";
        Console::pause();
        return;
    }

    Order& order = orders[pendingIndex];

    // Display the pending order.
    cout << "\n========== ORDER DETAILS ==========\n";
    cout << "Order ID: " << order.getOrderId() << "\n";
    cout << "Customer ID: " << order.getCustomerId() << "\n";
    cout << "Items: " << order.getSummary() << "\n";
    cout << fixed << setprecision(2);
    cout << "Total: PHP " << order.getTotal() << "\n";

    // Process payment.
    double payment = 0.0;
    double change = 0.0;

    bool paymentSuccessful = billingService.processPayment(
        order.getTotal(),
        payment,
        change
    );

    if (!paymentSuccessful) {
        Console::pause();
        return;
    }

    // Update the order.
    order.setStatus("PAID");
    order.setPayment(payment);
    order.setChange(change);

    // Save the updated order.
    if (!orderRepository.saveOrders(orders)) {
        cout << "\nError updating order payment information.\n";
        Console::pause();
        return;
    }

    cout << "\nOrder successfully paid!\n";
    cout << "Order Status: PAID\n";

    // Generate receipt.
    receiptRenderer.generateReceipt(order);

    Console::pause();
}

void OrderService::viewAllOrders() const {
    cout << "\n========== ALL CUSTOMER ORDERS ==========\n";

    vector<Order> orders = orderRepository.loadOrders();

    if (orders.empty()) {
        cout << "\nNo orders found.\n";
        Console::pause();
        return;
    }

    cout << fixed << setprecision(2);

    for (const Order& order : orders) {
        cout << "\n----------------------------------------\n";
        cout << "Order ID:    " << order.getOrderId() << "\n";
        cout << "Customer ID: " << order.getCustomerId() << "\n";
        cout << "Status:      " << order.getStatus() << "\n";
        cout << "Items:       " << order.getSummary() << "\n";
        cout << "Total:       PHP " << order.getTotal() << "\n";
        cout << "Payment:     PHP " << order.getPayment() << "\n";
        cout << "Change:      PHP " << order.getChange() << "\n";
    }

    cout << "\n----------------------------------------\n";
    cout << "Total orders: " << orders.size() << "\n";

    Console::pause();
}
