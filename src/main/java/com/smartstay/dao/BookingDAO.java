package com.smartstay.dao;

import com.smartstay.model.Booking;
import com.smartstay.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    public void addBooking(Booking booking)
            throws SQLException {

        String sql =
                "INSERT INTO bookings " +
                        "(customer_id, room_id, check_in, " +
                        "check_out, guests, status) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    booking.getCustomerId()
            );

            statement.setInt(
                    2,
                    booking.getRoomId()
            );

            statement.setDate(
                    3,
                    Date.valueOf(
                            booking.getCheckIn()
                    )
            );

            statement.setDate(
                    4,
                    Date.valueOf(
                            booking.getCheckOut()
                    )
            );

            statement.setInt(
                    5,
                    booking.getGuests()
            );

            statement.setString(
                    6,
                    booking.getStatus()
            );

            statement.executeUpdate();
        }
    }

    public boolean hasOverlappingBooking(
            int roomId,
            Date checkIn,
            Date checkOut
    ) throws SQLException {

        String sql =
                "SELECT COUNT(*) FROM bookings " +
                        "WHERE room_id = ? " +
                        "AND status NOT IN " +
                        "('Cancelled', 'Checked-out') " +
                        "AND check_in < ? " +
                        "AND check_out > ?";

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, roomId);
            statement.setDate(2, checkOut);
            statement.setDate(3, checkIn);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    public List<Booking> getAllBookings()
            throws SQLException {

        List<Booking> bookings =
                new ArrayList<>();

        String sql = "SELECT * FROM bookings";

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                Booking booking = new Booking(
                        resultSet.getInt("booking_id"),
                        resultSet.getInt("customer_id"),
                        resultSet.getInt("room_id"),
                        resultSet.getDate(
                                "check_in"
                        ).toLocalDate(),
                        resultSet.getDate(
                                "check_out"
                        ).toLocalDate(),
                        resultSet.getInt("guests"),
                        resultSet.getString("status")
                );

                bookings.add(booking);
            }
        }

        return bookings;
    }

    public void updateBookingStatus(
            int bookingId,
            String status
    ) throws SQLException {

        String sql =
                "UPDATE bookings SET status = ? " +
                        "WHERE booking_id = ?";

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, status);
            statement.setInt(2, bookingId);

            statement.executeUpdate();
        }
    }
}