package com.smartstay.interfaces;

import com.smartstay.model.Booking;

import java.sql.SQLException;
import java.util.List;

public interface BookingOperations {

    void createBooking(Booking booking) throws SQLException;

    void cancelBooking(int bookingId) throws SQLException;

    List<Booking> getBookings() throws SQLException;
}