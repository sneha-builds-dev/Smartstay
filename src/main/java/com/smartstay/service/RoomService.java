package com.smartstay.service;

import com.smartstay.dao.RoomDAO;
import com.smartstay.model.Room;

import java.sql.SQLException;
import java.util.List;

public class RoomService {

    private final RoomDAO roomDAO;

    public RoomService() {
        roomDAO = new RoomDAO();
    }

    public void addRoom(Room room) throws SQLException {
        roomDAO.addRoom(room);
    }

    public List<Room> getAllRooms() throws SQLException {
        return roomDAO.getAllRooms();
    }

    public void updateRoomStatus(int roomId, String status)
            throws SQLException {
        roomDAO.updateRoomStatus(roomId, status);
    }
}