#include <iostream>
#include "Console.h"
#include "CustomerPortal.h"
#include "AdminService.h"

using namespace std;

int main() {
    CustomerPortal customerPortal;
    AdminService adminService;

    int choice;

    do {
        Console::clearScreen();

        cout << "================================\n";
        cout << "    RESTAURANT ORDERING SYSTEM\n";
        cout << "================================\n";
        cout << "[1] Customer Portal\n";
        cout << "[2] Admin Portal\n";
        cout << "[0] Exit System\n";
        cout << "================================\n";

        choice = Console::getInt("Enter choice: ");

        switch (choice) {
            case 1:
                customerPortal.show();
                break;

            case 2:
                adminService.show();
                break;

            case 0:
                cout << "Thank you for using the system!\n";
                break;

            default:
                cout << "Invalid choice.\n";
                Console::pause();
        }

    } while (choice != 0);

    return 0;
}
