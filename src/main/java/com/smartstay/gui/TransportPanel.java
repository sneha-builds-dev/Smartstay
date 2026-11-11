
package com.smartstay.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import com.smartstay.transport.TransportService;
import com.smartstay.transport.Vehicle;
import com.smartstay.transport.Driver;
import com.smartstay.dao.TransportDAO;
public class TransportPanel extends JPanel {

    private static final Color BACKGROUND = new Color(18, 23, 32);
    private static final Color PANEL = new Color(27, 34, 46);
    private static final Color TEXT = new Color(240, 243, 248);
    private static final Color MUTED = new Color(155, 164, 178);
    private static final Color ACCENT = new Color(212, 175, 90);

    public TransportPanel() {
        setLayout(new BorderLayout(18, 18));
        setBackground(BACKGROUND);
        setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("Transport Management");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(
                "Manage guest pickups, drop-offs and transport services");
        subtitle.setForeground(MUTED);

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.add(title);
        header.add(Box.createVerticalStrut(8));
        header.add(subtitle);

        JPanel form = new JPanel(new GridLayout(0, 2, 14, 14));
        form.setBackground(PANEL);
        form.setBorder(new EmptyBorder(24, 24, 24, 24));

        JTextField customerId = new JTextField();
        JComboBox<Vehicle> vehicleBox = new JComboBox<>();
        JComboBox<Driver> driverBox = new JComboBox<>();
        JTextField pickup = new JTextField();
        JTextField drop = new JTextField();
        JTextField distance = new JTextField();
        JTextField pickupTime = new JTextField(
                java.time.LocalDateTime.now()
                        .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        );

        JLabel fareLabel = new JLabel("Estimated fare: ₹0.00");
        fareLabel.setForeground(ACCENT);
        JLabel revenueLabel = new JLabel("Total Transport Revenue: ₹0.00");
        revenueLabel.setForeground(ACCENT);

        addField(form, "Customer ID", customerId);
        addField(form, "Available Vehicle", vehicleBox);
        addField(form, "Available Driver", driverBox);
        addField(form, "Pickup Location", pickup);
        addField(form, "Drop Location", drop);
        addField(form, "Distance (km)", distance);
        addField(form, "Pickup Time (yyyy-MM-dd HH:mm)", pickupTime);

        JButton calculateButton = new JButton("Calculate Fare");
        JButton refreshButton = new JButton("Refresh Vehicles / Drivers");
        JButton bookButton = new JButton("Book Transport");

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        actions.setOpaque(false);
        actions.add(calculateButton);
        actions.add(refreshButton);
        actions.add(fareLabel);
        actions.add(revenueLabel);
        actions.add(bookButton);
        calculateButton.addActionListener(e -> {
            try {
                double km = Double.parseDouble(distance.getText().trim());

                if (km <= 0) {
                    JOptionPane.showMessageDialog(this,
                            "Distance must be greater than zero.");
                    return;
                }

                Vehicle vehicle = (Vehicle) vehicleBox.getSelectedItem();

                if (vehicle == null) {
                    JOptionPane.showMessageDialog(this,
                            "No available vehicle found.");
                    return;
                }

                double fare = km * vehicle.getPricePerKm();
                fareLabel.setText(String.format("Estimated fare: ₹%.2f", fare));

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Enter a valid distance.");
            }
        });
        bookButton.addActionListener(e -> {
            try {
                int customerIdValue = Integer.parseInt(customerId.getText().trim());

                Vehicle vehicle = (Vehicle) vehicleBox.getSelectedItem();
                Driver driver = (Driver) driverBox.getSelectedItem();

                String pickupLocation = pickup.getText().trim();
                String dropLocation = drop.getText().trim();

                double distanceKm = Double.parseDouble(distance.getText().trim());

                java.time.LocalDateTime pickupDateTime =
                        java.time.LocalDateTime.parse(
                                pickupTime.getText().trim(),
                                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                        );

                if (vehicle == null || driver == null
                        || pickupLocation.isEmpty() || dropLocation.isEmpty()
                        || distanceKm <= 0) {
                    javax.swing.JOptionPane.showMessageDialog(
                            this, "Please fill in all fields correctly."
                    );
                    return;
                }

                double fare = distanceKm * vehicle.getPricePerKm();

                TransportDAO dao = new TransportDAO();

                int bookingId = dao.saveTransportBooking(
                        customerIdValue,
                        vehicle.getVehicleId(),
                        driver.getDriverId(),
                        pickupLocation,
                        dropLocation,
                        pickupDateTime,
                        distanceKm,
                        fare
                );

                if (bookingId > 0) {
                    javax.swing.JOptionPane.showMessageDialog(
                            this,
                            "Transport booked successfully!\nBooking ID: " + bookingId
                                    + "\nEstimated Fare: ₹" + String.format("%.2f", fare)
                    );
                } else {
                    javax.swing.JOptionPane.showMessageDialog(
                            this, "Booking was not saved. Please try again."
                    );
                }

            } catch (NumberFormatException ex) {
                javax.swing.JOptionPane.showMessageDialog(
                        this, "Enter a valid Customer ID and distance."
                );
            } catch (java.time.format.DateTimeParseException ex) {
                javax.swing.JOptionPane.showMessageDialog(
                        this, "Enter pickup time as yyyy-MM-dd HH:mm."
                );
            } catch (java.sql.SQLException ex) {
                javax.swing.JOptionPane.showMessageDialog(
                        this, "Database error: " + ex.getMessage()
                );
            }
        });

        refreshButton.addActionListener(e -> {
            try {
                vehicleBox.removeAllItems();
                driverBox.removeAllItems();

                com.smartstay.dao.TransportDAO dao =
                        new com.smartstay.dao.TransportDAO();

                for (Vehicle vehicle : dao.getAvailableVehicles()) {
                    vehicleBox.addItem(vehicle);
                }

                for (Driver driver : dao.getAvailableDrivers()) {
                    driverBox.addItem(driver);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Could not load vehicles/drivers: " + ex.getMessage());
            }
        });

        JPanel content = new JPanel(new BorderLayout(12, 12));
        content.setBackground(PANEL);
        content.setBorder(new EmptyBorder(20, 20, 20, 20));
        content.add(form, BorderLayout.CENTER);
        content.add(actions, BorderLayout.SOUTH);

        add(header, BorderLayout.NORTH);

        JPanel mainContent = new JPanel(new BorderLayout(12, 18));
        mainContent.setOpaque(false);

        mainContent.add(content, BorderLayout.NORTH);

        String[] columns = {
                "Booking ID", "Customer ID", "Vehicle ID", "Driver ID",
                "Pickup", "Drop", "Distance (km)", "Fare (₹)", "Status"
        };

        javax.swing.table.DefaultTableModel historyModel =
                new javax.swing.table.DefaultTableModel(columns, 0) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };

        JTable historyTable = new JTable(historyModel);

        historyTable.setRowHeight(32);
        historyTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        historyTable.setFillsViewportHeight(true);
        historyTable.setSelectionMode(
                javax.swing.ListSelectionModel.SINGLE_SELECTION
        );
        historyTable.setAutoCreateRowSorter(true);

        historyTable.getTableHeader().setReorderingAllowed(false);
        historyTable.getTableHeader().setPreferredSize(
                new Dimension(0, 36)
        );

        int[] columnWidths = {
                85, 100, 90, 85, 180, 180, 110, 100, 100
        };

        for (int i = 0; i < columnWidths.length; i++) {
            historyTable.getColumnModel()
                    .getColumn(i)
                    .setPreferredWidth(columnWidths[i]);
        }

        JScrollPane historyScroll = new JScrollPane(historyTable);
        historyScroll.setPreferredSize(new Dimension(900, 220));

        JPanel historyPanel = new JPanel(new BorderLayout(0, 8));
        JTextField bookingSearchField = new JTextField();
        bookingSearchField.setPreferredSize(new Dimension(220, 32));
        bookingSearchField.setToolTipText("Search by Booking ID or Customer ID");
        JButton searchBookingButton = new JButton("Search");
        JButton showAllBookingsButton = new JButton("Show All");
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JComboBox<String> statusFilter = new JComboBox<>(
                new String[]{"All", "Booked", "Cancelled"}
        );
        searchPanel.setOpaque(false);

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(new JLabel("Status:"));
        searchPanel.add(statusFilter);
        searchPanel.add(bookingSearchField);
        searchPanel.add(searchBookingButton);
        searchPanel.add(showAllBookingsButton);
        historyPanel.setOpaque(false);
        JPanel historyTopPanel = new JPanel(new BorderLayout(0, 8));
        historyTopPanel.setOpaque(false);

        historyTopPanel.add(
                new JLabel("Transport Booking History"),
                BorderLayout.NORTH
        );

        historyTopPanel.add(searchPanel, BorderLayout.CENTER);

        historyPanel.add(historyTopPanel, BorderLayout.NORTH);
        historyPanel.add(historyScroll, BorderLayout.CENTER);
        historyPanel.add(searchPanel, BorderLayout.NORTH);

        JButton historyRefreshButton = new JButton("Refresh Booking History");
        JButton exportButton = new JButton("Export to CSV");
        JButton cancelBookingButton = new JButton("Cancel Booking");
        JPanel historyActions = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 10, 5)
        );
        historyActions.setOpaque(false);

        historyActions.add(historyRefreshButton);
        historyActions.add(cancelBookingButton);
        historyActions.add(exportButton);

        mainContent.add(historyActions, BorderLayout.SOUTH);
        historyActions.setPreferredSize(new java.awt.Dimension(700, 50));
        historyActions.setVisible(true);
        historyPanel.revalidate();
        historyPanel.repaint();

        mainContent.add(historyPanel, BorderLayout.CENTER);
        mainContent.setPreferredSize(new java.awt.Dimension(1100, 1000));
        JScrollPane pageScroll = new JScrollPane(mainContent);
        pageScroll.setBorder(null);
        pageScroll.getVerticalScrollBar().setUnitIncrement(16);

        add(pageScroll, BorderLayout.CENTER);
        searchBookingButton.addActionListener(e -> {
            String searchText = bookingSearchField.getText().trim();

            if (searchText.isEmpty()
                    && "All".equals(statusFilter.getSelectedItem())) {
                javax.swing.JOptionPane.showMessageDialog(
                        this, "Enter Booking ID or Customer ID."
                );
                return;
            }

            historyModel.setRowCount(0);

            try {
                TransportDAO dao = new TransportDAO();

                for (Object[] booking : dao.getTransportBookingHistory()) {
                    String bookingId = String.valueOf(booking[0]);
                    String bookingCustomerId = String.valueOf(booking[1]);

                    String bookingStatus = String.valueOf(booking[8]);
                    String selectedStatus = String.valueOf(statusFilter.getSelectedItem());

                    boolean matchesSearch = bookingId.contains(searchText)
                            || bookingCustomerId.contains(searchText);

                    boolean matchesStatus = selectedStatus.equals("All")
                            || bookingStatus.equalsIgnoreCase(selectedStatus);

                    if (matchesSearch && matchesStatus) {

                        historyModel.addRow(booking);
                    }
                }

                if (historyModel.getRowCount() == 0) {
                    javax.swing.JOptionPane.showMessageDialog(
                            this, "No matching bookings found."
                    );
                }

            } catch (java.sql.SQLException ex) {
                javax.swing.JOptionPane.showMessageDialog(
                        this, "Search failed: " + ex.getMessage()
                );
            }
        });
        statusFilter.addActionListener(e -> searchBookingButton.doClick());
        showAllBookingsButton.addActionListener(e -> {
            bookingSearchField.setText("");
            historyRefreshButton.doClick();
        });
        historyRefreshButton.addActionListener(e -> {
            try {
                historyModel.setRowCount(0);

                TransportDAO dao = new TransportDAO();

                for (Object[] booking : dao.getTransportBookingHistory()) {
                    historyModel.addRow(booking);
                }
                revenueLabel.setText(
                        String.format("Total Transport Revenue: ₹%.2f",
                                dao.getTotalTransportRevenue())
                );
            }

            catch (java.sql.SQLException ex) {
                javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Could not load booking history: " + ex.getMessage()
                );
            }
        });
        exportButton.addActionListener(e -> {
            javax.swing.JFileChooser chooser =
                    new javax.swing.JFileChooser();

            chooser.setSelectedFile(
                    new java.io.File("Transport_Booking_History.csv")
            );

            if (chooser.showSaveDialog(this)
                    == javax.swing.JFileChooser.APPROVE_OPTION) {

                java.io.File file = chooser.getSelectedFile();

                try (java.io.PrintWriter writer =
                             new java.io.PrintWriter(file)) {

                    for (int col = 0; col < historyModel.getColumnCount(); col++) {
                        writer.print("\"" +
                                historyModel.getColumnName(col) + "\"");
                        if (col < historyModel.getColumnCount() - 1) {
                            writer.print(",");
                        }
                    }
                    writer.println();

                    for (int row = 0; row < historyModel.getRowCount(); row++) {
                        for (int col = 0; col < historyModel.getColumnCount(); col++) {
                            Object value = historyModel.getValueAt(row, col);
                            String text = value == null ? "" : value.toString();

                            writer.print("\"" +
                                    text.replace("\"", "\"\"") + "\"");

                            if (col < historyModel.getColumnCount() - 1) {
                                writer.print(",");
                            }
                        }
                        writer.println();
                    }

                    javax.swing.JOptionPane.showMessageDialog(
                            this, "Booking history exported successfully!"
                    );

                } catch (java.io.IOException ex) {
                    javax.swing.JOptionPane.showMessageDialog(
                            this, "Export failed: " + ex.getMessage()
                    );
                }
            }
        });
        cancelBookingButton.addActionListener(e -> {
            int selectedRow = historyTable.getSelectedRow();

            if (selectedRow == -1) {
                javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Please select a booking from the history table."
                );
                return;
            }

            int modelRow = historyTable.convertRowIndexToModel(selectedRow);
            int bookingId = (int) historyModel.getValueAt(modelRow, 0);
            String status = String.valueOf(historyModel.getValueAt(modelRow, 8));

            if (!"Booked".equalsIgnoreCase(status)) {
                javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Only bookings with Booked status can be cancelled."
                );
                return;
            }

            int confirm = javax.swing.JOptionPane.showConfirmDialog(
                    this,
                    "Cancel booking #" + bookingId + "?",
                    "Confirm Cancellation",
                    javax.swing.JOptionPane.YES_NO_OPTION
            );

            if (confirm != javax.swing.JOptionPane.YES_OPTION) {
                return;
            }

            try {
                TransportDAO dao = new TransportDAO();

                if (dao.cancelTransportBooking(bookingId)) {
                    javax.swing.JOptionPane.showMessageDialog(
                            this,
                            "Booking #" + bookingId + " cancelled successfully."
                    );
                    historyRefreshButton.doClick();
                } else {
                    javax.swing.JOptionPane.showMessageDialog(
                            this,
                            "Booking could not be cancelled. Refresh the history and try again."
                    );
                }
            } catch (java.sql.SQLException ex) {
                javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Database error: " + ex.getMessage()
                );
            }
        });
        historyRefreshButton.doClick();
        refreshButton.doClick();
    }

    private void addField(JPanel panel, String labelText, JComponent input) {
        JLabel label = new JLabel(labelText);
        label.setForeground(TEXT);
        panel.add(label);
        panel.add(input);
    }
}