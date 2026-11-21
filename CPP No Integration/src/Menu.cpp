#include "Menu.h"

#include <iostream>
#include <fstream>
#include <sstream>
#include <iomanip>
#include <filesystem>
#include <algorithm>
#include <cctype>

using namespace std;

Menu::Menu() {
    filePath = "data/menu.txt";
}

void Menu::initialize() {
    filesystem::create_directories("data");

    ifstream file(filePath);

    if (!file.good()) {
        createDefaultMenu();
        return;
    }

    file.seekg(0, ios::end);

    if (file.tellg() == 0) {
        file.close();
        createDefaultMenu();
        return;
    }

    loadMenu();
}

void Menu::createDefaultMenu() {
    items.clear();

    items.emplace_back("MEALS", "Fried Chicken with Garlic Rice", 120.00);
    items.emplace_back("MEALS", "Beef Burger Steak with Rice", 150.00);
    items.emplace_back("MEALS", "Pork BBQ with Rice", 130.00);

    items.emplace_back("DRINKS", "Iced Tea", 35.00);
    items.emplace_back("DRINKS", "Lemonade", 40.00);
    items.emplace_back("DRINKS", "Bottled Water", 25.00);

    items.emplace_back("DESSERTS", "Chocolate Cake", 80.00);
    items.emplace_back("DESSERTS", "Ice Cream", 50.00);

    items.emplace_back("SIDES", "French Fries", 60.00);
    items.emplace_back("SIDES", "Garlic Rice", 30.00);

    items.emplace_back("BUNDLES", "Chicken Meal Bundle", 180.00);
    items.emplace_back("BUNDLES", "Family BBQ Bundle", 450.00);

    saveMenu();
}

void Menu::loadMenu() {
    items.clear();

    ifstream file(filePath);

    if (!file.is_open()) {
        return;
    }

    string line;

    while (getline(file, line)) {
        stringstream ss(line);

        string category;
        string name;
        string priceText;

        if (!getline(ss, category, '|') ||
            !getline(ss, name, '|') ||
            !getline(ss, priceText)) {
            continue;
        }

        try {
            double price = stod(priceText);

            if (category.empty() || name.empty() || price < 0) {
                continue;
            }

            items.emplace_back(category, name, price);
        }
        catch (...) {
            continue;
        }
    }

    if (items.empty()) {
        createDefaultMenu();
    }
}

bool Menu::saveMenu() const {
    ofstream file(filePath);

    if (!file.is_open()) {
        return false;
    }

    file << fixed << setprecision(2);

    for (const MenuItem& item : items) {
        file << item.getCategory() << "|"
             << item.getName() << "|"
             << item.getPrice() << "\n";
    }

    return file.good();
}

void Menu::displayMenu() const {
    const vector<string> categories = {
        "MEALS", "DRINKS", "DESSERTS", "SIDES", "BUNDLES"
    };

    cout << "\n================ RESTAURANT MENU ================\n";

    for (const string& category : categories) {
        cout << "\n--- " << category << " ---\n";

        bool found = false;

        for (size_t i = 0; i < items.size(); i++) {
            if (items[i].getCategory() == category) {
                found = true;

                cout << "[" << i + 1 << "] "
                     << left << setw(38) << items[i].getName()
                     << "PHP " << fixed << setprecision(2)
                     << items[i].getPrice() << "\n";
            }
        }

        if (!found) {
            cout << "No items available.\n";
        }
    }

    cout << "\n==================================================\n";
}

void Menu::addItem(const MenuItem& item) {
    items.push_back(item);
}

bool Menu::updateItem(
    int index,
    const string& newName,
    double newPrice
) {
    if (index < 0 || index >= static_cast<int>(items.size())) {
        return false;
    }

    if (!newName.empty()) {
        items[index].setName(newName);
    }

    if (newPrice >= 0) {
        items[index].setPrice(newPrice);
    }

    return true;
}

bool Menu::deleteItem(int index) {
    if (index < 0 || index >= static_cast<int>(items.size())) {
        return false;
    }

    items.erase(items.begin() + index);

    return true;
}

int Menu::getItemCount() const {
    return static_cast<int>(items.size());
}

MenuItem Menu::getItem(int index) const {
    return items.at(index);
}

const vector<MenuItem>& Menu::getItems() const {
    return items;
}
