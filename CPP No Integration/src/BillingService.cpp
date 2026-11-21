#include "BillingService.h"
#include "Console.h"

#include <iostream>
#include <string>
#include <sstream>
#include <iomanip>

using namespace std;

bool BillingService::processPayment(
    double total,
    double& payment,
    double& change
) {
    cout << "\n========== PAYMENT ==========\n";
    cout << fixed << setprecision(2);
    cout << "Amount Due: PHP " << total << "\n";

    while (true) {
        string input = Console::getString(
            "Enter payment amount or X to cancel: "
        );

        if (input == "X" || input == "x") {
            cout << "\nCheckout cancelled. Your order remains pending.\n";
            return false;
        }

        try {
            size_t processed = 0;
            double enteredAmount = stod(input, &processed);

            // Reject extra characters and invalid amounts.
            if (processed != input.length() || enteredAmount <= 0) {
                cout << "Please enter a valid positive amount.\n";
                continue;
            }

            if (enteredAmount < total) {
                cout << "Insufficient payment. You still owe PHP "
                     << fixed << setprecision(2)
                     << total - enteredAmount << "\n";
                continue;
            }

            payment = enteredAmount;
            change = payment - total;

            cout << "\nPayment accepted.\n";
            cout << "Change: PHP " << fixed << setprecision(2)
                 << change << "\n";

            return true;
        }
        catch (...) {
            cout << "Invalid payment. Please enter a number or X.\n";
        }
    }
}