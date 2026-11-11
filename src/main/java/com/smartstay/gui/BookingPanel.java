package com.smartstay.gui;

import com.smartstay.model.Booking;
import com.smartstay.service.BookingService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class BookingPanel extends JPanel {

    private final BookingService bookingService;
    private final DefaultTableModel tableModel;

    private JTextField customerIdField;
    private JTextField roomIdField;
    private JTextField checkInField;
    private JTextField checkOutField;
    private JTextField guestsField;

    public BookingPanel() {

        bookingService = new BookingService();

        setLayout(new BorderLayout(15, 15));

        setBorder(
                BorderFactory.createEmptyBorder(
                        20, 20, 20, 20
                )
        );

        JLabel titleLabel = new JLabel(
                "Booking Management",
                SwingConstants.CENTER
        );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(
                new GridLayout(5, 2, 10, 10)
        );

        customerIdField = new JTextField();
        roomIdField = new JTextField();
        checkInField = new JTextField();
        checkOutField = new JTextField();
        guestsField = new JTextField();

        formPanel.add(new JLabel("Customer ID:"));
        formPanel.add(customerIdField);

        formPanel.add(new JLabel("Room ID:"));
        formPanel.add(roomIdField);

        formPanel.add(
                new JLabel("Check-in (YYYY-MM-DD):")
        );
        formPanel.add(checkInField);

        formPanel.add(
                new JLabel("Check-out (YYYY-MM-DD):")
        );
        formPanel.add(checkOutField);

        formPanel.add(
                new JLabel("Number of Guests:")
        );
        formPanel.add(guestsField);

        JButton bookButton =
                new JButton("Create Booking");

        JPanel topPanel = new JPanel(
                new BorderLayout(10, 10)
        );

        topPanel.add(
                formPanel,
                BorderLayout.CENTER
        );

        topPanel.add(
                bookButton,
                BorderLayout.SOUTH
        );

        add(
                topPanel,
                BorderLayout.NORTH
        );

        String[] columns = {
                "Booking ID",
                "Customer ID",
                "Room ID",
                "Check-in",
                "Check-out",
                "Guests",
                "Status"
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

        JTable bookingTable =
                new JTable(tableModel);

        JScrollPane scrollPane =
                new JScrollPane(bookingTable);

        add(
                scrollPane,
                BorderLayout.CENTER
        );

        bookButton.addActionListener(
                e -> createBooking()
        );

        loadBookings();
    }

    private void createBooking() {

        try {

            int customerId = Integer.parseInt(
                    customerIdField
                            .getText()
                            .trim()
            );

            int roomId = Integer.parseInt(
                    roomIdField
                            .getText()
                            .trim()
            );

            LocalDate checkIn =
                    LocalDate.parse(
                            checkInField
                                    .getText()
                                    .trim()
                    );

            LocalDate checkOut =
                    LocalDate.parse(
                            checkOutField
                                    .getText()
                                    .trim()
                    );

            int guests = Integer.parseInt(
                    guestsField
                            .getText()
                            .trim()
            );

            if (customerId <= 0 ||
                    roomId <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Customer ID and Room ID "
                                + "must be valid."
                );

                return;
            }

            if (checkOut.isBefore(checkIn) ||
                    checkOut.equals(checkIn)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Check-out date must be after "
                                + "check-in date."
                );

                return;
            }

            if (guests <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Number of guests must be "
                                + "greater than 0."
                );

                return;
            }

            Booking booking = new Booking(
                    0,
                    customerId,
                    roomId,
                    checkIn,
                    checkOut,
                    guests,
                    "Reserved"
            );

            bookingService.addBooking(booking);

            JOptionPane.showMessageDialog(
                    this,
                    "Booking created successfully!"
            );

            clearFields();
            loadBookings();

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid numeric values."
            );

        } catch (DateTimeParseException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Use date format: YYYY-MM-DD"
            );

        } catch (RuntimeException e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Booking Error",
                    JOptionPane.WARNING_MESSAGE
            );

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database error: "
                            + e.getMessage()
            );
        }
    }

    private void loadBookings() {

        try {

            List<Booking> bookings =
                    bookingService.getAllBookings();

            tableModel.setRowCount(0);

            for (Booking booking : bookings) {

                tableModel.addRow(
                        new Object[]{
                                booking.getBookingId(),
                                booking.getCustomerId(),
                                booking.getRoomId(),
                                booking.getCheckIn(),
                                booking.getCheckOut(),
                                booking.getGuests(),
                                booking.getStatus()
                        }
                );
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load bookings: "
                            + e.getMessage()
            );
        }
    }

    private void clearFields() {

        customerIdField.setText("");
        roomIdField.setText("");
        checkInField.setText("");
        checkOutField.setText("");
        guestsField.setText("");
    }
}