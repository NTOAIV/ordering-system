#include "ReservationRepository.h"

#include <iostream>
#include <fstream>
#include <sstream>
#include <filesystem>

using namespace std;

ReservationRepository::ReservationRepository() {
    filePath = "data/Reservations.txt";
}

void ReservationRepository::initialize() {
    filesystem::create_directories("data");

    if (!filesystem::exists(filePath)) {
        ofstream file(filePath);
        file.close();
    }
}

vector<Reservation> ReservationRepository::loadReservations() const {
    vector<Reservation> reservations;
    ifstream file(filePath);

    string line;

    while (getline(file, line)) {
        stringstream ss(line);

        string customerIdText;
        string date;
        string time;
        string guestsText;

        if (getline(ss, customerIdText, '|') &&
            getline(ss, date, '|') &&
            getline(ss, time, '|') &&
            getline(ss, guestsText)) {

            try {
                int customerId = stoi(customerIdText);
                int guests = stoi(guestsText);

                reservations.emplace_back(customerId, date, time, guests);
            }
            catch (...) {
                // Ignore invalid records.
            }
        }
    }

    return reservations;
}

bool ReservationRepository::saveReservations(
    const vector<Reservation>& reservations
) const {
    ofstream file(filePath);

    if (!file.is_open()) {
        return false;
    }

    for (const Reservation& reservation : reservations) {
        file << reservation.getCustomerId() << "|"
             << reservation.getDate() << "|"
             << reservation.getTime() << "|"
             << reservation.getGuests() << "\n";
    }

    return true;
}