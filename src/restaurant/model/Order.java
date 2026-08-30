package restaurant.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * One customer order: the courses they chose, how they want to eat, and when
 * it was placed.
 *
 * <p>Orders are written to and read back from a plain text file, so the class
 * owns both halves of that format ({@link #toLine()} and
 * {@link #fromLine(String)}). Keeping them together means the two can never
 * drift apart.</p>
 */
public record Order(String appetizer,
                    String soup,
                    String mainCourse,
                    String drink,
                    String dessert,
                    DiningOption diningOption,
                    LocalDateTime placedAt) {

    private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final String SEPARATOR = " | ";

    /** How the customer wants the order served. */
    public enum DiningOption {
        DINE_IN("Dining in"),
        TAKE_OUT("Take out");

        private final String label;

        DiningOption(String label) {
            this.label = label;
        }

        public String label() {
            return label;
        }

        public static DiningOption fromLabel(String label) {
            for (DiningOption option : values()) {
                if (option.label.equalsIgnoreCase(label)) {
                    return option;
                }
            }
            throw new IllegalArgumentException("Unknown dining option: " + label);
        }
    }

    /** Builds an order stamped with the current time. */
    public static Order placedNow(String appetizer,
                                  String soup,
                                  String mainCourse,
                                  String drink,
                                  String dessert,
                                  DiningOption diningOption) {
        return new Order(appetizer, soup, mainCourse, drink, dessert,
                diningOption, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
    }

    /** Serialises the order as a single line of the orders file. */
    public String toLine() {
        return String.join(SEPARATOR,
                TIMESTAMP.format(placedAt),
                appetizer, soup, mainCourse, drink, dessert,
                diningOption.label());
    }

    /** Parses a line previously written by {@link #toLine()}. */
    public static Order fromLine(String line) {
        String[] parts = line.split("\\s*\\|\\s*");
        if (parts.length != 7) {
            throw new IllegalArgumentException("Malformed order line: " + line);
        }
        return new Order(parts[1], parts[2], parts[3], parts[4], parts[5],
                DiningOption.fromLabel(parts[6]),
                LocalDateTime.parse(parts[0], TIMESTAMP));
    }

    /** A human-readable summary for the manager's list. */
    public String summary() {
        return TIMESTAMP.format(placedAt).replace('T', ' ')
                + "  ·  " + diningOption.label()
                + "  ·  " + String.join(", ", appetizer, soup, mainCourse, drink, dessert);
    }
}
