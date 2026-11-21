#ifndef ORDER_ITEM_H
#define ORDER_ITEM_H

#include <string>
using namespace std;

class OrderItem {
private:
    string name;
    double price;
    int quantity;

public:
    OrderItem(string name, double price, int quantity);

    string getName() const;
    double getPrice() const;
    int getQuantity() const;
    double getSubtotal() const;
};

#endif