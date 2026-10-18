package GUI;

import Connections.MyJDBC;
import Modules.Customer;
import Modules.Food;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class FoodStoreSwing extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);
    private final Map<Integer, Integer> customerCart = new LinkedHashMap<>();

    public FoodStoreSwing() {
        setTitle("Quick Bite - Food Store");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        cards.add(buildLoginPanel(), "login");
        cards.add(buildAdminPanel(), "admin");
        cards.add(buildCustomerPanel(), "customer");

        setContentPane(cards);
        cardLayout.show(cards, "login");
    }

    private JPanel buildLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 245, 238));

        JLabel title = new JLabel("Quick Bite", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setBorder(BorderFactory.createEmptyBorder(25, 0, 20, 0));

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField usernameField = new JTextField(18);
        JPasswordField passwordField = new JPasswordField(18);

        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; form.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; form.add(passwordField, gbc);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttons.setOpaque(false);

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        JButton exitButton = new JButton("Exit");

        buttons.add(loginButton);
        buttons.add(registerButton);
        buttons.add(exitButton);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter username and password.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String role = MyJDBC.login(username, password);
            if (role == null) {
                JOptionPane.showMessageDialog(this, "Invalid username or password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if ("Admin".equals(role)) {
                cardLayout.show(cards, "admin");
            } else if ("Customer".equals(role)) {
                cardLayout.show(cards, "customer");
            }

            usernameField.setText("");
            passwordField.setText("");
        });

        registerButton.addActionListener(e -> handleRegistration());
        exitButton.addActionListener(e -> System.exit(0));

        panel.add(title, BorderLayout.NORTH);
        panel.add(form, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel buildAdminPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(247, 250, 255));

        JLabel title = new JLabel("Admin Dashboard");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));

        JButton logout = new JButton("Logout");
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(title, BorderLayout.WEST);
        top.add(logout, BorderLayout.EAST);

        JPanel foodManagementPanel = new JPanel(new BorderLayout(10, 10));
        foodManagementPanel.setBackground(new Color(247, 250, 255));

        JButton refreshFoods = new JButton("Refresh Food");
        JButton addFood = new JButton("Add Food");
        JButton updateFood = new JButton("Update Food");
        JButton deleteFood = new JButton("Delete Food");

        JPanel foodActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        foodActions.setOpaque(false);
        foodActions.add(refreshFoods);
        foodActions.add(addFood);
        foodActions.add(updateFood);
        foodActions.add(deleteFood);

        JTable foodTable = new JTable(buildFoodTableModel());
        JScrollPane foodScroll = new JScrollPane(foodTable);
        foodManagementPanel.add(foodActions, BorderLayout.NORTH);
        foodManagementPanel.add(foodScroll, BorderLayout.CENTER);

        JPanel userManagementPanel = new JPanel(new BorderLayout(10, 10));
        userManagementPanel.setBackground(new Color(247, 250, 255));

        JButton refreshCustomers = new JButton("Refresh Users");
        JButton addCustomer = new JButton("Add User");
        JButton updateCustomer = new JButton("Update User");
        JButton deleteCustomer = new JButton("Delete User");

        JPanel userActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        userActions.setOpaque(false);
        userActions.add(refreshCustomers);
        userActions.add(addCustomer);
        userActions.add(updateCustomer);
        userActions.add(deleteCustomer);

        JTable customerTable = new JTable(buildCustomerTableModel());
        JScrollPane customerScroll = new JScrollPane(customerTable);
        userManagementPanel.add(userActions, BorderLayout.NORTH);
        userManagementPanel.add(customerScroll, BorderLayout.CENTER);

        JTabbedPane adminTabs = new JTabbedPane();
        adminTabs.addTab("Manage Food", foodManagementPanel);
        adminTabs.addTab("Manage Users", userManagementPanel);

        refreshFoods.addActionListener(e -> loadFoodTable(foodTable));
        refreshCustomers.addActionListener(e -> loadCustomerTable(customerTable));

        addFood.addActionListener(e -> {
            FoodForm form = promptForFood(null);
            if (form == null) return;
            if (MyJDBC.addFood(new Food(0, form.name, form.price, form.quantity))) {
                JOptionPane.showMessageDialog(this, "Food added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadFoodTable(foodTable);
            } else {
                JOptionPane.showMessageDialog(this, "Unable to add food.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        updateFood.addActionListener(e -> {
            int selected = foodTable.getSelectedRow();
            if (selected < 0) {
                JOptionPane.showMessageDialog(this, "Please select a food item first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int foodId = Integer.parseInt(foodTable.getValueAt(selected, 0).toString());
            String currentName = foodTable.getValueAt(selected, 1).toString();
            double currentPrice = Double.parseDouble(foodTable.getValueAt(selected, 2).toString());
            int currentQuantity = Integer.parseInt(foodTable.getValueAt(selected, 3).toString());

            FoodForm form = promptForFood(new Food(foodId, currentName, currentPrice, currentQuantity));
            if (form == null) return;

            Food updated = new Food(foodId, form.name, form.price, form.quantity);
            if (updated.updateFood()) {
                JOptionPane.showMessageDialog(this, "Food updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadFoodTable(foodTable);
            } else {
                JOptionPane.showMessageDialog(this, "Unable to update food.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteFood.addActionListener(e -> {
            int selected = foodTable.getSelectedRow();
            if (selected < 0) {
                JOptionPane.showMessageDialog(this, "Please select a food item first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int foodId = Integer.parseInt(foodTable.getValueAt(selected, 0).toString());
            if (MyJDBC.deleteFood(foodId)) {
                JOptionPane.showMessageDialog(this, "Food deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadFoodTable(foodTable);
            } else {
                JOptionPane.showMessageDialog(this, "Unable to delete food.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        addCustomer.addActionListener(e -> {
            CustomerInput input = promptForCustomer(null);
            if (input == null) return;
            Customer customer = new Customer(0, input.username, input.password);
            if (customer.addNewCustomer()) {
                JOptionPane.showMessageDialog(this, "Customer added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadCustomerTable(customerTable);
            } else {
                JOptionPane.showMessageDialog(this, "Unable to add customer.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        updateCustomer.addActionListener(e -> {
            int selected = customerTable.getSelectedRow();
            if (selected < 0) {
                JOptionPane.showMessageDialog(this, "Please select a customer first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int userId = Integer.parseInt(customerTable.getValueAt(selected, 0).toString());
            String username = customerTable.getValueAt(selected, 1).toString();
            String password = customerTable.getValueAt(selected, 2).toString();

            CustomerInput input = promptForCustomer(new Customer(userId, username, password));
            if (input == null) return;

            Customer updated = new Customer(userId, input.username, input.password);
            if (updated.updateCustomerDetails()) {
                JOptionPane.showMessageDialog(this, "Customer updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadCustomerTable(customerTable);
            } else {
                JOptionPane.showMessageDialog(this, "Unable to update customer.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteCustomer.addActionListener(e -> {
            int selected = customerTable.getSelectedRow();
            if (selected < 0) {
                JOptionPane.showMessageDialog(this, "Please select a customer first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int userId = Integer.parseInt(customerTable.getValueAt(selected, 0).toString());
            if (MyJDBC.deleteCustomer(userId)) {
                JOptionPane.showMessageDialog(this, "Customer deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadCustomerTable(customerTable);
            } else {
                JOptionPane.showMessageDialog(this, "Unable to delete customer.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        logout.addActionListener(e -> {
            customerCart.clear();
            cardLayout.show(cards, "login");
        });

        loadFoodTable(foodTable);
        loadCustomerTable(customerTable);

        panel.add(top, BorderLayout.NORTH);
        panel.add(adminTabs, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildCustomerPanel() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(255, 247, 240));

        JLabel title = new JLabel("Customer Menu");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));

        JButton refreshButton = new JButton("Refresh Menu");
        JButton logoutButton = new JButton("Logout");
        JButton addToCartButton = new JButton("Add To Cart");
        JButton removeButton = new JButton("Remove Item");
        JButton clearButton = new JButton("Clear Cart");
        JButton payButton = new JButton("Pay Now");

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        header.setOpaque(false);
        header.add(title);
        header.add(refreshButton);
        header.add(logoutButton);

        JTable menuTable = new JTable(buildFoodTableModel());
        JScrollPane menuScroll = new JScrollPane(menuTable);

        JPanel cartControls = new JPanel(new GridLayout(1, 6, 10, 0));
        cartControls.setBorder(BorderFactory.createTitledBorder("Cart Actions"));

        JTextField foodIdField = new JTextField();
        JTextField quantityField = new JTextField();

        cartControls.add(new JLabel("Food ID"));
        cartControls.add(foodIdField);
        cartControls.add(new JLabel("Quantity"));
        cartControls.add(quantityField);
        cartControls.add(addToCartButton);
        cartControls.add(removeButton);
        cartControls.add(clearButton);
        cartControls.add(payButton);

        JTable cartTable = new JTable(buildCartTableModel());
        JScrollPane cartScroll = new JScrollPane(cartTable);

        addToCartButton.addActionListener(e -> {
            try {
                int foodId = Integer.parseInt(foodIdField.getText().trim());
                int qty = Integer.parseInt(quantityField.getText().trim());

                if (qty <= 0) {
                    JOptionPane.showMessageDialog(this, "Quantity must be greater than zero.", "Invalid Quantity", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Food food = MyJDBC.getFoodById(foodId);
                if (food == null) {
                    JOptionPane.showMessageDialog(this, "Food not found.", "Invalid Food", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int existingQty = customerCart.getOrDefault(foodId, 0);
                if (existingQty + qty > food.getQuantity()) {
                    JOptionPane.showMessageDialog(this, "Not enough stock for that quantity.", "Stock Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                customerCart.put(foodId, existingQty + qty);
                foodIdField.setText("");
                quantityField.setText("");
                loadCartTable(cartTable);
                JOptionPane.showMessageDialog(this, "Item added to cart.", "Cart Updated", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Food ID and quantity must be numbers.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        });

        removeButton.addActionListener(e -> {
            int selected = cartTable.getSelectedRow();
            if (selected < 0) {
                JOptionPane.showMessageDialog(this, "Please select an item from the cart first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int foodId = Integer.parseInt(cartTable.getValueAt(selected, 0).toString());
            customerCart.remove(foodId);
            loadCartTable(cartTable);
            JOptionPane.showMessageDialog(this, "Item removed from cart.", "Cart Updated", JOptionPane.INFORMATION_MESSAGE);
        });

        clearButton.addActionListener(e -> {
            customerCart.clear();
            loadCartTable(cartTable);
            JOptionPane.showMessageDialog(this, "Cart cleared.", "Cart Updated", JOptionPane.INFORMATION_MESSAGE);
        });

        payButton.addActionListener(e -> {
            if (customerCart.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Your cart is empty.", "Empty Cart", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double total = calculateCartTotal();
            JTextField amountField = new JTextField(String.format("%.2f", total));
            Object[] message = {"Amount to pay:", amountField};
            int option = JOptionPane.showConfirmDialog(this, message, "Payment", JOptionPane.OK_CANCEL_OPTION);
            if (option != JOptionPane.OK_OPTION) {
                return;
            }

            try {
                double amountPaid = Double.parseDouble(amountField.getText().trim());
                if (amountPaid < total) {
                    JOptionPane.showMessageDialog(this, "Insufficient payment amount.", "Payment Failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                for (Map.Entry<Integer, Integer> entry : customerCart.entrySet()) {
                    if (!MyJDBC.buyFood(entry.getKey(), entry.getValue())) {
                        JOptionPane.showMessageDialog(this, "Payment failed because stock changed. Please try again.", "Payment Failed", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                double change = amountPaid - total;
                customerCart.clear();
                loadCartTable(cartTable);
                loadFoodTable(menuTable);
                JOptionPane.showMessageDialog(this, "Payment successful! Total: $" + String.format("%.2f", total) + " | Change: $" + String.format("%.2f", change), "Order Complete", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid payment amount.", "Invalid Payment", JOptionPane.ERROR_MESSAGE);
            }
        });

        refreshButton.addActionListener(e -> loadFoodTable(menuTable));
        logoutButton.addActionListener(e -> {
            customerCart.clear();
            loadCartTable(cartTable);
            cardLayout.show(cards, "login");
        });

        loadFoodTable(menuTable);
        loadCartTable(cartTable);

        panel.add(header, BorderLayout.NORTH);
        panel.add(menuScroll, BorderLayout.CENTER);
        panel.add(cartControls, BorderLayout.SOUTH);
        panel.add(cartScroll, BorderLayout.EAST);
        return panel;
    }

    private DefaultTableModel buildFoodTableModel() {
        return new DefaultTableModel(new Object[]{"Food ID", "Food Name", "Price", "Quantity"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private DefaultTableModel buildCustomerTableModel() {
        return new DefaultTableModel(new Object[]{"User ID", "Username", "Password"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private DefaultTableModel buildCartTableModel() {
        return new DefaultTableModel(new Object[]{"Food ID", "Food Name", "Quantity", "Unit Price", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private void loadFoodTable(JTable table) {
        DefaultTableModel model = buildFoodTableModel();
        ArrayList<Food> foods = MyJDBC.viewAllFood();
        for (Food food : foods) {
            model.addRow(new Object[]{food.getFoodId(), food.getFoodName(), food.getPrice(), food.getQuantity()});
        }
        table.setModel(model);
    }

    private void loadCustomerTable(JTable table) {
        DefaultTableModel model = buildCustomerTableModel();
        ArrayList<Customer> customers = MyJDBC.viewAllCustomers();
        for (Customer customer : customers) {
            model.addRow(new Object[]{customer.getUserId(), customer.getUsername(), customer.getPassword()});
        }
        table.setModel(model);
    }

    private void loadCartTable(JTable table) {
        DefaultTableModel model = buildCartTableModel();
        for (Map.Entry<Integer, Integer> entry : customerCart.entrySet()) {
            Food food = MyJDBC.getFoodById(entry.getKey());
            if (food == null) {
                continue;
            }
            int quantity = entry.getValue();
            double subtotal = food.getPrice() * quantity;
            model.addRow(new Object[]{food.getFoodId(), food.getFoodName(), quantity, food.getPrice(), subtotal});
        }
        table.setModel(model);
    }

    private double calculateCartTotal() {
        double total = 0.0;
        for (Map.Entry<Integer, Integer> entry : customerCart.entrySet()) {
            Food food = MyJDBC.getFoodById(entry.getKey());
            if (food != null) {
                total += food.getPrice() * entry.getValue();
            }
        }
        return total;
    }

    private void handleRegistration() {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JPasswordField verifyField = new JPasswordField();

        Object[] message = {
                "Username:", usernameField,
                "Password:", passwordField,
                "Verify password:", verifyField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Register New Customer", JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) {
            return;
        }

        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String verify = new String(verifyField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!password.equals(verify)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!", "Registration Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (MyJDBC.usernameExists(username)) {
            JOptionPane.showMessageDialog(this, "Username already exists. Choose another one.", "Duplicate User", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Customer customer = new Customer(0, username, password);
        if (customer.register()) {
            JOptionPane.showMessageDialog(this, "Registered Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            cardLayout.show(cards, "login");
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private FoodForm promptForFood(Food currentFood) {
        JTextField nameField = new JTextField(currentFood == null ? "" : currentFood.getFoodName());
        JTextField priceField = new JTextField(currentFood == null ? "" : String.valueOf(currentFood.getPrice()));
        JTextField quantityField = new JTextField(currentFood == null ? "" : String.valueOf(currentFood.getQuantity()));

        Object[] message = {
                "Food name:", nameField,
                "Price:", priceField,
                "Quantity:", quantityField
        };

        int option = JOptionPane.showConfirmDialog(this, message, currentFood == null ? "Add Food" : "Update Food", JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) {
            return null;
        }

        String name = nameField.getText().trim();
        String priceText = priceField.getText().trim();
        String quantityText = quantityField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Food name cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        try {
            double price = Double.parseDouble(priceText);
            int quantity = Integer.parseInt(quantityText);
            if (price < 0 || quantity < 0) {
                JOptionPane.showMessageDialog(this, "Price and quantity cannot be negative.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return null;
            }
            return new FoodForm(name, price, quantity);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values for price and quantity.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private CustomerInput promptForCustomer(Customer currentCustomer) {
        JTextField usernameField = new JTextField(currentCustomer == null ? "" : currentCustomer.getUsername());
        JPasswordField passwordField = new JPasswordField(currentCustomer == null ? "" : currentCustomer.getPassword());

        Object[] message = {
                "Username:", usernameField,
                "Password:", passwordField
        };

        int option = JOptionPane.showConfirmDialog(this, message, currentCustomer == null ? "Add Customer" : "Update Customer", JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) {
            return null;
        }

        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        return new CustomerInput(username, password);
    }

    private static class FoodForm {
        private final String name;
        private final double price;
        private final int quantity;

        private FoodForm(String name, double price, int quantity) {
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }
    }

    private static class CustomerInput {
        private final String username;
        private final String password;

        private CustomerInput(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }
}
