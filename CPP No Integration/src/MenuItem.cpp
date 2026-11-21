#include "MenuItem.h"

MenuItem::MenuItem(string category, string name, double price) {
    this->category = category;
    this->name = name;
    this->price = price;
}

string MenuItem::getCategory() const {
    return category;
}

string MenuItem::getName() const {
    return name;
}

double MenuItem::getPrice() const {
    return price;
}

void MenuItem::setName(const string& newName) {
    name = newName;
}

void MenuItem::setPrice(double newPrice) {
    price = newPrice;
}
