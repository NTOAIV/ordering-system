package ordering;

/** Customer-facing reservation action. */
public final class ReservationService {
    private final Console console;
    private final ReservationRepository reservations;

    public ReservationService(Console console, ReservationRepository reservations) {
        this.console = console;
        this.reservations = reservations;
    }

    public void setReservation(int customerId) {
        System.out.println("\nSET RESERVATION");
        Reservation existing = reservations.findByCustomer(customerId);
        if (existing != null) {
            System.out.println("Current reservation: " + existing.date() + " at " + existing.time()
                    + " for " + existing.guests() + " guest(s).");
            System.out.println("Saving a new reservation will replace it.");
        }

        String date = readField("Reservation date (MM/DD/YY): ");
        String time = readField("Reservation time (HH:MM AM/PM): ");
        int guests = console.readPositiveInt("Number of guests: ");

        reservations.save(new Reservation(customerId, date, time, guests));
        System.out.println("Reservation saved for customer ID " + customerId + ".");
    }

    private String readField(String prompt) {
        while (true) {
            String value = console.readText(prompt);
            if (!value.contains("|")) {
                return value;
            }
            System.out.println("This field cannot contain the '|' character.");
        }
    }
}
