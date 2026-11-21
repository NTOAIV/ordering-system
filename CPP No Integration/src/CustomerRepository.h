#ifndef CUSTOMER_REPOSITORY_H
#define CUSTOMER_REPOSITORY_H

#include "CustomerRecord.h"
#include <vector>
#include <string>

using namespace std;

class CustomerRepository {
private:
    string filePath;

public:
    CustomerRepository();

    void initialize();
    vector<CustomerRecord> loadCustomers() const;
    bool saveCustomers(const vector<CustomerRecord>& customers) const;
    int generateNextId(const vector<CustomerRecord>& customers) const;
};

#endif
