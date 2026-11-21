#ifndef RESERVATION_H
#define RESERVATION_H

#include <string>
using namespace std;

class Reservation {
private:
    int customerId;
    string date;
    string time;
    int guests;

public:
    Reservation(int customerId, string date, string time, int guests);

    int getCustomerId() const;
    string getDate() const;
    string getTime() const;
    int getGuests() const;
};

#endif