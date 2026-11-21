# C++ No Integration

A standalone, **C++-only** version of the restaurant ordering system. It has separate Customer and Admin portals, an editable menu, customer accounts, reservations, orders, and checkout receipts.

There is no Java or Python integration in this folder. Every part of the system runs in C++.

## How to run

### Windows

```bat
cd "CPP No Integration"
run.bat
```

Alternatively, compile and run manually using the terminal:

```powershell
g++ -std=c++17 -Wall -Wextra -I .\src .\src\*.cpp -o RestaurantOrderingSystem.exe
.\RestaurantOrderingSystem.exe
```

### Linux / macOS

If you have a compatible C++ compiler installed:

```bash
cd "CPP No Integration"
chmod +x run.sh
./run.sh
```

The program uses C++17 and stores its data in text files.

## Portals

### Customer portal

| Option | What it does |
|---|---|
| `[1] Register Account` | Creates a customer account with an ID, name, and contact number. |
| `[2] Update Account Details` | Updates an existing customer's name and contact number. |
| `[3] View Menu & Place Order` | Browses the current menu and builds an order. Maximum 5 items total. |
| `[4] Set Reservation` | Sets or replaces a reservation for the entered customer ID. |
| `[5] Check Out` | Shows the amount due, accepts payment, generates a receipt, and marks the order `PAID`. |
| `[X] Exit System` | Leaves the customer portal. An unpaid order remains available to the admin. |

The customer portal asks for a customer ID when an operation requires one. It does not currently have a password-based login or authentication system.

Customers cannot view the complete order history and cannot edit the menu.

### Admin portal

| Option | What it does |
|---|---|
| `[1] View All Orders` | Shows the orders recorded in the system, including their statuses. |
| `[2] View All Reservations` | Shows all customer reservations. |
| `[3] View Customer Accounts` | Displays customer IDs, names, and contact numbers. |
| `[4] Manage Menu` | Allows the admin to view, add, update, and delete menu items. |
| `[0] Return` | Returns to the previous portal selection. |

The admin cannot place customer orders, edit customer accounts, or directly edit reservations.

## Menu management

Inside `[4] Manage Menu`:

| Option | What it does |
|---|---|
| `[1] View Menu` | Shows menu categories, items, and prices. |
| `[2] Add Menu Item` | Adds an item to a menu category. |
| `[3] Update Menu Item` | Updates an existing menu item's information and/or price. |
| `[4] Delete Menu Item` | Deletes a menu item. |

Menu changes are saved to `data/menu.txt`, so they remain after restarting the program.

If the menu file is missing, the default menu is generated automatically.

Default menu categories:

- `MEALS`
- `DRINKS`
- `DESSERTS`
- `SIDES`
- `BUNDLES`

## Checkout behaviour

- The customer must have a pending order before checking out.
- The system calculates the amount due using the stored order.
- The customer must enter enough money to cover the total.
- Insufficient payment is rejected.
- A successful payment records the payment and change, marks the order `PAID`, and generates a receipt in the `receipts/` folder.
- Placing a new order cancels the customer's previous pending order.
- Each customer can have only one pending order at a time.
- A pending order can be checked out later, including after restarting the program, using its stored order information.

## Order lifecycle

```text
PENDING --> PAID
PENDING --> CANCELLED (replaced by a newer order)
```

`PENDING` orders are visible to the admin and can still be checked out.

`PAID` orders have their payment and change recorded.

`CANCELLED` orders were replaced by a newer order before checkout.

## Data files

All persistent data is stored in the `data/` folder.

| File | Purpose | Format |
|---|---|---|
| `data/menu.txt` | Menu items editable by the admin | `CATEGORY|name|price` |
| `data/Customer_Records.txt` | Customer accounts | `ID|Name|Contact` |
| `data/Reservations.txt` | Customer reservations | `customerId|date|time|guests` |
| `data/orders.txt` | Orders and their statuses | `orderId|customerId|status|total|payment|change|summary` |
| `receipts/Customer Receipt <ID>.txt` | Receipt generated during checkout | Human-readable text |

The system uses text-file storage instead of a database.

## Seed data

The system initializes the following customer record when `data/Customer_Records.txt` is missing or empty:

- Customer ID: `25`
- Name: `Abe`
- Contact: `239120`

Record format:

```text
25|Abe|239120
```

The default menu is also generated when its data file is missing.

Orders and reservations can be created by customers during use. The project can be prepared with empty order and reservation files and an empty receipts folder for a fresh demonstration.

## Reset a demo run

Before resetting, close the application and back up any data you want to keep.

To prepare a fresh demonstration:

- Keep only the original seed customer in `data/Customer_Records.txt`.
- Clear the contents of `data/orders.txt`.
- Clear the contents of `data/Reservations.txt`.
- Delete old receipt files inside `receipts/`.
- Keep `data/menu.txt` to preserve the current menu.

If `data/Customer_Records.txt` is missing or empty, the seed customer is generated again.

If `data/menu.txt` is missing, the default menu is generated again.

**Note:** Resetting orders and reservations does not reset menu changes. Restore the default menu separately if needed.

## Source layout

```text
CPP No Integration/
│
├── src/
│   ├── main.cpp
│   ├── Console.h
│   ├── Console.cpp
│   │
│   ├── CustomerRecord.h
│   ├── CustomerRecord.cpp
│   ├── CustomerRepository.h
│   ├── CustomerRepository.cpp
│   ├── CustomerRecordService.h
│   ├── CustomerRecordService.cpp
│   │
│   ├── MenuItem.h
│   ├── MenuItem.cpp
│   ├── Menu.h
│   ├── Menu.cpp
│   │
│   ├── Reservation.h
│   ├── Reservation.cpp
│   ├── ReservationRepository.h
│   ├── ReservationRepository.cpp
│   ├── ReservationService.h
│   ├── ReservationService.cpp
│   │
│   ├── OrderItem.h
│   ├── OrderItem.cpp
│   ├── Order.h
│   ├── Order.cpp
│   ├── OrderCalculator.h
│   ├── OrderCalculator.cpp
│   ├── OrderRepository.h
│   ├── OrderRepository.cpp
│   ├── OrderService.h
│   ├── OrderService.cpp
│   │
│   ├── BillingService.h
│   ├── BillingService.cpp
│   ├── ReceiptRenderer.h
│   ├── ReceiptRenderer.cpp
│   │
│   ├── CustomerPortal.h
│   ├── CustomerPortal.cpp
│   ├── AdminService.h
│   └── AdminService.cpp
│
├── data/
│   ├── menu.txt
│   ├── Customer_Records.txt
│   ├── Reservations.txt
│   └── orders.txt
│
├── receipts/
│
├── run.bat
├── run.sh
└── README.md
```

## Technologies

- **Language:** C++
- **Standard:** C++17
- **Compiler:** GCC / G++
- **Development Environment:** Visual Studio Code
- **Storage:** Text files
- **Interface:** Console-based application

## Conclusion

The C++ No Integration project is a standalone console-based restaurant ordering system. It implements customer account management, menu management, reservations, order processing, billing, and receipt generation using C++17 and file handling.