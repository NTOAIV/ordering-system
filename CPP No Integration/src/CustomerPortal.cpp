#include "CustomerPortal.h"
#include "Console.h"

#include <iostream>

using namespace std;

CustomerPortal::CustomerPortal() {
    menu.initialize();
}

void CustomerPortal::show() {
    menu.loadMenu();

    int choice;

    do {
        cout << "\n========== CUSTOMER PORTAL ==========\n";
        cout << "[1] Register Account\n";
        cout << "[2] Update Account Details\n";
        cout << "[3] View Menu & Place Order\n";
        cout << "[4] Set Reservation\n";
        cout << "[5] Check Out\n";
        cout << "[0] Return to Main Menu\n";

        choice = Console::getInt("Enter choice: ");

        switch (choice) {
            case 1:
                customerService.registerAccount();
                break;

            case 2:
                customerService.updateAccount();
                break;

            case 3:
                orderService.placeOrder();
                break;

            case 4:
                reservationService.setReservation();
                break;

            case 5:
                orderService.checkout();
                break;

            case 0:
                break;

            default:
                cout << "\nInvalid choice.\n";
                Console::pause();
        }

    } while (choice != 0);
}