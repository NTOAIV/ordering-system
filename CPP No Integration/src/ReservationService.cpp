#include "ReservationService.h"
#include "Console.h"

#include <iostream>
#include <iomanip>

using namespace std;

ReservationService::ReservationService() {
    reservationRepository.initialize();
    customerRepository.initialize();
}

void ReservationService::setReservation() {
    cout << "\n========== SET RESERVATION ==========\n";

    int customerId = Console::getInt("Enter your Customer ID: ");

    // Check whether the customer is registered.
    vector<CustomerRecord> customers = customerRepository.loadCustomers();

    bool customerExists = false;

    for (const CustomerRecord& customer : customers) {
        if (customer.getCustomerId() == customerId) {
            customerExists = true;
            break;
        }
    }

    if (!customerExists) {
        cout << "\nCustomer ID not found. Please register first.\n";
        Console::pause();
        return;
    }

    string date = Console::getString("Enter reservation date (YYYY-MM-DD): ");
    string time = Console::getString("Enter reservation time: ");
    int guests = Console::getInt("Enter number of guests: ");

    if (date.empty() || time.empty() ||
        date.find('|') != string::npos ||
        time.find('|') != string::npos ||
        guests <= 0) {

        cout << "\nInvalid reservation details.\n";
        Console::pause();
        return;
    }

    vector<Reservation> reservations =
        reservationRepository.loadReservations();

    bool replaced = false;

    // Replace the existing reservation for this customer.
    for (Reservation& reservation : reservations) {
        if (reservation.getCustomerId() == customerId) {
            reservation = Reservation(customerId, date, time, guests);
            replaced = true;
            break;
        }
    }

    // If the customer has no reservation, add a new one.
    if (!replaced) {
        reservations.emplace_back(customerId, date, time, guests);
    }

    if (reservationRepository.saveReservations(reservations)) {
        if (replaced) {
            cout << "\nYour reservation has been updated successfully.\n";
        }
        else {
            cout << "\nYour reservation has been saved successfully.\n";
        }
    }
    else {
        cout << "\nError saving reservation.\n";
    }

    Console::pause();
}

void ReservationService::viewAllReservations() {
    cout << "\n========== ALL RESERVATIONS ==========\n";

    vector<Reservation> reservations =
        reservationRepository.loadReservations();

    if (reservations.empty()) {
        cout << "\nNo reservations found.\n";
        Console::pause();
        return;
    }

    cout << left
         << setw(15) << "Customer ID"
         << setw(18) << "Date"
         << setw(15) << "Time"
         << setw(10) << "Guests"
         << "\n";

    cout << "----------------------------------------------------------\n";

    for (const Reservation& reservation : reservations) {
        cout << left
             << setw(15) << reservation.getCustomerId()
             << setw(18) << reservation.getDate()
             << setw(15) << reservation.getTime()
             << setw(10) << reservation.getGuests()
             << "\n";
    }

    Console::pause();
}