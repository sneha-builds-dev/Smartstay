package com.smartstay.transport;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransportDAO {

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/smartstay",
                "root",
                "9123"
        );
    }

    public List<Vehicle> getAvailableVehicles() throws SQLException {
        List<Vehicle> vehicles = new ArrayList<>();

        String sql = """
                SELECT * FROM vehicles
                WHERE status = 'Available'
                ORDER BY price_per_km
                """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                vehicles.add(new Vehicle(
                        rs.getInt("vehicle_id"),
                        rs.getString("vehicle_number"),
                        rs.getString("vehicle_type"),
                        rs.getInt("capacity"),
                        rs.getDouble("price_per_km"),
                        rs.getString("status")
                ));
            }
        }

        return vehicles;
    }

    public List<Driver> getAvailableDrivers() throws SQLException {
        List<Driver> drivers = new ArrayList<>();

        String sql = """
                SELECT * FROM drivers
                WHERE status = 'Available'
                ORDER BY driver_name
                """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                drivers.add(new com.smartstay.transport.Driver(
                        rs.getInt("driver_id"),
                        rs.getString("driver_name"),
                        rs.getString("phone"),
                        rs.getString("license_number"),
                        rs.getString("status")
                ));
            }
        }

        return drivers;
    }
}