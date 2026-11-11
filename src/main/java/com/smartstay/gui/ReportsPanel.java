package com.smartstay.gui;

import com.smartstay.util.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ReportsPanel extends JPanel {

    private final DefaultTableModel tableModel;
    private final JTable reportTable;

    public ReportsPanel() {

        setLayout(new BorderLayout(15, 15));

        setBorder(
                BorderFactory.createEmptyBorder(
                        20, 20, 20, 20
                )
        );

        JLabel titleLabel = new JLabel(
                "SmartStay Reports",
                SwingConstants.CENTER
        );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(
                new FlowLayout()
        );

        JButton roomReportButton =
                new JButton("Room Report");

        JButton bookingReportButton =
                new JButton("Booking Report");

        JButton revenueReportButton =
                new JButton("Revenue Report");

        JButton occupancyButton =
                new JButton("Occupancy Report");

        buttonPanel.add(roomReportButton);
        buttonPanel.add(bookingReportButton);
        buttonPanel.add(revenueReportButton);
        buttonPanel.add(occupancyButton);

        add(buttonPanel, BorderLayout.SOUTH);

        String[] columns = {
                "Report"
        };

        tableModel = new DefaultTableModel(
                columns,
                0
        ) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column
            ) {
                return false;
            }
        };

        reportTable = new JTable(tableModel);

        reportTable.setRowHeight(30);

        add(
                new JScrollPane(reportTable),
                BorderLayout.CENTER
        );

        roomReportButton.addActionListener(
                e -> loadRoomReport()
        );

        bookingReportButton.addActionListener(
                e -> loadBookingReport()
        );

        revenueReportButton.addActionListener(
                e -> loadRevenueReport()
        );

        occupancyButton.addActionListener(
                e -> loadOccupancyReport()
        );
    }

    private void loadRoomReport() {

        tableModel.setRowCount(0);

        String sql =
                "SELECT status, COUNT(*) AS total " +
                        "FROM rooms GROUP BY status";

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                String status =
                        resultSet.getString("status");

                int total =
                        resultSet.getInt("total");

                tableModel.addRow(
                        new Object[]{
                                "Rooms - "
                                        + status
                                        + ": "
                                        + total
                        }
                );
            }

        } catch (SQLException e) {

            showError(e);
        }
    }

    private void loadBookingReport() {

        tableModel.setRowCount(0);

        String sql =
                "SELECT status, COUNT(*) AS total " +
                        "FROM bookings GROUP BY status";

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                String status =
                        resultSet.getString("status");

                int total =
                        resultSet.getInt("total");

                tableModel.addRow(
                        new Object[]{
                                "Bookings - "
                                        + status
                                        + ": "
                                        + total
                        }
                );
            }

        } catch (SQLException e) {

            showError(e);
        }
    }

    private void loadRevenueReport() {

        tableModel.setRowCount(0);

        String sql =
                "SELECT " +
                        "COALESCE(SUM(total_amount), 0) " +
                        "AS revenue " +
                        "FROM bills " +
                        "WHERE payment_status = 'Paid'";

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            if (resultSet.next()) {

                double revenue =
                        resultSet.getDouble("revenue");

                tableModel.addRow(
                        new Object[]{
                                String.format(
                                        "Total Paid Revenue: ₹%.2f",
                                        revenue
                                )
                        }
                );
            }

        } catch (SQLException e) {

            showError(e);
        }
    }

    private void loadOccupancyReport() {

        tableModel.setRowCount(0);

        String sql =
                "SELECT " +
                        "COUNT(*) AS total_rooms, " +
                        "SUM(status = 'Occupied') " +
                        "AS occupied_rooms " +
                        "FROM rooms";

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            if (resultSet.next()) {

                int totalRooms =
                        resultSet.getInt("total_rooms");

                int occupiedRooms =
                        resultSet.getInt("occupied_rooms");

                double occupancy =
                        totalRooms == 0
                                ? 0
                                : (occupiedRooms * 100.0)
                                / totalRooms;

                tableModel.addRow(
                        new Object[]{
                                "Total Rooms: "
                                        + totalRooms
                        }
                );

                tableModel.addRow(
                        new Object[]{
                                "Occupied Rooms: "
                                        + occupiedRooms
                        }
                );

                tableModel.addRow(
                        new Object[]{
                                String.format(
                                        "Occupancy Rate: %.2f%%",
                                        occupancy
                                )
                        }
                );
            }

        } catch (SQLException e) {

            showError(e);
        }
    }

    private void showError(SQLException e) {

        JOptionPane.showMessageDialog(
                this,
                "Could not load report:\n"
                        + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}