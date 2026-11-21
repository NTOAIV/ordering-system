#include "OrderCalculator.h"

double OrderCalculator::calculateTotal(const vector<OrderItem>& items) {
    double total = 0.0;

    for (const OrderItem& item : items) {
        total += item.getSubtotal();
    }

    return total;
}