package restaurant.ui;

import restaurant.model.Order;
import restaurant.store.Config;
import restaurant.store.OrderStore;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * The manager's view of the order queue, read back from the orders file.
 *
 * <p>The original version printed orders to the console; here they appear in
 * the window, newest first, with the file path shown so staff know where the
 * queue lives.</p>
 */
public class OrdersListFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private final DefaultListModel<String> model = new DefaultListModel<>();
    private final JList<String> list = new JList<>(model);
    private final JLabel status = Theme.body(" ");
    private final OrderStore orderStore;

    public OrdersListFrame() {
        this(new OrderStore(Path.of(Config.ordersFile())));
    }

    public OrdersListFrame(OrderStore orderStore) {
        this.orderStore = orderStore;
        setTitle("Orders");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(760, 440);
        setLocationRelativeTo(null);
        setContentPane(buildContent());
        refresh();
    }

    private JPanel buildContent() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(Theme.MAROON);
        panel.setBorder(Theme.PADDING);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(Theme.display("Orders"), BorderLayout.WEST);
        header.add(status, BorderLayout.EAST);
        panel.add(header, BorderLayout.NORTH);

        list.setFont(Theme.BODY);
        panel.add(new JScrollPane(list), BorderLayout.CENTER);

        JButton refresh = Theme.button("Refresh");
        refresh.addActionListener(event -> refresh());
        JButton back = Theme.button("Main menu");
        back.addActionListener(event -> dispose());

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);
        actions.add(back);
        actions.add(refresh);
        panel.add(actions, BorderLayout.SOUTH);

        return panel;
    }

    /** Reloads the file and shows the newest order first. */
    private void refresh() {
        try {
            List<Order> orders = orderStore.readAll();
            model.clear();
            orders.stream()
                    .sorted((left, right) -> right.placedAt().compareTo(left.placedAt()))
                    .map(Order::summary)
                    .forEach(model::addElement);

            status.setText(orders.isEmpty()
                    ? "No orders yet · " + orderStore.file()
                    : orders.size() + (orders.size() == 1 ? " order · " : " orders · ") + orderStore.file());
        } catch (IOException e) {
            // Clear the list as well as reporting the failure. Leaving the old
            // rows and the old count on screen behind the dialog would show the
            // manager a queue that looks current and is not.
            model.clear();
            status.setText("Could not read " + orderStore.file());
            JOptionPane.showMessageDialog(this,
                    "Could not read the orders file: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
