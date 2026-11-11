package com.smartstay.gui;

import com.smartstay.model.Bill;
import com.smartstay.service.BillingService;

import javax.swing.*;
import java.awt.*;

public class BillingPanel extends JPanel {

    private final BillingService billingService;

    private JTextField bookingIdField;
    private JTextField roomChargesField;
    private JTextField additionalChargesField;
    private JTextField discountField;
    private JLabel totalLabel;

    public BillingPanel() {

        billingService = new BillingService();

        setLayout(new BorderLayout(15, 15));

        setBorder(
                BorderFactory.createEmptyBorder(
                        30, 30, 30, 30
                )
        );

        JLabel titleLabel = new JLabel(
                "Billing Management",
                SwingConstants.CENTER
        );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(
                new GridLayout(5, 2, 10, 10)
        );

        bookingIdField = new JTextField();
        roomChargesField = new JTextField();
        additionalChargesField = new JTextField();
        discountField = new JTextField("0");

        formPanel.add(new JLabel("Booking ID:"));
        formPanel.add(bookingIdField);

        formPanel.add(new JLabel("Room Charges:"));
        formPanel.add(roomChargesField);

        formPanel.add(new JLabel("Additional Charges:"));
        formPanel.add(additionalChargesField);

        formPanel.add(new JLabel("Discount:"));
        formPanel.add(discountField);

        totalLabel = new JLabel("Total: ₹0.00");

        totalLabel.setFont(
                new Font("Arial", Font.BOLD, 18)
        );

        formPanel.add(new JLabel("Final Amount:"));
        formPanel.add(totalLabel);

        add(
                formPanel,
                BorderLayout.CENTER
        );

        JButton generateButton =
                new JButton("Generate Bill");

        JButton paidButton =
                new JButton("Mark as Paid");

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(generateButton);
        buttonPanel.add(paidButton);

        add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        generateButton.addActionListener(
                e -> generateBill()
        );

        paidButton.addActionListener(
                e -> markAsPaid()
        );
    }

    private void generateBill() {

        try {

            int bookingId = Integer.parseInt(
                    bookingIdField
                            .getText()
                            .trim()
            );

            double roomCharges = Double.parseDouble(
                    roomChargesField
                            .getText()
                            .trim()
            );

            double additionalCharges =
                    Double.parseDouble(
                            additionalChargesField
                                    .getText()
                                    .trim()
                    );

            double discount = Double.parseDouble(
                    discountField
                            .getText()
                            .trim()
            );

            if (bookingId <= 0 ||
                    roomCharges < 0 ||
                    additionalCharges < 0 ||
                    discount < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter valid values."
                );

                return;
            }

            Bill bill =
                    billingService.generateBill(
                            bookingId,
                            roomCharges,
                            additionalCharges,
                            discount
                    );

            totalLabel.setText(
                    String.format(
                            "Total: ₹%.2f",
                            bill.getTotalAmount()
                    )
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Bill generated and saved successfully!\n"
                            + "Total Amount: ₹"
                            + String.format(
                            "%.2f",
                            bill.getTotalAmount()
                    )
            );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid numeric values."
            );

        } catch (RuntimeException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not save bill:\n"
                            + e.getMessage()
            );
        }
    }

    private void markAsPaid() {

        try {

            int bookingId = Integer.parseInt(
                    bookingIdField
                            .getText()
                            .trim()
            );

            if (bookingId <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter a valid Booking ID."
                );

                return;
            }

            billingService.markAsPaid(
                    bookingId
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Payment marked as Paid successfully!"
            );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid Booking ID."
            );

        } catch (java.sql.SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not update payment status:\n"
                            + e.getMessage()
            );
        }
    }
}