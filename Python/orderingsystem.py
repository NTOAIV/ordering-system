
# FOOD ORDERING SYSTEM

menu = {
    1: {"name": "Burger", "price": 85},
    2: {"name": "Pizza", "price": 150},
    3: {"name": "Fries", "price": 55},
    4: {"name": "Fried Chicken", "price": 120},
    5: {"name": "Spaghetti", "price": 75},
    6: {"name": "Iced Tea", "price": 35}
}

cart = []

print("=" * 40)
print("       FOOD ORDERING SYSTEM")
print("=" * 40)

customer = input("Enter Customer Name: ")

while True:
    print("\n============== MENU ==============")
    print("No.  Food Item          Price")
    print("----------------------------------")

    for number, item in menu.items():
        print(f"{number}.   {item['name']:<18} PHP {item['price']}")

    print("0.   Checkout")
    print("==================================")

    try:
        choice = int(input("\nSelect an item (0 to checkout): "))

        if choice == 0:
            break

        if choice not in menu:
            print("Invalid selection! Please try again.")
            continue

        quantity = int(input("Enter quantity: "))

        if quantity <= 0:
            print("Quantity must be greater than zero.")
            continue

        selected_item = menu[choice]

        cart.append({
            "name": selected_item["name"],
            "price": selected_item["price"],
            "quantity": quantity,
            "total": selected_item["price"] * quantity
        })

        print(f"{selected_item['name']} added to cart!")

    except ValueError:
        print("Please enter a valid number.")

# CHECKOUT
if not cart:
    print("\nNo items ordered.")
    print("Thank you for visiting!")

else:
    subtotal = sum(item["total"] for item in cart)

    discount = 0

    if subtotal >= 500:
        discount = subtotal * 0.10

    total = subtotal - discount

    print("\n" + "=" * 45)
    print("                 RECEIPT")
    print("=" * 45)
    print("Customer:", customer)
    print("-" * 45)

    print(f"{'Item':<18}{'Qty':<6}{'Price':<10}{'Total'}")
    print("-" * 45)

    for item in cart:
        print(
            f"{item['name']:<18}"
            f"{item['quantity']:<6}"
            f"{item['price']:<10.2f}"
            f"{item['total']:.2f}"
        )

    print("-" * 45)
    print(f"Subtotal:          PHP {subtotal:.2f}")
    print(f"Discount:          PHP {discount:.2f}")
    print(f"Total Amount:      PHP {total:.2f}")

    while True:
        try:
            payment = float(input("\nEnter payment amount: PHP "))

            if payment < total:
                print("Insufficient payment. Please try again.")
            else:
                break

        except ValueError:
            print("Invalid payment amount.")

    change = payment - total

    print(f"Payment:           PHP {payment:.2f}")
    print(f"Change:            PHP {change:.2f}")

    print("=" * 45)
    print("       THANK YOU FOR YOUR ORDER!")
    print("=" * 45)
