package com.smartstay.model;

public class StandardRoom extends Room {

    public StandardRoom() {
        super();
    }

    public StandardRoom(
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
                "Standard",
                pricePerNight,
                capacity,
                status,
                facilities
        );
    }

    @Override
    public double getPricePerNight() {

        return super.getPricePerNight();
    }
}