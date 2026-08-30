package restaurant.store;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Application settings, read from {@code app.properties} in the working
 * directory and overridable by environment variables, so no credential ever
 * needs to live in the source.
 */
public final class Config {

    private static final String FILE_NAME = "app.properties";
    private static final Properties PROPERTIES = load();

    private Config() {
    }

    public static String managerUsername() {
        return get("manager.username", "MANAGER_USERNAME", "manager");
    }

    /** SHA-256 of the manager password, hex encoded. Default is "changeme". */
    public static String managerPasswordHash() {
        return get("manager.password.sha256", "MANAGER_PASSWORD_SHA256",
                "057ba03d6c44104863dc7361fe4578965d1887360f90a0895882e58a6248fc86");
    }

    /** Where orders are stored, relative to the working directory. */
    public static String ordersFile() {
        return get("orders.file", "ORDERS_FILE", "orders.txt");
    }

    private static String get(String key, String environmentVariable, String fallback) {
        String fromEnvironment = System.getenv(environmentVariable);
        if (fromEnvironment != null && !fromEnvironment.isBlank()) {
            return fromEnvironment;
        }
        return PROPERTIES.getProperty(key, fallback);
    }

    private static Properties load() {
        Properties properties = new Properties();
        Path file = Path.of(FILE_NAME);
        if (Files.exists(file)) {
            try (InputStream in = Files.newInputStream(file)) {
                properties.load(in);
            } catch (IOException e) {
                System.err.println("Could not read " + FILE_NAME + ": " + e.getMessage());
            }
        }
        return properties;
    }
}
