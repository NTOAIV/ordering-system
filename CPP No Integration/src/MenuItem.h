#ifndef MENU_ITEM_H
#define MENU_ITEM_H

#include <string>
using namespace std;

class MenuItem {
private:
    string category;
    string name;
    double price;

public:
    MenuItem(string category, string name, double price);

    string getCategory() const;
    string getName() const;
    double getPrice() const;

    void setName(const string& newName);
    void setPrice(double newPrice);
};

#endif
