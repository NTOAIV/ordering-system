package ordering;

/** One customer account. Reservations are stored separately. */
public final class CustomerRecord {
    private final int id;
    private final String name;
    private final String contact;

    public CustomerRecord(int id, String name, String contact) {
        this.id = id;
        this.name = name;
        this.contact = contact;
    }

    public int id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String contact() {
        return contact;
    }
}
