#ifndef CUSTOMER_RECORD_SERVICE_H
#define CUSTOMER_RECORD_SERVICE_H

#include "CustomerRepository.h"

class CustomerRecordService {
private:
    CustomerRepository repository;

public:
    CustomerRecordService();

    void registerAccount();
    void updateAccount();
    void viewAccounts() const;
};

#endif
