package com.smartstay.dao;

import com.smartstay.model.Room;
import com.smartstay.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    public void addRoom(Room room) throws SQLException {

        String sql = "INSERT INTO rooms " +
                "(room_number, room_type, price_per_night, " +
                "capacity, status, facilities) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    room.getRoomNumber()
            );

            statement.setString(
                    2,
                    room.getRoomType()
            );

            statement.setDouble(
                    3,
                    room.getPricePerNight()
            );

            statement.setInt(
                    4,
                    room.getCapacity()
            );

            statement.setString(
                    5,
                    room.getStatus()
            );

            statement.setString(
                    6,
                    room.getFacilities()
            );

            statement.executeUpdate();
        }
    }

    public List<Room> getAllRooms()
            throws SQLException {

        List<Room> rooms = new ArrayList<>();

        String sql = "SELECT * FROM rooms";

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                Room room = new Room(
                        resultSet.getInt("room_id"),
                        resultSet.getString("room_number"),
                        resultSet.getString("room_type"),
                        resultSet.getDouble(
                                "price_per_night"
                        ),
                        resultSet.getInt("capacity"),
                        resultSet.getString("status"),
                        resultSet.getString("facilities")
                );

                rooms.add(room);
            }
        }

        return rooms;
    }

    public Room getRoomById(int roomId)
            throws SQLException {

        String sql =
                "SELECT * FROM rooms WHERE room_id = ?";

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, roomId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    return new Room(
                            resultSet.getInt("room_id"),
                            resultSet.getString(
                                    "room_number"
                            ),
                            resultSet.getString(
                                    "room_type"
                            ),
                            resultSet.getDouble(
                                    "price_per_night"
                            ),
                            resultSet.getInt(
                                    "capacity"
                            ),
                            resultSet.getString(
                                    "status"
                            ),
                            resultSet.getString(
                                    "facilities"
                            )
                    );
                }
            }
        }

        return null;
    }

    public void updateRoomStatus(
            int roomId,
            String status
    ) throws SQLException {

        String sql =
                "UPDATE rooms SET status = ? " +
                        "WHERE room_id = ?";

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, status);
            statement.setInt(2, roomId);

            statement.executeUpdate();
        }
    }
}