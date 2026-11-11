package com.smartstay.model;

public class Suite extends Room {

    public Suite() {
        super();
    }

    public Suite(
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
                "Suite",
                pricePerNight,
                capacity,
                status,
                facilities
        );
    }

    @Override
    public double getPricePerNight() {

        return super.getPricePerNight() * 1.25;
    }
}