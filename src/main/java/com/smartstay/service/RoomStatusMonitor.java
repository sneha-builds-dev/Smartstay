package com.smartstay.service;

import com.smartstay.dao.RoomDAO;
import com.smartstay.model.Room;

import java.sql.SQLException;
import java.util.List;

public class RoomStatusMonitor implements Runnable {

    private final RoomDAO roomDAO;

    private volatile boolean running = true;

    public RoomStatusMonitor() {
        roomDAO = new RoomDAO();
    }

    @Override
    public void run() {

        while (running) {

            try {

                List<Room> rooms =
                        roomDAO.getAllRooms();

                System.out.println(
                        "\n--- Room Status Monitor ---"
                );

                for (Room room : rooms) {

                    System.out.println(
                            "Room "
                                    + room.getRoomNumber()
                                    + " : "
                                    + room.getStatus()
                    );
                }

                System.out.println(
                        "Monitor running in background..."
                );

                Thread.sleep(10000);

            } catch (SQLException e) {

                System.out.println(
                        "Room monitor database error: "
                                + e.getMessage()
                );

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();

                System.out.println(
                        "Room status monitor stopped."
                );

                break;
            }
        }
    }

    public void stopMonitor() {
        running = false;
    }
}