#include "CustomerRepository.h"

#include <fstream>
#include <sstream>
#include <filesystem>
#include <algorithm>

using namespace std;

CustomerRepository::CustomerRepository() {
    filePath = "data/Customer_Records.txt";
}

void CustomerRepository::initialize() {
    filesystem::create_directories("data");

    ifstream file(filePath);

    if (!file.good()) {
        vector<CustomerRecord> seedCustomers;
        seedCustomers.emplace_back(25, "Abe", "239120");

        saveCustomers(seedCustomers);
        return;
    }

    file.seekg(0, ios::end);

    if (file.tellg() == 0) {
        file.close();

        vector<CustomerRecord> seedCustomers;
        seedCustomers.emplace_back(25, "Abe", "239120");

        saveCustomers(seedCustomers);
    }
}

vector<CustomerRecord> CustomerRepository::loadCustomers() const {
    vector<CustomerRecord> customers;

    ifstream file(filePath);

    if (!file.is_open()) {
        return customers;
    }

    string line;

    while (getline(file, line)) {
        stringstream ss(line);

        string idText;
        string name;
        string contact;

        if (!getline(ss, idText, '|') ||
            !getline(ss, name, '|') ||
            !getline(ss, contact)) {
            continue;
        }

        try {
            int id = stoi(idText);

            customers.emplace_back(id, name, contact);
        }
        catch (...) {
            continue;
        }
    }

    return customers;
}

bool CustomerRepository::saveCustomers(
    const vector<CustomerRecord>& customers
) const {
    ofstream file(filePath);

    if (!file.is_open()) {
        return false;
    }

    for (const CustomerRecord& customer : customers) {
        file << customer.getCustomerId() << "|"
             << customer.getName() << "|"
             << customer.getContact() << "\n";
    }

    return file.good();
}

int CustomerRepository::generateNextId(
    const vector<CustomerRecord>& customers
) const {
    int highestId = 24;

    for (const CustomerRecord& customer : customers) {
        highestId = max(highestId, customer.getCustomerId());
    }

    return highestId + 1;
}
