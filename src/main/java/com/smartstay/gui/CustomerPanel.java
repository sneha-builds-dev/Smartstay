package com.smartstay.gui;

import com.smartstay.model.Customer;
import com.smartstay.service.CustomerService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class CustomerPanel extends JPanel {

    private final CustomerService customerService;
    private final DefaultTableModel tableModel;

    private JTextField nameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextField addressField;

    public CustomerPanel() {

        customerService = new CustomerService();

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ---------- TITLE ----------
        JLabel titleLabel = new JLabel(
                "Customer Management",
                SwingConstants.CENTER
        );

        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        add(titleLabel, BorderLayout.NORTH);

        // ---------- FORM ----------
        JPanel formPanel = new JPanel(
                new GridLayout(4, 2, 10, 10)
        );

        nameField = new JTextField();
        phoneField = new JTextField();
        emailField = new JTextField();
        addressField = new JTextField();

        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);

        formPanel.add(new JLabel("Phone:"));
        formPanel.add(phoneField);

        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);

        formPanel.add(new JLabel("Address:"));
        formPanel.add(addressField);

        JButton addButton = new JButton("Add Customer");

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(addButton, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        // ---------- TABLE ----------
        String[] columns = {
                "ID",
                "Name",
                "Phone",
                "Email",
                "Address"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable customerTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(customerTable);

        add(scrollPane, BorderLayout.CENTER);

        // ---------- BUTTON ACTION ----------
        addButton.addActionListener(e -> addCustomer());

        // Load existing customers
        loadCustomers();
    }

    private void addCustomer() {

        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressField.getText().trim();

        if (name.isEmpty() || phone.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Name and phone are required."
            );

            return;
        }

        Customer customer = new Customer(
                0,
                name,
                phone,
                email,
                address
        );

        try {

            customerService.addCustomer(customer);

            JOptionPane.showMessageDialog(
                    this,
                    "Customer added successfully!"
            );

            clearFields();
            loadCustomers();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database error: " + e.getMessage()
            );
        }
    }

    private void loadCustomers() {

        try {

            List<Customer> customers =
                    customerService.getAllCustomers();

            tableModel.setRowCount(0);

            for (Customer customer : customers) {

                tableModel.addRow(new Object[]{
                        customer.getCustomerId(),
                        customer.getName(),
                        customer.getPhone(),
                        customer.getEmail(),
                        customer.getAddress()
                });
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load customers: "
                            + e.getMessage()
            );
        }
    }

    private void clearFields() {

        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        addressField.setText("");
    }
}