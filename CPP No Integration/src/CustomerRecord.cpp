#include "CustomerRecord.h"

CustomerRecord::CustomerRecord(int id, string name, string contact) {
    customerId = id;
    this->name = name;
    this->contact = contact;
}

int CustomerRecord::getCustomerId() const {
    return customerId;
}

string CustomerRecord::getName() const {
    return name;
}

string CustomerRecord::getContact() const {
    return contact;
}

void CustomerRecord::setName(const string& newName) {
    name = newName;
}

void CustomerRecord::setContact(const string& newContact) {
    contact = newContact;
}
