#ifndef ORDER_REPOSITORY_H
#define ORDER_REPOSITORY_H

#include "Order.h"
#include <vector>
#include <string>
using namespace std;

class OrderRepository {
private:
    string filePath;

public:
    OrderRepository();

    void initialize();
    vector<Order> loadOrders() const;
    bool saveOrders(const vector<Order>& orders) const;
    string generateNextId(const vector<Order>& orders) const;
};

#endif