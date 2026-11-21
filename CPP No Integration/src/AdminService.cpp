#include "AdminService.h"
#include "Console.h"

#include <iostream>
#include <iomanip>
#include <vector>
#include <limits>
#include <sstream>

using namespace std;

AdminService::AdminService() {
    menu.initialize();
}

void AdminService::show() {
    int choice;

    do {
        cout << "\n========== ADMIN PORTAL ==========\n";
        cout << "[1] View All Orders\n";
        cout << "[2] View All Reservations\n";
        cout << "[3] View Customer Accounts\n";
        cout << "[4] Manage Menu\n";
        cout << "[0] Return to Main Menu\n";

        choice = Console::getInt("Enter choice: ");

        switch (choice) {
            case 1:
                orderService.viewAllOrders();
                break;

            case 2:
                reservationService.viewAllReservations();
                break;

            case 3:
                customerService.viewAccounts();
                break;

            case 4:
                manageMenu();
                break;

            case 0:
                break;

            default:
                cout << "\nInvalid choice.\n";
                Console::pause();
        }

    } while (choice != 0);
}

void AdminService::manageMenu() {
    menu.loadMenu();
    int choice;

    do {
        Console::clearScreen();

        cout << "================================\n";
        cout << "          MENU MANAGEMENT\n";
        cout << "================================\n";
        cout << "[1] View Menu\n";
        cout << "[2] Add Menu Item\n";
        cout << "[3] Update Menu Item\n";
        cout << "[4] Delete Menu Item\n";
        cout << "[0] Return to Admin Portal\n";
        cout << "================================\n";

        choice = Console::getInt("Enter choice: ");

        switch (choice) {
            case 1:
                menu.displayMenu();
                Console::pause();
                break;

            case 2:
                addMenuItem();
                break;

            case 3:
                updateMenuItem();
                break;

            case 4:
                deleteMenuItem();
                break;

            case 0:
                break;

            default:
                cout << "\nInvalid choice.\n";
                Console::pause();
        }

    } while (choice != 0);
}

void AdminService::addMenuItem() {
    cout << "\n========== ADD MENU ITEM ==========\n";

    cout << "[1] Meals\n";
    cout << "[2] Drinks\n";
    cout << "[3] Desserts\n";
    cout << "[4] Side Dishes\n";
    cout << "[5] Bundles\n";

    int categoryChoice = Console::getInt("Select category: ");

    vector<string> categories = {
        "MEALS", "DRINKS", "DESSERTS", "SIDES", "BUNDLES"
    };

    if (categoryChoice < 1 || categoryChoice > 5) {
        cout << "\nInvalid category.\n";
        Console::pause();
        return;
    }

    string category = categories[categoryChoice - 1];

    string name = Console::getString("Enter item name: ");
    string priceInput = Console::getString("Enter item price: ");

    if (name.empty() ||
        name.find('|') != string::npos ||
        priceInput.empty()) {
        cout << "\nInvalid item information.\n";
        Console::pause();
        return;
    }

    try {
        size_t pos = 0;
        double price = stod(priceInput, &pos);

        if (pos != priceInput.size() ||
            price < 0 ||
            priceInput.find('|') != string::npos) {
            cout << "\nInvalid price.\n";
            Console::pause();
            return;
        }

        menu.addItem(MenuItem(category, name, price));

        if (menu.saveMenu()) {
            cout << "\nMenu item successfully added!\n";
        }
        else {
            cout << "\nUnable to save menu item.\n";
        }
    }
    catch (...) {
        cout << "\nPlease enter a valid price.\n";
    }

    Console::pause();
}

void AdminService::updateMenuItem() {
    cout << "\n========== UPDATE MENU ITEM ==========\n";

    menu.displayMenu();

    if (menu.getItemCount() == 0) {
        Console::pause();
        return;
    }

    int choice = Console::getInt("\nEnter item number to update: ");

    if (choice < 1 || choice > menu.getItemCount()) {
        cout << "\nInvalid item number.\n";
        Console::pause();
        return;
    }

    MenuItem item = menu.getItem(choice - 1);

    cout << "\nCurrent name: " << item.getName() << "\n";
    cout << "Current price: " << fixed << setprecision(2)
         << item.getPrice() << "\n";

    cout << "\nLeave blank to keep the current value.\n";

    string newName = Console::getString("New name: ");
    string newPriceInput = Console::getString("New price: ");

    if (newName.find('|') != string::npos) {
        cout << "\nThe character '|' is not allowed.\n";
        Console::pause();
        return;
    }

    double newPrice = -1;

    if (!newPriceInput.empty()) {
        try {
            size_t pos = 0;
            newPrice = stod(newPriceInput, &pos);

            if (pos != newPriceInput.size() || newPrice < 0 ||
                newPriceInput.find('|') != string::npos) {
                cout << "\nInvalid price.\n";
                Console::pause();
                return;
            }
        }
        catch (...) {
            cout << "\nPlease enter a valid price.\n";
            Console::pause();
            return;
        }
    }

    if (newName.empty() && newPrice < 0) {
        cout << "\nNo changes were made.\n";
        Console::pause();
        return;
    }

    menu.updateItem(choice - 1, newName, newPrice);

    if (menu.saveMenu()) {
        cout << "\nMenu item successfully updated!\n";
    }
    else {
        cout << "\nUnable to save menu changes.\n";
    }

    Console::pause();
}

void AdminService::deleteMenuItem() {
    cout << "\n========== DELETE MENU ITEM ==========\n";

    menu.displayMenu();

    if (menu.getItemCount() == 0) {
        Console::pause();
        return;
    }

    int choice = Console::getInt("\nEnter item number to delete: ");

    if (choice < 1 || choice > menu.getItemCount()) {
        cout << "\nInvalid item number.\n";
        Console::pause();
        return;
    }

    MenuItem item = menu.getItem(choice - 1);

    cout << "\nSelected item: " << item.getName() << "\n";

    string confirmation = Console::getString(
        "Are you sure you want to delete this item? [Y/N]: "
    );

    if (confirmation == "Y" || confirmation == "y") {
        menu.deleteItem(choice - 1);

        if (menu.saveMenu()) {
            cout << "\nMenu item successfully deleted!\n";
        }
        else {
            cout << "\nUnable to save menu changes.\n";
        }
    }
    else {
        cout << "\nDeletion cancelled.\n";
    }

    Console::pause();
}
