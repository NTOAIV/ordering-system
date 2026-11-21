#ifndef ORDER_H
#define ORDER_H

#include <string>
using namespace std;

class Order {
private:
    string orderId;
    int customerId;
    string status;
    double total;
    double payment;
    double change;
    string summary;

public:
    Order(
        string orderId,
        int customerId,
        string status,
        double total,
        double payment,
        double change,
        string summary
    );

    string getOrderId() const;
    int getCustomerId() const;
    string getStatus() const;
    double getTotal() const;
    double getPayment() const;
    double getChange() const;
    string getSummary() const;

    void setStatus(const string& newStatus);
    void setPayment(double newPayment);
    void setChange(double newChange);
};

#endif