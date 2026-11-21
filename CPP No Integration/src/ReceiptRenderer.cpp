#include "ReceiptRenderer.h"

#include <iostream>
#include <fstream>
#include <iomanip>
#include <filesystem>

using namespace std;

bool ReceiptRenderer::generateReceipt(const Order& order) const {
    filesystem::create_directories("receipts");

    string filePath =
        "receipts/Customer Receipt " + order.getOrderId() + ".txt";

    ofstream file(filePath);

    if (!file.is_open()) {
        cout << "\nUnable to generate receipt file.\n";
        return false;
    }

    file << "========================================\n";
    file << "           RESTAURANT RECEIPT            \n";
    file << "========================================\n\n";

    file << "Order ID:    " << order.getOrderId() << "\n";
    file << "Customer ID: " << order.getCustomerId() << "\n";
    file << "Status:      " << order.getStatus() << "\n";

    file << "\n----------------------------------------\n";
    file << "ORDER DETAILS\n";
    file << "----------------------------------------\n";

    file << order.getSummary() << "\n";

    file << "\n----------------------------------------\n";
    file << fixed << setprecision(2);

    file << "Total:   PHP " << order.getTotal() << "\n";
    file << "Payment: PHP " << order.getPayment() << "\n";
    file << "Change:  PHP " << order.getChange() << "\n";

    file << "\n========================================\n";
    file << "          THANK YOU FOR ORDERING!        \n";
    file << "========================================\n";

    file.close();

    cout << "\nReceipt generated successfully!\n";
    cout << "File: " << filePath << "\n";

    return true;
}