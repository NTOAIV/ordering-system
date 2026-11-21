#ifndef ORDER_SERVICE_H
#define ORDER_SERVICE_H

#include "OrderRepository.h"
#include "OrderCalculator.h"
#include "CustomerRepository.h"
#include "BillingService.h"
#include "ReceiptRenderer.h"
#include "Menu.h"

class OrderService {
private:
    OrderRepository orderRepository;
    CustomerRepository customerRepository;
    BillingService billingService;
    ReceiptRenderer receiptRenderer;
    Menu menu;

public:
    OrderService();

    void placeOrder();
    void checkout();
    void viewAllOrders() const;
};

#endif