package com.smartstay.service;

import com.smartstay.dao.BookingDAO;
import com.smartstay.dao.RoomDAO;
import com.smartstay.exception.RoomNotAvailableException;
import com.smartstay.interfaces.BookingOperations;
import com.smartstay.model.Booking;
import com.smartstay.model.Room;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class BookingService implements BookingOperations {

    private final BookingDAO bookingDAO;
    private final RoomDAO roomDAO;

    public BookingService() {
        bookingDAO = new BookingDAO();
        roomDAO = new RoomDAO();
    }

    @Override
    public void createBooking(Booking booking)
            throws SQLException {

        try {

            Room room =
                    roomDAO.getRoomById(
                            booking.getRoomId()
                    );

            if (room == null) {

                throw new RoomNotAvailableException(
                        "Room does not exist."
                );
            }

            if (!room.getStatus()
                    .equalsIgnoreCase("Available")) {

                throw new RoomNotAvailableException(
                        "Room "
                                + room.getRoomNumber()
                                + " is currently "
                                + room.getStatus()
                                + "."
                );
            }

            if (booking.getGuests()
                    > room.getCapacity()) {

                throw new RoomNotAvailableException(
                        "Room capacity is only "
                                + room.getCapacity()
                                + " guests."
                );
            }

            if (!booking.getCheckOut()
                    .isAfter(
                            booking.getCheckIn()
                    )) {

                throw new IllegalArgumentException(
                        "Check-out date must be after "
                                + "check-in date."
                );
            }

            boolean overlapping =
                    bookingDAO.hasOverlappingBooking(
                            booking.getRoomId(),
                            Date.valueOf(
                                    booking.getCheckIn()
                            ),
                            Date.valueOf(
                                    booking.getCheckOut()
                            )
                    );

            if (overlapping) {

                throw new RoomNotAvailableException(
                        "This room is already booked "
                                + "for the selected dates."
                );
            }

            bookingDAO.addBooking(booking);

            roomDAO.updateRoomStatus(
                    booking.getRoomId(),
                    "Reserved"
            );

        } catch (RoomNotAvailableException e) {

            throw new RuntimeException(
                    e.getMessage(),
                    e
            );
        }
    }

    @Override
    public void cancelBooking(int bookingId)
            throws SQLException {

        bookingDAO.updateBookingStatus(
                bookingId,
                "Cancelled"
        );
    }

    @Override
    public List<Booking> getBookings()
            throws SQLException {

        return bookingDAO.getAllBookings();
    }

    public void addBooking(Booking booking)
            throws SQLException {

        createBooking(booking);
    }

    public List<Booking> getAllBookings()
            throws SQLException {

        return getBookings();
    }

    public void updateBookingStatus(
            int bookingId,
            String status
    ) throws SQLException {

        bookingDAO.updateBookingStatus(
                bookingId,
                status
        );
    }
}