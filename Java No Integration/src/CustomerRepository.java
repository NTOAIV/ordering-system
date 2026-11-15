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
 * Customer accounts stored in Customer_Records.txt.
 * File format: ID|Name|Contact
 */
public final class CustomerRepository {
    public static final String FILE = "Customer_Records.txt";

    private final List<CustomerRecord> records = new ArrayList<>();

    public CustomerRepository() {
        load();
    }

    public List<CustomerRecord> findAll() {
        return Collections.unmodifiableList(records);
    }

    public CustomerRecord findById(int id) {
        for (CustomerRecord record : records) {
            if (record.id() == id) {
                return record;
            }
        }
        return null;
    }

    public boolean exists(int id) {
        return findById(id) != null;
    }

    public void add(CustomerRecord record) {
        records.add(record);
        saveAll();
    }

    public boolean update(CustomerRecord record) {
        int index = indexOf(record.id());
        if (index < 0) {
            return false;
        }
        records.set(index, record);
        saveAll();
        return true;
    }

    private int indexOf(int id) {
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).id() == id) {
                return i;
            }
        }
        return -1;
    }

    private void load() {
        File file = new File(FILE);
        if (!file.exists()) {
            records.add(new CustomerRecord(25, "Abe", "239120"));
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
                if (parts.length < 3) {
                    System.out.println("[Customers] Skipping malformed line: " + trimmed);
                    continue;
                }
                int id;
                try {
                    id = Integer.parseInt(parts[0].trim());
                } catch (NumberFormatException exception) {
                    System.out.println("[Customers] Skipping invalid ID: " + trimmed);
                    continue;
                }
                String name = parts[1].trim();
                String contact = parts[2].trim();
                if (id <= 0 || name.isEmpty() || exists(id)) {
                    System.out.println("[Customers] Skipping invalid or duplicate record: " + trimmed);
                    continue;
                }
                records.add(new CustomerRecord(id, name, contact));
            }
        } catch (IOException exception) {
            System.out.println("[Customers] Could not read " + FILE + ": " + exception.getMessage());
        }
    }

    private void saveAll() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE))) {
            for (CustomerRecord record : records) {
                writer.write(record.id() + "|" + record.name() + "|" + record.contact() + "\n");
            }
        } catch (IOException exception) {
            System.out.println("[Customers] Could not save " + FILE + ": " + exception.getMessage());
        }
    }
}
