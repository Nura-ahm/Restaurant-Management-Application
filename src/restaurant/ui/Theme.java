package restaurant.ui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.Font;

/** The restaurant's colours and type, in one place. */
final class Theme {

    static final Color MAROON = new Color(0x80, 0x00, 0x00);
    static final Color SALMON = new Color(0xE9, 0x96, 0x7A);
    static final Color DARK_RED = new Color(0x8B, 0x00, 0x00);
    static final Color CREAM = new Color(0xF5, 0xF5, 0xF5);

    static final Font DISPLAY = new Font("Serif", Font.BOLD | Font.ITALIC, 26);
    static final Font HEADING = new Font("SansSerif", Font.BOLD, 15);
    static final Font BODY = new Font("SansSerif", Font.PLAIN, 13);

    static final Border PADDING = BorderFactory.createEmptyBorder(20, 24, 20, 24);

    private Theme() {
    }

    static JLabel display(String text) {
        JLabel label = new JLabel(text);
        label.setFont(DISPLAY);
        label.setForeground(Color.WHITE);
        return label;
    }

    static JLabel heading(String text) {
        JLabel label = new JLabel(text);
        label.setFont(HEADING);
        label.setForeground(Color.WHITE);
        return label;
    }

    static JLabel body(String text) {
        JLabel label = new JLabel(text);
        label.setFont(BODY);
        label.setForeground(CREAM);
        return label;
    }

    static JButton button(String text) {
        JButton button = new JButton(text);
        button.setFont(HEADING);
        button.setBackground(SALMON);
        button.setForeground(DARK_RED);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }
}
