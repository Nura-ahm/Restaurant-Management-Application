package restaurant.ui;

import restaurant.security.PasswordHasher;
import restaurant.store.Config;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;

/**
 * Manager sign-in. The typed password is hashed and compared with the
 * configured hash, then wiped from memory.
 */
public class ManagerLoginFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private final JTextField usernameField = new JTextField(14);
    private final JPasswordField passwordField = new JPasswordField(14);
    private final JLabel message = new JLabel(" ");
    private final JButton signInButton = Theme.button("Sign in");

    public ManagerLoginFrame() {
        setTitle("Manager sign in");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(420, 260);
        setLocationRelativeTo(null);
        setContentPane(buildContent());
        getRootPane().setDefaultButton(signInButton);
    }

    private JPanel buildContent() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Theme.MAROON);
        panel.setBorder(Theme.PADDING);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(7, 7, 7, 7);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        panel.add(Theme.display("Staff only"), c);

        c.gridwidth = 1;
        c.gridy++;
        panel.add(Theme.heading("Username"), c);
        c.gridx = 1;
        panel.add(usernameField, c);

        c.gridx = 0;
        c.gridy++;
        panel.add(Theme.heading("Password"), c);
        c.gridx = 1;
        panel.add(passwordField, c);

        signInButton.addActionListener(event -> attemptSignIn());
        JButton back = Theme.button("Back");
        back.addActionListener(event -> dispose());

        c.gridx = 0;
        c.gridy++;
        panel.add(back, c);
        c.gridx = 1;
        panel.add(signInButton, c);

        message.setForeground(new Color(0xFF, 0xD5, 0xD5));
        message.setFont(Theme.BODY);
        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 2;
        panel.add(message, c);

        return panel;
    }

    private void attemptSignIn() {
        char[] typed = passwordField.getPassword();
        try {
            boolean ok = usernameField.getText().trim().equalsIgnoreCase(Config.managerUsername())
                    && PasswordHasher.matches(typed, Config.managerPasswordHash());

            if (ok) {
                new OrdersListFrame().setVisible(true);
                dispose();
            } else {
                message.setText("Those details don't match an account.");
                passwordField.setText("");
            }
        } catch (RuntimeException e) {
            // A missing or malformed password hash in the config throws here.
            // Without this the exception escapes onto the event dispatch thread
            // and the button simply appears to do nothing.
            JOptionPane.showMessageDialog(this,
                    "Sign in failed: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            Arrays.fill(typed, '\0');
        }
    }
}
