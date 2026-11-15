package ordering;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Persistent restaurant menu stored in menu.txt so the admin can add, update,
 * and delete items without recompiling the program.
 *
 * File format: CATEGORY|name|price
 */
public final class Menu {
    public static final String MENU_FILE = "menu.txt";

    public static final String MEALS = "MEALS";
    public static final String DRINKS = "DRINKS";
    public static final String DESSERTS = "DESSERTS";
    public static final String SIDES = "SIDES";
    public static final String BUNDLES = "BUNDLES";

    public static final String[] CATEGORIES = {MEALS, DRINKS, DESSERTS, SIDES, BUNDLES};

    private final Map<String, List<MenuItem>> items = new LinkedHashMap<>();

    private Menu() {
        for (String category : CATEGORIES) {
            items.put(category, new ArrayList<>());
        }
    }

    public static Menu load() {
        Menu menu = new Menu();
        File file = new File(MENU_FILE);
        if (!file.exists()) {
            menu.seedDefaults();
            menu.save();
            System.out.println("[Menu] Created " + MENU_FILE + " with the default menu.");
            return menu;
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
                    System.out.println("[Menu] Skipping malformed line: " + trimmed);
                    continue;
                }
                String category = parts[0].trim().toUpperCase(Locale.US);
                String name = parts[1].trim();
                if (!menu.items.containsKey(category) || !isValidName(name)) {
                    System.out.println("[Menu] Skipping malformed line: " + trimmed);
                    continue;
                }
                double price;
                try {
                    price = Double.parseDouble(parts[2].trim());
                } catch (NumberFormatException exception) {
                    System.out.println("[Menu] Skipping malformed price: " + trimmed);
                    continue;
                }
                if (!Double.isFinite(price) || price <= 0) {
                    System.out.println("[Menu] Skipping invalid price: " + trimmed);
                    continue;
                }
                menu.items.get(category).add(new MenuItem(name, price));
            }
        } catch (IOException exception) {
            System.out.println("[Menu] Could not read " + MENU_FILE + ": " + exception.getMessage());
            System.out.println("[Menu] Loading the default menu instead.");
            menu.seedDefaults();
        }
        return menu;
    }

    public List<MenuItem> items(String category) {
        List<MenuItem> list = items.get(category);
        if (list == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(list);
    }

    public int itemCount(String category) {
        return items(category).size();
    }

    public MenuItem itemAt(String category, int index) {
        return items(category).get(index);
    }

    public void add(String category, String name, double price) {
        listFor(category).add(new MenuItem(name, price));
    }

    public void update(String category, int index, String name, double price) {
        listFor(category).set(index, new MenuItem(name, price));
    }

    public void remove(String category, int index) {
        listFor(category).remove(index);
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MENU_FILE))) {
            for (String category : CATEGORIES) {
                for (MenuItem item : items.get(category)) {
                    writer.write(String.format(Locale.US, "%s|%s|%.2f\n",
                            category, item.name(), item.price()));
                }
            }
        } catch (IOException exception) {
            System.out.println("[Menu] Could not save " + MENU_FILE + ": " + exception.getMessage());
        }
    }

    public static String label(String category) {
        switch (category) {
            case MEALS:
                return "Meals";
            case DRINKS:
                return "Drinks";
            case DESSERTS:
                return "Desserts";
            case SIDES:
                return "Side Dishes";
            case BUNDLES:
                return "Bundles";
            default:
                return category;
        }
    }

    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && !name.contains("|");
    }

    private List<MenuItem> listFor(String category) {
        List<MenuItem> list = items.get(category);
        if (list == null) {
            throw new IllegalArgumentException("Unknown menu category: " + category);
        }
        return list;
    }

    private void seedDefaults() {
        add(MEALS, "Meal 1 -> Fried Chicken W/ Garlic Rice", 156.25);
        add(MEALS, "Meal 2 -> Pasta and Wings W/ Garlic Bread", 184.60);
        add(MEALS, "Meal 3 -> Steak and Fries W/ Garlic Bread", 450.00);
        add(DRINKS, "Sprite", 40.00);
        add(DRINKS, "Juice", 20.00);
        add(DRINKS, "Coffee", 30.00);
        add(DESSERTS, "Tiramisu", 60.00);
        add(DESSERTS, "Ice Cream", 20.00);
        add(DESSERTS, "Cheesecake", 90.00);
        add(SIDES, "Coleslaw", 25.00);
        add(SIDES, "Mashed Potato", 35.00);
        add(SIDES, "Buttered Corn", 25.00);
        add(BUNDLES, "Bundle 1 -> Meal 1, Juice, Tiramisu, & Coleslaw", 380.60);
        add(BUNDLES, "Bundle 2 -> Meal 2, Sprite, Ice Cream, & Buttered Corn", 340.60);
        add(BUNDLES, "Bundle 3 -> Meal 3, Coffee, Cheesecake, & Mashed Potato", 600.00);
    }
}
