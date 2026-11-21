#ifndef RESERVATION_SERVICE_H
#define RESERVATION_SERVICE_H

#include "ReservationRepository.h"
#include "CustomerRepository.h"

class ReservationService {
private:
    ReservationRepository reservationRepository;
    CustomerRepository customerRepository;

public:
    ReservationService();

    void setReservation();
    void viewAllReservations();
};

#endif