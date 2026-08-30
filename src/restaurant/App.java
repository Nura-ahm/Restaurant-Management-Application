package restaurant;

import restaurant.ui.MainScreen;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/** Entry point for the restaurant application. */
public final class App {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Falling back to the default look and feel is fine.
            }
            new MainScreen().setVisible(true);
        });
    }

    private App() {
    }
}
