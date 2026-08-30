package restaurant.ui;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.Dimension;

/**
 * The front door of the app: customers place an order, managers sign in to
 * read the queue.
 */
public class MainScreen extends JFrame {

    private static final long serialVersionUID = 1L;

    public MainScreen() {
        setTitle("Goro Garden Restaurant");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(520, 400);
        setLocationRelativeTo(null);
        setContentPane(buildContent());
    }

    private JPanel buildContent() {
        JPanel panel = new JPanel();
        panel.setBackground(Theme.MAROON);
        panel.setBorder(Theme.PADDING);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(centred(Theme.display("Goro Garden")));
        panel.add(Box.createVerticalStrut(4));
        panel.add(centred(Theme.body("Kitchen open · 11:00 – 23:00")));
        panel.add(Box.createVerticalGlue());

        JButton order = Theme.button("Place an order");
        order.addActionListener(event -> new OrderFrame().setVisible(true));
        panel.add(fullWidth(order));
        panel.add(Box.createVerticalStrut(12));

        JButton check = Theme.button("Check orders");
        check.addActionListener(event -> new ManagerLoginFrame().setVisible(true));
        panel.add(fullWidth(check));

        return panel;
    }

    private static JComponent centred(JComponent component) {
        component.setAlignmentX(Component.CENTER_ALIGNMENT);
        return component;
    }

    private static JComponent fullWidth(JComponent component) {
        component.setAlignmentX(Component.CENTER_ALIGNMENT);
        component.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        return component;
    }
}
