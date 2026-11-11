package com.smartstay.dao;

import com.smartstay.model.Bill;
import com.smartstay.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BillDAO {

    public void addBill(Bill bill) throws SQLException {

        String sql = "INSERT INTO bills " +
                "(booking_id, room_charges, additional_charges, " +
                "discount, total_amount, payment_status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, bill.getBookingId());
            statement.setDouble(2, bill.getRoomCharges());
            statement.setDouble(3, bill.getAdditionalCharges());
            statement.setDouble(4, bill.getDiscount());
            statement.setDouble(5, bill.getTotalAmount());
            statement.setString(6, bill.getPaymentStatus());

            statement.executeUpdate();
        }
    }

    public void updatePaymentStatus(
            int bookingId,
            String paymentStatus
    ) throws SQLException {

        String sql =
                "UPDATE bills SET payment_status = ? " +
                        "WHERE booking_id = ?";

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, paymentStatus);
            statement.setInt(2, bookingId);

            statement.executeUpdate();
        }
    }
}