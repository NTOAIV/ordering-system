#ifndef RESERVATION_REPOSITORY_H
#define RESERVATION_REPOSITORY_H

#include "Reservation.h"
#include <vector>
#include <string>
using namespace std;

class ReservationRepository {
private:
    string filePath;

public:
    ReservationRepository();

    void initialize();
    vector<Reservation> loadReservations() const;
    bool saveReservations(const vector<Reservation>& reservations) const;
};

#endif