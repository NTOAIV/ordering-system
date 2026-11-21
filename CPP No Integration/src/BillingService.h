#ifndef BILLING_SERVICE_H
#define BILLING_SERVICE_H

class BillingService {
public:
    bool processPayment(double total, double& payment, double& change);
};

#endif