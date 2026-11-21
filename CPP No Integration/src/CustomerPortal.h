#ifndef CUSTOMER_PORTAL_H
#define CUSTOMER_PORTAL_H

#include "CustomerRecordService.h"
#include "ReservationService.h"
#include "OrderService.h"
#include "Menu.h"

class CustomerPortal {
private:
    CustomerRecordService customerService;
    ReservationService reservationService;
    OrderService orderService;
    Menu menu;

public:
    CustomerPortal();
    void show();
};

#endif
