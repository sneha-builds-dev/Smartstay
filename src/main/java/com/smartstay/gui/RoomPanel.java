package com.smartstay.gui;

import com.smartstay.model.Room;
import com.smartstay.service.RoomService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class RoomPanel extends JPanel {

    private final RoomService roomService;
    private final DefaultTableModel tableModel;
    private final JTable roomTable;

    private JTextField roomNumberField;
    private JTextField priceField;
    private JTextField capacityField;
    private JTextField facilitiesField;

    private JComboBox<String> roomTypeCombo;
    private JComboBox<String> statusCombo;

    public RoomPanel() {

        roomService = new RoomService();

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ---------- TITLE ----------
        JLabel titleLabel = new JLabel("Room Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        add(titleLabel, BorderLayout.NORTH);

        // ---------- FORM ----------
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));

        roomNumberField = new JTextField();
        priceField = new JTextField();
        capacityField = new JTextField();
        facilitiesField = new JTextField();

        roomTypeCombo = new JComboBox<>(
                new String[]{"Standard", "Deluxe", "Suite"}
        );

        statusCombo = new JComboBox<>(
                new String[]{
                        "Available",
                        "Reserved",
                        "Occupied",
                        "Cleaning",
                        "Maintenance"
                }
        );

        formPanel.add(new JLabel("Room Number:"));
        formPanel.add(roomNumberField);

        formPanel.add(new JLabel("Room Type:"));
        formPanel.add(roomTypeCombo);

        formPanel.add(new JLabel("Price / Night:"));
        formPanel.add(priceField);

        formPanel.add(new JLabel("Capacity:"));
        formPanel.add(capacityField);

        formPanel.add(new JLabel("Status:"));
        formPanel.add(statusCombo);

        formPanel.add(new JLabel("Facilities:"));
        formPanel.add(facilitiesField);

        // ---------- BUTTON ----------
        JButton addButton = new JButton("Add Room");

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(addButton, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // ---------- TABLE ----------
        String[] columns = {
                "ID",
                "Room No.",
                "Type",
                "Price",
                "Capacity",
                "Status",
                "Facilities"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        roomTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(roomTable);

        add(scrollPane, BorderLayout.CENTER);

        // ---------- BUTTON ACTION ----------
        addButton.addActionListener(e -> addRoom());

        // Load existing rooms
        loadRooms();
    }

    private void addRoom() {

        try {

            String roomNumber = roomNumberField.getText().trim();
            String roomType = roomTypeCombo.getSelectedItem().toString();

            double price = Double.parseDouble(
                    priceField.getText().trim()
            );

            int capacity = Integer.parseInt(
                    capacityField.getText().trim()
            );

            String status = statusCombo.getSelectedItem().toString();

            String facilities = facilitiesField.getText().trim();

            if (roomNumber.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter room number."
                );
                return;
            }

            if (capacity <= 0 || price <= 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Price and capacity must be greater than 0."
                );
                return;
            }

            Room room = new Room(
                    0,
                    roomNumber,
                    roomType,
                    price,
                    capacity,
                    status,
                    facilities
            );

            roomService.addRoom(room);

            JOptionPane.showMessageDialog(
                    this,
                    "Room added successfully!"
            );

            clearFields();
            loadRooms();

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid price and capacity."
            );

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database error: " + e.getMessage()
            );
        }
    }

    private void loadRooms() {

        try {

            List<Room> rooms = roomService.getAllRooms();

            tableModel.setRowCount(0);

            for (Room room : rooms) {

                tableModel.addRow(new Object[]{
                        room.getRoomId(),
                        room.getRoomNumber(),
                        room.getRoomType(),
                        room.getPricePerNight(),
                        room.getCapacity(),
                        room.getStatus(),
                        room.getFacilities()
                });
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load rooms: " + e.getMessage()
            );
        }
    }

    private void clearFields() {

        roomNumberField.setText("");
        priceField.setText("");
        capacityField.setText("");
        facilitiesField.setText("");

        roomTypeCombo.setSelectedIndex(0);
        statusCombo.setSelectedIndex(0);
    }
}