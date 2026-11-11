package com.smartstay.transport;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class TransportPanel extends JPanel {

    private final TransportDAO dao = new TransportDAO();
    private final TransportService service = new TransportService();

    private final JComboBox<Vehicle> vehicleBox = new JComboBox<>();
    private final JComboBox<Driver> driverBox = new JComboBox<>();

    private final JTextField pickupField = new JTextField(20);
    private final JTextField dropField = new JTextField(20);
    private final JTextField distanceField = new JTextField(10);
    private final JSpinner passengerSpinner =
            new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));

    private final JLabel fareLabel = new JLabel("Estimated fare: ₹0.00");

    public TransportPanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel heading = new JLabel("Transport Management");
        heading.setFont(new Font("SansSerif", Font.BOLD, 24));
        add(heading, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(0, 2, 12, 12));

        form.add(new JLabel("Pickup Location:"));
        form.add(pickupField);

        form.add(new JLabel("Drop Location:"));
        form.add(dropField);

        form.add(new JLabel("Passengers:"));
        form.add(passengerSpinner);

        form.add(new JLabel("Vehicle:"));
        form.add(vehicleBox);

        form.add(new JLabel("Driver:"));
        form.add(driverBox);

        form.add(new JLabel("Distance (km):"));
        form.add(distanceField);

        JButton calculateButton = new JButton("Calculate Fare");
        calculateButton.addActionListener(e -> calculateFare());

        JButton refreshButton = new JButton("Refresh Vehicles / Drivers");
        refreshButton.addActionListener(e -> loadOptions());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        bottom.add(calculateButton);
        bottom.add(refreshButton);
        bottom.add(fareLabel);

        add(form, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        loadOptions();
    }

    private void loadOptions() {
        try {
            vehicleBox.removeAllItems();
            driverBox.removeAllItems();

            for (Vehicle vehicle : dao.getAvailableVehicles()) {
                vehicleBox.addItem(vehicle);
            }

            for (Driver driver : dao.getAvailableDrivers()) {
                driverBox.addItem(driver);
            }

            vehicleBox.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(
                        JList<?> list, Object value, int index,
                        boolean selected, boolean focused) {

                    super.getListCellRendererComponent(
                            list, value, index, selected, focused);

                    if (value instanceof Vehicle v) {
                        setText(v.getVehicleType() + " - "
                                + v.getVehicleNumber() + " ("
                                + v.getCapacity() + " seats, ₹"
                                + v.getPricePerKm() + "/km)");
                    }
                    return this;
                }
            });

            driverBox.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(
                        JList<?> list, Object value, int index,
                        boolean selected, boolean focused) {

                    super.getListCellRendererComponent(
                            list, value, index, selected, focused);

                    if (value instanceof Driver d) {
                        setText(d.getDriverName() + " - " + d.getPhone());
                    }
                    return this;
                }
            });

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not load transport options.\n" + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void calculateFare() {
        try {
            String pickup = pickupField.getText().trim();
            String drop = dropField.getText().trim();

            if (pickup.isEmpty() || drop.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this, "Enter pickup and drop locations.");
                return;
            }

            double distance = Double.parseDouble(
                    distanceField.getText().trim());

            Vehicle vehicle = (Vehicle) vehicleBox.getSelectedItem();

            if (vehicle == null) {
                JOptionPane.showMessageDialog(
                        this, "No available vehicle. Refresh or add vehicles.");
                return;
            }

            int passengers = (Integer) passengerSpinner.getValue();

            if (!service.canCarryPassengers(vehicle, passengers)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Selected vehicle cannot accommodate "
                                + passengers + " passengers.");
                return;
            }

            double fare = service.calculateFare(
                    distance, vehicle.getPricePerKm());

            fareLabel.setText(String.format(
                    "Estimated fare: ₹%.2f", fare));

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this, "Enter a valid distance in kilometres.");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    this, ex.getMessage());
        }
    }
}