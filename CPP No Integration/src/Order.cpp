#include "Order.h"

Order::Order(
    string orderId,
    int customerId,
    string status,
    double total,
    double payment,
    double change,
    string summary
) {
    this->orderId = orderId;
    this->customerId = customerId;
    this->status = status;
    this->total = total;
    this->payment = payment;
    this->change = change;
    this->summary = summary;
}

string Order::getOrderId() const {
    return orderId;
}

int Order::getCustomerId() const {
    return customerId;
}

string Order::getStatus() const {
    return status;
}

double Order::getTotal() const {
    return total;
}

double Order::getPayment() const {
    return payment;
}

double Order::getChange() const {
    return change;
}

string Order::getSummary() const {
    return summary;
}

void Order::setStatus(const string& newStatus) {
    status = newStatus;
}

void Order::setPayment(double newPayment) {
    payment = newPayment;
}

void Order::setChange(double newChange) {
    change = newChange;
}