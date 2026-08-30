package restaurant.ui;

import restaurant.model.Order;
import restaurant.store.Config;
import restaurant.store.OrderStore;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * The customer's menu. Pick a course from each list, choose dine-in or take
 * out, and the order is appended to the orders file.
 */
public class OrderFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final List<String> APPETIZERS = List.of("Chicken fingers", "Spring rolls", "Mantı");
    private static final List<String> SOUPS = List.of("Chicken soup", "Corn soup", "Mixed vegetable soup");
    private static final List<String> MAINS = List.of("BBQ meats", "Burgers", "Pasta", "Fried rice");
    private static final List<String> DRINKS = List.of("Soft drink", "Water", "Wine", "Fresh juice");
    private static final List<String> DESSERTS = List.of(
            "Cheesecake", "Lemon cake", "Cookies", "Tiramisu", "Waffles", "Crêpes", "Fruit");

    private final JComboBox<String> appetizerBox = comboBox(APPETIZERS);
    private final JComboBox<String> soupBox = comboBox(SOUPS);
    private final JComboBox<String> mainBox = comboBox(MAINS);
    private final JComboBox<String> drinkBox = comboBox(DRINKS);
    private final JComboBox<String> dessertBox = comboBox(DESSERTS);
    private final JRadioButton dineIn = new JRadioButton("Dining in", true);
    private final JRadioButton takeOut = new JRadioButton("Take out");

    private final OrderStore orderStore;

    public OrderFrame() {
        this(new OrderStore(Path.of(Config.ordersFile())));
    }

    /** Lets a test write to its own file. */
    public OrderFrame(OrderStore orderStore) {
        this.orderStore = orderStore;
        setTitle("Place an order");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(480, 440);
        setLocationRelativeTo(null);
        setContentPane(buildContent());
    }

    private JPanel buildContent() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Theme.MAROON);
        panel.setBorder(Theme.PADDING);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(7, 7, 7, 7);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        panel.add(Theme.display("Menu"), c);

        c.gridwidth = 1;
        addRow(panel, c, "Appetizer", appetizerBox);
        addRow(panel, c, "Soup", soupBox);
        addRow(panel, c, "Main course", mainBox);
        addRow(panel, c, "Drink", drinkBox);
        addRow(panel, c, "Dessert", dessertBox);

        ButtonGroup dining = new ButtonGroup();
        dining.add(dineIn);
        dining.add(takeOut);
        JPanel diningRow = new JPanel();
        diningRow.setBackground(Theme.MAROON);
        for (JRadioButton option : List.of(dineIn, takeOut)) {
            option.setBackground(Theme.MAROON);
            option.setForeground(Theme.CREAM);
            option.setFont(Theme.BODY);
            diningRow.add(option);
        }
        addRow(panel, c, "Dining option", diningRow);

        JButton back = Theme.button("Back");
        back.addActionListener(event -> dispose());
        JButton send = Theme.button("Send order");
        send.addActionListener(event -> sendOrder());

        c.gridx = 0;
        c.gridy++;
        panel.add(back, c);
        c.gridx = 1;
        panel.add(send, c);

        return panel;
    }

    private void addRow(JPanel panel, GridBagConstraints c, String label, java.awt.Component field) {
        c.gridx = 0;
        c.gridy++;
        panel.add(Theme.heading(label), c);
        c.gridx = 1;
        panel.add(field, c);
    }

    private void sendOrder() {
        Order order = Order.placedNow(
                selected(appetizerBox),
                selected(soupBox),
                selected(mainBox),
                selected(drinkBox),
                selected(dessertBox),
                dineIn.isSelected() ? Order.DiningOption.DINE_IN : Order.DiningOption.TAKE_OUT);

        try {
            orderStore.append(order);
            JOptionPane.showMessageDialog(this,
                    "Order sent to the kitchen. Thank you!",
                    "Order received", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Could not save the order: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static String selected(JComboBox<String> box) {
        return String.valueOf(box.getSelectedItem());
    }

    private static JComboBox<String> comboBox(List<String> options) {
        JComboBox<String> box = new JComboBox<>(new DefaultComboBoxModel<>(options.toArray(String[]::new)));
        box.setFont(Theme.BODY);
        return box;
    }
}
