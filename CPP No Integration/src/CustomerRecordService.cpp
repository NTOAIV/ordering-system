#include "CustomerRecordService.h"
#include "Console.h"

#include <iostream>
#include <iomanip>

using namespace std;

CustomerRecordService::CustomerRecordService() {
    repository.initialize();
}

void CustomerRecordService::registerAccount() {
    vector<CustomerRecord> customers = repository.loadCustomers();

    cout << "\n========== REGISTER ACCOUNT ==========\n";

    string name = Console::getString("Enter customer name: ");
    string contact = Console::getString("Enter contact number: ");

    if (name.empty() || contact.empty()) {
        cout << "\nName and contact number cannot be empty.\n";
        Console::pause();
        return;
    }

    if (name.find('|') != string::npos ||
        contact.find('|') != string::npos) {
        cout << "\nThe character '|' is not allowed.\n";
        Console::pause();
        return;
    }

    int newId = repository.generateNextId(customers);

    customers.emplace_back(newId, name, contact);

    if (repository.saveCustomers(customers)) {
        cout << "\nAccount successfully registered!\n";
        cout << "Customer ID: " << newId << "\n";
        cout << "Name: " << name << "\n";
        cout << "Contact: " << contact << "\n";
    }
    else {
        cout << "\nError: Unable to save customer account.\n";
    }

    Console::pause();
}

void CustomerRecordService::updateAccount() {
    vector<CustomerRecord> customers = repository.loadCustomers();

    cout << "\n========== UPDATE ACCOUNT ==========\n";

    int id = Console::getInt("Enter your customer ID: ");

    auto it = customers.begin();

    while (it != customers.end() &&
           it->getCustomerId() != id) {
        ++it;
    }

    if (it == customers.end()) {
        cout << "\nCustomer account not found.\n";
        Console::pause();
        return;
    }

    cout << "\nCurrent Account Details\n";
    cout << "Customer ID: " << it->getCustomerId() << "\n";
    cout << "Name: " << it->getName() << "\n";
    cout << "Contact: " << it->getContact() << "\n";

    cout << "\nLeave an input blank to keep the existing value.\n";

    string newName = Console::getString("New name: ");
    string newContact = Console::getString("New contact number: ");

    if (newName.find('|') != string::npos ||
        newContact.find('|') != string::npos) {
        cout << "\nThe character '|' is not allowed.\n";
        Console::pause();
        return;
    }

    if (!newName.empty()) {
        it->setName(newName);
    }

    if (!newContact.empty()) {
        it->setContact(newContact);
    }

    if (repository.saveCustomers(customers)) {
        cout << "\nAccount successfully updated!\n";
    }
    else {
        cout << "\nError: Unable to save account changes.\n";
    }

    Console::pause();
}

void CustomerRecordService::viewAccounts() const {
    vector<CustomerRecord> customers = repository.loadCustomers();

    cout << "\n================ CUSTOMER ACCOUNTS ================\n";

    if (customers.empty()) {
        cout << "No customer accounts found.\n";
        Console::pause();
        return;
    }

    cout << left
         << setw(15) << "Customer ID"
         << setw(25) << "Name"
         << setw(20) << "Contact" << "\n";

    cout << "----------------------------------------------------\n";

    for (const CustomerRecord& customer : customers) {
        cout << left
             << setw(15) << customer.getCustomerId()
             << setw(25) << customer.getName()
             << setw(20) << customer.getContact()
             << "\n";
    }

    cout << "====================================================\n";
    cout << "Total customers: " << customers.size() << "\n";

    Console::pause();
}
