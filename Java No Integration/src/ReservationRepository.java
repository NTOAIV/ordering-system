package ordering;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Reservations stored in Reservations.txt.
 * File format: customerId|date|time|guests
 *
 * Each customer has at most one reservation. Saving a new reservation for an
 * existing customer replaces the old one.
 */
public final class ReservationRepository {
    public static final String FILE = "Reservations.txt";

    private final List<Reservation> reservations = new ArrayList<>();

    public ReservationRepository() {
        load();
    }

    public List<Reservation> findAll() {
        return Collections.unmodifiableList(reservations);
    }

    public Reservation findByCustomer(int customerId) {
        for (Reservation reservation : reservations) {
            if (reservation.customerId() == customerId) {
                return reservation;
            }
        }
        return null;
    }

    public void save(Reservation reservation) {
        int index = indexOf(reservation.customerId());
        if (index >= 0) {
            reservations.set(index, reservation);
        } else {
            reservations.add(reservation);
        }
        saveAll();
    }

    private int indexOf(int customerId) {
        for (int i = 0; i < reservations.size(); i++) {
            if (reservations.get(i).customerId() == customerId) {
                return i;
            }
        }
        return -1;
    }

    private void load() {
        File file = new File(FILE);
        if (!file.exists()) {
            reservations.add(new Reservation(25, "12/12/24", "05:00 P.M", 2));
            saveAll();
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }
                String[] parts = trimmed.split("\\|", -1);
                if (parts.length < 4) {
                    System.out.println("[Reservations] Skipping malformed line: " + trimmed);
                    continue;
                }
                int customerId;
                int guests;
                try {
                    customerId = Integer.parseInt(parts[0].trim());
                    guests = Integer.parseInt(parts[3].trim());
                } catch (NumberFormatException exception) {
                    System.out.println("[Reservations] Skipping invalid numbers: " + trimmed);
                    continue;
                }
                String date = parts[1].trim();
                String time = parts[2].trim();
                if (customerId <= 0 || date.isEmpty() || time.isEmpty() || guests <= 0
                        || findByCustomer(customerId) != null) {
                    System.out.println("[Reservations] Skipping invalid or duplicate reservation: " + trimmed);
                    continue;
                }
                reservations.add(new Reservation(customerId, date, time, guests));
            }
        } catch (IOException exception) {
            System.out.println("[Reservations] Could not read " + FILE + ": " + exception.getMessage());
        }
    }

    private void saveAll() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE))) {
            for (Reservation reservation : reservations) {
                writer.write(reservation.customerId() + "|" + reservation.date() + "|"
                        + reservation.time() + "|" + reservation.guests() + "\n");
            }
        } catch (IOException exception) {
            System.out.println("[Reservations] Could not save " + FILE + ": " + exception.getMessage());
        }
    }
}
