#include "OrderItem.h"

OrderItem::OrderItem(string name, double price, int quantity) {
    this->name = name;
    this->price = price;
    this->quantity = quantity;
}

string OrderItem::getName() const {
    return name;
}

double OrderItem::getPrice() const {
    return price;
}

int OrderItem::getQuantity() const {
    return quantity;
}

double OrderItem::getSubtotal() const {
    return price * quantity;
}