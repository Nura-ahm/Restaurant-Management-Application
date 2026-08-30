# Restaurant Management Application

Front-of-house software for a small restaurant, with two sides to it.
Customers build an order from the menu and choose dine-in or take-out;
managers sign in separately to read the order queue back.

Built with **Java 17** and **Swing**. Orders are stored in a plain text file
through `java.nio`, so there's no database to install — clone it, compile it,
run it.

---

## What it does

**For the customer**

- Pick an appetizer, soup, main course, drink and dessert from the menu.
- Choose dining in or taking out.
- Sending the order appends it to the order queue, timestamped.

**For the manager**

- Sign-in screen; the password is checked against a SHA-256 hash rather than a
  string in the source.
- The full order queue, newest first, with the location of the queue file
  shown so staff know where it lives.
- A refresh button, because the kitchen screen stays open all shift.

---

## Running it

You'll need a JDK, 17 or newer. Nothing else.

```bash
# Compile
javac -d out $(find src -name '*.java')

# Run
java -cp out restaurant.App
```

Orders are written to `orders.txt` in the working directory. To change the
manager password:

```bash
java -cp out restaurant.security.PasswordHasher "my new password"
```

Then copy `app.properties.example` to `app.properties` and paste the hash in.
The shipped default is the hash of `changeme`.

---

## How it's put together

```
src/restaurant/
├── App.java                      entry point
├── model/Order.java              one order; owns its own file format
├── store/
│   ├── Config.java               settings from app.properties or the environment
│   └── OrderStore.java           append and read the order queue
├── security/PasswordHasher.java  SHA-256 hashing, constant-time comparison
└── ui/
    ├── Theme.java                the restaurant's colours and type
    ├── MainScreen.java           customer / manager split
    ├── OrderFrame.java           the menu
    ├── ManagerLoginFrame.java    staff sign-in
    └── OrdersListFrame.java      the order queue
```

`Order` is a record that knows how to write itself as one line and parse
itself back, so the two halves of the file format sit side by side and can't
drift apart. `OrderStore` owns the file; the screens never touch it directly.
A line that can't be parsed is skipped and logged rather than taking down the
manager's screen.

---

## Notes

This began as a university coursework project and has since been rebuilt.
Along the way a few real bugs came out of it:

- The order was never actually added to the list before being written, so
  **the file always came out empty**. It now builds an `Order` from the form.
- The file path was hard-coded to one Windows desktop
  (`C:/Users/hp/OneDrive/Desktop/menuorders`); it's now relative and
  configurable.
- The manager's "Get orders" button printed to the console instead of the
  window. Orders now appear in the UI.
- The manager password sat in plain text in the source. It's a hash in config
  now.

## Author

Nura M. Ahmed — [nura-ahm.github.io](https://nura-ahm.github.io)
