package com.smartstay.gui;

import com.smartstay.model.Room;
import com.smartstay.service.RecommendationService;
import com.smartstay.service.RoomService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class RecommendationPanel extends JPanel {

    private final RoomService roomService;
    private final RecommendationService recommendationService;

    private JTextField guestsField;
    private JTextField budgetField;
    private JTextField facilityField;

    private JComboBox<String> roomTypeCombo;

    private final DefaultTableModel tableModel;

    public RecommendationPanel() {

        roomService = new RoomService();
        recommendationService = new RecommendationService();

        setLayout(new BorderLayout(15, 15));

        setBorder(
                BorderFactory.createEmptyBorder(
                        20, 20, 20, 20
                )
        );

        JLabel titleLabel = new JLabel(
                "Smart Room Recommendation",
                SwingConstants.CENTER
        );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(
                new GridLayout(4, 2, 10, 10)
        );

        guestsField = new JTextField();

        budgetField = new JTextField();

        facilityField = new JTextField();

        roomTypeCombo = new JComboBox<>(
                new String[]{
                        "",
                        "Standard",
                        "Deluxe",
                        "Suite"
                }
        );

        formPanel.add(
                new JLabel("Number of Guests:")
        );
        formPanel.add(guestsField);

        formPanel.add(
                new JLabel("Maximum Budget / Night:")
        );
        formPanel.add(budgetField);

        formPanel.add(
                new JLabel("Preferred Room Type:")
        );
        formPanel.add(roomTypeCombo);

        formPanel.add(
                new JLabel("Required Facility:")
        );
        formPanel.add(facilityField);

        JButton recommendButton =
                new JButton("Find Best Rooms");

        JPanel topPanel = new JPanel(
                new BorderLayout(10, 10)
        );

        topPanel.add(
                formPanel,
                BorderLayout.CENTER
        );

        topPanel.add(
                recommendButton,
                BorderLayout.SOUTH
        );

        add(
                topPanel,
                BorderLayout.NORTH
        );

        String[] columns = {
                "Room ID",
                "Room No.",
                "Type",
                "Price / Night",
                "Capacity",
                "Status",
                "Facilities"
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

        JTable recommendationTable =
                new JTable(tableModel);

        add(
                new JScrollPane(recommendationTable),
                BorderLayout.CENTER
        );

        recommendButton.addActionListener(
                e -> findRecommendations()
        );
    }

    private void findRecommendations() {

        try {

            int guests = Integer.parseInt(
                    guestsField.getText().trim()
            );

            double budget = Double.parseDouble(
                    budgetField.getText().trim()
            );

            String preferredType =
                    roomTypeCombo
                            .getSelectedItem()
                            .toString();

            String requiredFacility =
                    facilityField.getText().trim();

            if (guests <= 0 || budget <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Guests and budget must be greater than 0."
                );

                return;
            }

            List<Room> rooms =
                    roomService.getAllRooms();

            List<Room> recommendedRooms =
                    recommendationService.recommendRooms(
                            rooms,
                            guests,
                            budget,
                            preferredType,
                            requiredFacility
                    );

            tableModel.setRowCount(0);

            for (Room room : recommendedRooms) {

                tableModel.addRow(
                        new Object[]{
                                room.getRoomId(),
                                room.getRoomNumber(),
                                room.getRoomType(),
                                room.getPricePerNight(),
                                room.getCapacity(),
                                room.getStatus(),
                                room.getFacilities()
                        }
                );
            }

            if (recommendedRooms.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "No suitable rooms found."
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        recommendedRooms.size()
                                + " suitable room(s) found!"
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid numeric values."
            );

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load rooms:\n"
                            + e.getMessage()
            );
        }
    }
}