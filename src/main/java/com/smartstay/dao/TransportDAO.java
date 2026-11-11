package com.smartstay.dao;

import com.smartstay.transport.Driver;
import com.smartstay.transport.Vehicle;
import com.smartstay.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransportDAO {

    public List<Vehicle> getAvailableVehicles() throws SQLException {
        List<Vehicle> vehicles = new ArrayList<>();

        String sql = "SELECT * FROM vehicles WHERE status = 'Available'";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                Vehicle vehicle = new Vehicle();
                vehicle.setVehicleId(result.getInt("vehicle_id"));
                vehicle.setVehicleNumber(result.getString("vehicle_number"));
                vehicle.setVehicleType(result.getString("vehicle_type"));
                vehicle.setCapacity(result.getInt("capacity"));
                vehicle.setPricePerKm(result.getDouble("price_per_km"));
                vehicle.setStatus(result.getString("status"));

                vehicles.add(vehicle);
            }
        }

        return vehicles;
    }

    public boolean updateVehicleStatus(int vehicleId, String status)
            throws SQLException {

        String sql = "UPDATE vehicles SET status = ? WHERE vehicle_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, vehicleId);

            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateDriverStatus(int driverId, String status)
            throws SQLException {

        String sql = "UPDATE drivers SET status = ? WHERE driver_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, driverId);

            return ps.executeUpdate() > 0;
        }
    }

    public List<Driver> getAvailableDrivers() throws SQLException {
        List<Driver> drivers = new ArrayList<>();

        String sql = "SELECT * FROM drivers WHERE status = 'Available'";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                Driver driver = new Driver();
                driver.setDriverId(result.getInt("driver_id"));
                driver.setDriverName(result.getString("driver_name"));
                driver.setPhone(result.getString("phone"));
                driver.setLicenseNumber(result.getString("license_number"));
                driver.setStatus(result.getString("status"));

                drivers.add(driver);
            }
        }

        return drivers;
    }

    public int saveTransportBooking(
            int customerId,
            int vehicleId,
            int driverId,
            String pickupLocation,
            String dropLocation,
            java.time.LocalDateTime pickupTime,
            double distanceKm,
            double fare) throws java.sql.SQLException {

        String sql = "INSERT INTO transport_bookings "
                + "(customer_id, vehicle_id, driver_id, pickup_location, "
                + "drop_location, pickup_time, distance_km, fare, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (java.sql.Connection connection =
                     com.smartstay.util.DBConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {
                int bookingId;

                try (java.sql.PreparedStatement statement =
                             connection.prepareStatement(
                                     sql,
                                     java.sql.Statement.RETURN_GENERATED_KEYS)) {

                    statement.setInt(1, customerId);
                    statement.setInt(2, vehicleId);
                    statement.setInt(3, driverId);
                    statement.setString(4, pickupLocation);
                    statement.setString(5, dropLocation);
                    statement.setTimestamp(6,
                            java.sql.Timestamp.valueOf(pickupTime));
                    statement.setDouble(7, distanceKm);
                    statement.setDouble(8, fare);
                    statement.setString(9, "Booked");

                    statement.executeUpdate();

                    try (java.sql.ResultSet keys =
                                 statement.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new java.sql.SQLException(
                                    "Booking ID could not be generated.");
                        }
                        bookingId = keys.getInt(1);
                    }
                }

                try (java.sql.PreparedStatement ps =
                             connection.prepareStatement(
                                     "UPDATE vehicles SET status = 'Booked' "
                                             + "WHERE vehicle_id = ? "
                                             + "AND status = 'Available'")) {
                    ps.setInt(1, vehicleId);

                    if (ps.executeUpdate() != 1) {
                        throw new java.sql.SQLException(
                                "Selected vehicle is no longer available.");
                    }
                }

                try (java.sql.PreparedStatement ps =
                             connection.prepareStatement(
                                     "UPDATE drivers SET status = 'Booked' "
                                             + "WHERE driver_id = ? "
                                             + "AND status = 'Available'")) {
                    ps.setInt(1, driverId);

                    if (ps.executeUpdate() != 1) {
                        throw new java.sql.SQLException(
                                "Selected driver is no longer available.");
                    }
                }

                connection.commit();
                return bookingId;

            } catch (java.sql.SQLException ex) {
                connection.rollback();
                throw ex;
            }
        }
    }

    public java.util.List<Object[]> getTransportBookingHistory()
            throws java.sql.SQLException {

        java.util.List<Object[]> bookings = new java.util.ArrayList<>();

        String sql = """
        
                SELECT transport_booking_id, customer_id, vehicle_id,
               driver_id, pickup_location, drop_location,
               distance_km, fare, status
        FROM transport_bookings
        ORDER BY transport_booking_id DESC
        """;

        try (java.sql.Connection connection =
                     com.smartstay.util.DBConnection.getConnection();
             java.sql.PreparedStatement statement =
                     connection.prepareStatement(sql);
             java.sql.ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                bookings.add(new Object[]{
                        rs.getInt("transport_booking_id"),
                        rs.getInt("customer_id"),
                        rs.getInt("vehicle_id"),
                        rs.getInt("driver_id"),
                        rs.getString("pickup_location"),
                        rs.getString("drop_location"),
                        rs.getDouble("distance_km"),
                        rs.getDouble("fare"),
                        rs.getString("status")
                });
            }
        }

        return bookings;
    }
    public double getTotalTransportRevenue() throws java.sql.SQLException {

        String sql = "SELECT COALESCE(SUM(fare), 0) "
                + "FROM transport_bookings "
                + "WHERE status = 'Booked'";

        try (java.sql.Connection con =
                     com.smartstay.util.DBConnection.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble(1);
            }
        }

        return 0.0;
    }
    public boolean cancelTransportBooking(int bookingId)
            throws java.sql.SQLException {

        try (java.sql.Connection connection =
                     com.smartstay.util.DBConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {
                int vehicleId;
                int driverId;

                String findSql =
                        "SELECT vehicle_id, driver_id "
                                + "FROM transport_bookings "
                                + "WHERE transport_booking_id = ? "
                                + "AND status = 'Booked' FOR UPDATE";

                try (java.sql.PreparedStatement ps =
                             connection.prepareStatement(findSql)) {

                    ps.setInt(1, bookingId);

                    try (java.sql.ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            connection.rollback();
                            return false;
                        }

                        vehicleId = rs.getInt("vehicle_id");
                        driverId = rs.getInt("driver_id");
                    }
                }

                try (java.sql.PreparedStatement ps =
                             connection.prepareStatement(
                                     "UPDATE transport_bookings "
                                             + "SET status = 'Cancelled' "
                                             + "WHERE transport_booking_id = ?")) {

                    ps.setInt(1, bookingId);

                    if (ps.executeUpdate() != 1) {
                        throw new java.sql.SQLException(
                                "Booking cancellation failed.");
                    }
                }

                try (java.sql.PreparedStatement ps =
                             connection.prepareStatement(
                                     "UPDATE vehicles SET status = 'Available' "
                                             + "WHERE vehicle_id = ?")) {

                    ps.setInt(1, vehicleId);
                    ps.executeUpdate();
                }

                try (java.sql.PreparedStatement ps =
                             connection.prepareStatement(
                                     "UPDATE drivers SET status = 'Available' "
                                             + "WHERE driver_id = ?")) {

                    ps.setInt(1, driverId);
                    ps.executeUpdate();
                }

                connection.commit();
                return true;

            } catch (java.sql.SQLException ex) {
                connection.rollback();
                throw ex;
            }
        }
    }
    }