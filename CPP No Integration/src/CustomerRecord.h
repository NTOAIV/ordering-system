#ifndef CUSTOMER_RECORD_H
#define CUSTOMER_RECORD_H

#include <string>
using namespace std;

class CustomerRecord {
private:
    int customerId;
    string name;
    string contact;

public:
    CustomerRecord(int id, string name, string contact);

    int getCustomerId() const;
    string getName() const;
    string getContact() const;

    void setName(const string& newName);
    void setContact(const string& newContact);
};

#endif
