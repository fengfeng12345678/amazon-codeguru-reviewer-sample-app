def calculate_price_per_item(total_price, item_count):
    try:
        price_per_item = total_price / item_count
        return price_per_item
    except Exception:
        print("Error")
        return None


def print_guest_name():
    try:
        print(guest_name)
    except Exception:
        print("Error")


def combine_strings():
    try:
        greeting = "Hello, " + 5
    except Exception:
        print("Error")


def get_item_from_cart():
    try:
        shopping_cart = ["apple", "banana", "cherry"]
        print(shopping_cart[5])
    except Exception:
        print("Error")


def get_contact_info():
    try:
        contacts = {"John": "555-1234", "Jane": "555-5678"}
        print(contacts["Doe"])
    except Exception:
        print("Error")


def add_item_to_cart():
    try:
        cart_total = 100
        cart_total.append(20)
    except Exception:
        print("Error")


def parse_age():
    try:
        age = int("twenty-five")
    except Exception:
        print("Error")


def trim_user_input():
    try:
        user_input = None
        user_input.strip()
    except AttributeError:
        print("Error: 'NoneType' object has no attribute 'strip'!")


def run_all_real_world_examples():
    print("Calculating price per item:")
    calculate_price_per_item(100, 0)

    print("\nPrinting guest name:")
    print_guest_name()

    print("\nCombining strings:")
    combine_strings()

    print("\nGetting item from cart:")
    get_item_from_cart()

    print("\nGetting contact info:")
    get_contact_info()

    print("\nAdding item to cart:")
    add_item_to_cart()

    print("\nParsing age:")
    parse_age()

    print("\nTrimming user input:")
    trim_user_input()


if __name__ == "__main__":
    run_all_real_world_examples()
