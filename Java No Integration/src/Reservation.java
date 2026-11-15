package ordering;

/** One customer reservation. */
public final class Reservation {
    private final int customerId;
    private final String date;
    private final String time;
    private final int guests;

    public Reservation(int customerId, String date, String time, int guests) {
        this.customerId = customerId;
        this.date = date;
        this.time = time;
        this.guests = guests;
    }

    public int customerId() {
        return customerId;
    }

    public String date() {
        return date;
    }

    public String time() {
        return time;
    }

    public int guests() {
        return guests;
    }
}
