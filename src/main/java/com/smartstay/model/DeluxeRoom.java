package com.smartstay.model;

public class DeluxeRoom extends Room {

    public DeluxeRoom() {
        super();
    }

    public DeluxeRoom(
            int roomId,
            String roomNumber,
            double pricePerNight,
            int capacity,
            String status,
            String facilities
    ) {

        super(
                roomId,
                roomNumber,
                "Deluxe",
                pricePerNight,
                capacity,
                status,
                facilities
        );
    }

    @Override
    public double getPricePerNight() {

        return super.getPricePerNight() * 1.10;
    }
}