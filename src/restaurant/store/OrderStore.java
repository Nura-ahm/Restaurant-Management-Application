package restaurant.store;

import restaurant.model.Order;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores orders in a plain text file, one order per line.
 *
 * <p>The path is relative to wherever the application is started, so the app
 * runs the same on any machine — the original version pointed at one specific
 * Windows desktop.</p>
 */
public class OrderStore {

    private static final Path DEFAULT_FILE = Path.of("orders.txt");

    private final Path file;

    public OrderStore() {
        this(DEFAULT_FILE);
    }

    /** Lets tests point the store at a temporary file. */
    public OrderStore(Path file) {
        this.file = file;
    }

    public Path file() {
        return file.toAbsolutePath();
    }

    /** Appends one order, creating the file the first time. */
    public void append(Order order) throws IOException {
        Files.writeString(file,
                order.toLine() + System.lineSeparator(),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
    }

    /**
     * Reads every order back. Lines that can't be parsed are skipped rather
     * than crashing the manager's screen.
     */
    public List<Order> readAll() throws IOException {
        if (!Files.exists(file)) {
            return List.of();
        }

        List<Order> orders = new ArrayList<>();
        for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
            if (line.isBlank()) {
                continue;
            }
            try {
                orders.add(Order.fromLine(line));
            } catch (RuntimeException e) {
                System.err.println("Skipping unreadable order: " + line);
            }
        }
        return orders;
    }
}
