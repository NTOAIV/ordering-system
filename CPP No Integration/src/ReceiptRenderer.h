#ifndef RECEIPT_RENDERER_H
#define RECEIPT_RENDERER_H

#include "Order.h"

class ReceiptRenderer {
public:
    bool generateReceipt(const Order& order) const;
};

#endif