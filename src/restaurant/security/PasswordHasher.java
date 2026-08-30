package restaurant.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Hashes passwords so that no plain-text credential is ever compared, stored
 * or committed.
 *
 * <p>SHA-256 is used here because it needs no third-party library and keeps
 * this coursework project dependency-free. A production system should use a
 * slow, salted algorithm such as bcrypt or Argon2 instead.</p>
 */
public final class PasswordHasher {

    private PasswordHasher() {
    }

    /** Returns the lowercase hex SHA-256 hash of the given text. */
    public static String sha256(char[] password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(new String(password).getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is required but unavailable", e);
        }
    }

    /**
     * Compares a typed password against a stored hash in constant time, so the
     * comparison itself gives nothing away.
     */
    public static boolean matches(char[] password, String expectedHash) {
        String actual = sha256(password);
        return MessageDigest.isEqual(
                actual.getBytes(StandardCharsets.UTF_8),
                expectedHash.trim().toLowerCase().getBytes(StandardCharsets.UTF_8));
    }

    /** Prints the hash of a password, so a new one can be put in app.properties. */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java restaurant.security.PasswordHasher <password>");
            return;
        }
        System.out.println(sha256(args[0].toCharArray()));
    }
}
