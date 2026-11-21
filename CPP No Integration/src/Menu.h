#ifndef MENU_H
#define MENU_H

#include "MenuItem.h"
#include <vector>
#include <string>

using namespace std;

class Menu {
private:
    vector<MenuItem> items;
    string filePath;

    void createDefaultMenu();

public:
    Menu();

    void initialize();
    void loadMenu();
    bool saveMenu() const;

    void displayMenu() const;
    void addItem(const MenuItem& item);
    bool updateItem(int index, const string& newName, double newPrice);
    bool deleteItem(int index);

    int getItemCount() const;
    MenuItem getItem(int index) const;
    const vector<MenuItem>& getItems() const;
};

#endif
