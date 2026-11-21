#include "Reservation.h"

Reservation::Reservation(int customerId, string date, string time, int guests) {
    this->customerId = customerId;
    this->date = date;
    this->time = time;
    this->guests = guests;
}

int Reservation::getCustomerId() const {
    return customerId;
}

string Reservation::getDate() const {
    return date;
}

string Reservation::getTime() const {
    return time;
}

int Reservation::getGuests() const {
    return guests;
}