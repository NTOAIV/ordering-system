# Java No Integration

A standalone, **Java-only** version of the restaurant ordering system. It has
separate Customer and Admin portals, a menu the admin can edit, customer
accounts, reservations, orders, and checkout receipts.

There is no Python or C++ integration in this folder. Every part of the system
runs in Java.

## How to run

### Windows

```bat
cd "Java No Integration"
run.bat
```

### Linux / macOS

```bash
cd "Java No Integration"
chmod +x run.sh
./run.sh
```

Manual equivalent:

```bat
javac -d out src\*.java
java -cp out ordering.Main
```

## Portals

### Customer portal

| Option | What it does |
|---|---|
| `[1] Register Account` | Creates a customer account with an ID, name, and contact number. |
| `[2] Update Account Details` | Updates the name and contact number only. Reservations are separate. |
| `[3] View Menu & Place Order` | Browses the current menu and builds one order. Maximum 5 items total. |
| `[4] Set Reservation` | Sets or replaces the reservation for the signed-in customer ID. |
| `[5] Check Out` | Shows the amount due, accepts payment, prints a receipt, and marks the order `PAID`. |
| `[X] Exit System` | Leaves the portal. An unpaid order stays visible to the admin. |

Customers cannot view the order history and cannot edit the menu.

### Admin portal

| Option | What it does |
|---|---|
| `[1] View Orders & Reservations` | Shows every order (paid, pending, or cancelled) and every reservation. |
| `[2] View Customer Accounts` | Read-only list of customer IDs, names, and contacts. |
| `[3] Manage Menu` | View, add, update, and delete menu items. Changes are saved to `menu.txt`. |
| `[X] Exit System` | Leaves the portal. |

The admin cannot place orders, edit customer accounts, or edit reservations.

## Menu management

Inside `[3] Manage Menu`:

| Option | What it does |
|---|---|
| `[1] View Menu` | Shows every category and item with its price. |
| `[2] Add Menu Item` | Adds an item to Meals, Drinks, Desserts, Side Dishes, or Bundles. |
| `[3] Update Menu Item` | Renames an item and/or changes its price. Blank input keeps the old value. |
| `[4] Delete Menu Item` | Deletes an item after a `[Y/N]` confirmation. |

Menu changes are written to `menu.txt`, so they are still there after a restart.
If `menu.txt` is missing, the default menu is created automatically.

## Checkout behaviour

- The customer must have a pending order. If there is nothing to check out, the
  program returns to the customer portal instead of getting stuck.
- At the payment prompt, enter `[X]` to cancel and return to the portal.
- A successful payment writes `Customer Receipt <ID>.txt` and marks the order
  `PAID` in `orders.txt`.
- Placing a new order cancels the customer's older pending order, so each
  customer has only one open order at a time.
- A pending order can be checked out later, even in a new session. The receipt
  then uses the stored item summary.

## Order lifecycle

```text
PENDING --> PAID
PENDING --> CANCELLED   (replaced by a newer order)
```

`PENDING` orders are visible to the admin and can still be checked out.
`PAID` orders have payment and change recorded. `CANCELLED` orders were replaced
before checkout.

## Data files

| File | Purpose | Format |
|---|---|---|
| `menu.txt` | Menu items editable by the admin | `CATEGORY\|name\|price` |
| `Customer_Records.txt` | Customer accounts | `ID\|Name\|Contact` |
| `Reservations.txt` | One reservation per customer | `customerId\|date\|time\|guests` |
| `orders.txt` | All orders and their status | `orderId\|customerId\|status\|total\|payment\|change\|summary` |
| `Customer Receipt <ID>.txt` | Receipt written at checkout | Human-readable |

Default menu categories are `MEALS`, `DRINKS`, `DESSERTS`, `SIDES`, and
`BUNDLES`.

## Seed data

The folder ships with:

- Customer `25` (`Abe`)
- One reservation for Abe
- One paid order (`TXN-1001`)
- The default menu

## Reset a demo run

From inside this folder, delete the runtime files you want to reset:

```bat
del orders.txt
del Reservations.txt
del "Customer Receipt 25.txt"
```

If you delete `menu.txt`, it is recreated with the default menu. If you delete
`Customer_Records.txt`, it is recreated with the seed customer.

## Source layout

```text
src/
├── Main.java                  # role selection and startup
├── Console.java               # shared input helpers
├── Menu.java                  # persistent menu + default items
├── MenuItem.java              # one menu item
├── CustomerRecord.java        # one customer account
├── CustomerRepository.java    # reads/writes Customer_Records.txt
├── CustomerRecordService.java # register/update/view accounts
├── Reservation.java           # one reservation
├── ReservationRepository.java # reads/writes Reservations.txt
├── ReservationService.java    # set a reservation
├── Order.java                 # order + status lifecycle
├── OrderItem.java             # one order line
├── OrderCalculator.java       # subtotal and rounding
├── OrderRepository.java       # reads/writes orders.txt
├── OrderService.java          # customer menu browsing and ordering
├── BillingService.java        # checkout and payment
├── ReceiptRenderer.java       # receipts and admin tables
├── CustomerPortal.java        # customer portal
└── AdminService.java          # admin portal and menu management
```
