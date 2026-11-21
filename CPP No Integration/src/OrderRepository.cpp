#include "OrderRepository.h"

#include <fstream>
#include <sstream>
#include <filesystem>
#include <iomanip>

using namespace std;

OrderRepository::OrderRepository() {
    filePath = "data/orders.txt";
}

void OrderRepository::initialize() {
    filesystem::create_directories("data");

    if (!filesystem::exists(filePath)) {
        ofstream file(filePath);
        file.close();
    }
}

vector<Order> OrderRepository::loadOrders() const {
    vector<Order> orders;
    ifstream file(filePath);

    string line;

    while (getline(file, line)) {
        stringstream ss(line);

        string orderId;
        string customerIdText;
        string status;
        string totalText;
        string paymentText;
        string changeText;
        string summary;

        if (getline(ss, orderId, '|') &&
            getline(ss, customerIdText, '|') &&
            getline(ss, status, '|') &&
            getline(ss, totalText, '|') &&
            getline(ss, paymentText, '|') &&
            getline(ss, changeText, '|') &&
            getline(ss, summary)) {

            try {
                int customerId = stoi(customerIdText);
                double total = stod(totalText);
                double payment = stod(paymentText);
                double change = stod(changeText);

                orders.emplace_back(
                    orderId,
                    customerId,
                    status,
                    total,
                    payment,
                    change,
                    summary
                );
            }
            catch (...) {
                // Ignore invalid records.
            }
        }
    }

    return orders;
}

bool OrderRepository::saveOrders(const vector<Order>& orders) const {
    ofstream file(filePath);

    if (!file.is_open()) {
        return false;
    }

    file << fixed << setprecision(2);

    for (const Order& order : orders) {
        file << order.getOrderId() << "|"
             << order.getCustomerId() << "|"
             << order.getStatus() << "|"
             << order.getTotal() << "|"
             << order.getPayment() << "|"
             << order.getChange() << "|"
             << order.getSummary() << "\n";
    }

    return true;
}

string OrderRepository::generateNextId(const vector<Order>& orders) const {
    int highestNumber = 1000;

    for (const Order& order : orders) {
        string id = order.getOrderId();

        if (id.rfind("TXN-", 0) == 0) {
            try {
                int number = stoi(id.substr(4));

                if (number > highestNumber) {
                    highestNumber = number;
                }
            }
            catch (...) {
                // Ignore IDs that do not have a valid number.
            }
        }
    }

    return "TXN-" + to_string(highestNumber + 1);
}