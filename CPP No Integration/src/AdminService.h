#ifndef ADMIN_SERVICE_H
#define ADMIN_SERVICE_H

#include "CustomerRecordService.h"
#include "ReservationService.h"
#include "OrderService.h"
#include "Menu.h"

class AdminService {
private:
    CustomerRecordService customerService;
    ReservationService reservationService;
    OrderService orderService;
    Menu menu;

    void manageMenu();
    void addMenuItem();
    void updateMenuItem();
    void deleteMenuItem();

public:
    AdminService();
    void show();
};

#endif