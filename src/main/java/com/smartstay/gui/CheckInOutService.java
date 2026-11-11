package com.smartstay.service;

import com.smartstay.dao.BookingDAO;
import com.smartstay.dao.RoomDAO;

import java.sql.SQLException;

public class CheckInOutService {

    private final BookingDAO bookingDAO;
    private final RoomDAO roomDAO;

    public CheckInOutService() {
        bookingDAO = new BookingDAO();
        roomDAO = new RoomDAO();
    }

    public void checkIn(int bookingId, int roomId)
            throws SQLException {

        bookingDAO.updateBookingStatus(
                bookingId,
                "Checked-in"
        );

        roomDAO.updateRoomStatus(
                roomId,
                "Occupied"
        );
    }

    public void checkOut(int bookingId, int roomId)
            throws SQLException {

        bookingDAO.updateBookingStatus(
                bookingId,
                "Checked-out"
        );

        roomDAO.updateRoomStatus(
                roomId,
                "Cleaning"
        );
    }

    public void markRoomAvailable(int roomId)
            throws SQLException {

        roomDAO.updateRoomStatus(
                roomId,
                "Available"
        );
    }
}