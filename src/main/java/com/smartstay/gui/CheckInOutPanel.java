package com.smartstay.gui;

import com.smartstay.service.CheckInOutService;

import javax.swing.*;
import java.awt.*;

public class CheckInOutPanel extends JPanel {

    private final CheckInOutService checkInOutService;

    private JTextField bookingIdField;
    private JTextField roomIdField;

    public CheckInOutPanel() {

        checkInOutService = new CheckInOutService();

        setLayout(new BorderLayout(15, 15));
        setBorder(
                BorderFactory.createEmptyBorder(
                        30, 30, 30, 30
                )
        );

        JLabel titleLabel = new JLabel(
                "Check-in / Check-out",
                SwingConstants.CENTER
        );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(
                new GridLayout(2, 2, 10, 10)
        );

        bookingIdField = new JTextField();
        roomIdField = new JTextField();

        formPanel.add(
                new JLabel("Booking ID:")
        );
        formPanel.add(bookingIdField);

        formPanel.add(
                new JLabel("Room ID:")
        );
        formPanel.add(roomIdField);

        JPanel buttonPanel = new JPanel(
                new FlowLayout()
        );

        JButton checkInButton =
                new JButton("Check-in");

        JButton checkOutButton =
                new JButton("Check-out");

        JButton availableButton =
                new JButton("Mark Available");

        buttonPanel.add(checkInButton);
        buttonPanel.add(checkOutButton);
        buttonPanel.add(availableButton);

        JPanel centerPanel = new JPanel(
                new BorderLayout(15, 15)
        );

        centerPanel.add(
                formPanel,
                BorderLayout.NORTH
        );

        centerPanel.add(
                buttonPanel,
                BorderLayout.CENTER
        );

        add(centerPanel, BorderLayout.CENTER);

        checkInButton.addActionListener(
                e -> checkIn()
        );

        checkOutButton.addActionListener(
                e -> checkOut()
        );

        availableButton.addActionListener(
                e -> markAvailable()
        );
    }

    private void checkIn() {

        try {

            int bookingId = Integer.parseInt(
                    bookingIdField.getText().trim()
            );

            int roomId = Integer.parseInt(
                    roomIdField.getText().trim()
            );

            checkInOutService.checkIn(
                    bookingId,
                    roomId
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Guest checked-in successfully!\n"
                            + "Room status changed to Occupied."
            );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid numeric IDs."
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Check-in failed: "
                            + e.getMessage()
            );
        }
    }

    private void checkOut() {

        try {

            int bookingId = Integer.parseInt(
                    bookingIdField.getText().trim()
            );

            int roomId = Integer.parseInt(
                    roomIdField.getText().trim()
            );

            checkInOutService.checkOut(
                    bookingId,
                    roomId
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Guest checked-out successfully!\n"
                            + "Room status changed to Cleaning."
            );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid numeric IDs."
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Check-out failed: "
                            + e.getMessage()
            );
        }
    }

    private void markAvailable() {

        try {

            int roomId = Integer.parseInt(
                    roomIdField.getText().trim()
            );

            checkInOutService.markRoomAvailable(
                    roomId
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Room is now Available."
            );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid Room ID."
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not update room: "
                            + e.getMessage()
            );
        }
    }
}