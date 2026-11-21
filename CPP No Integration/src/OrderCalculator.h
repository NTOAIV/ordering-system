#ifndef ORDER_CALCULATOR_H
#define ORDER_CALCULATOR_H

#include "OrderItem.h"
#include <vector>

class OrderCalculator {
public:
    static double calculateTotal(const vector<OrderItem>& items);
};

#endif