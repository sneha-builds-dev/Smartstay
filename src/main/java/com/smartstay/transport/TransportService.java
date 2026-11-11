package com.smartstay.transport;

public class TransportService {

    public double calculateFare(double distanceKm, double pricePerKm) {
        if (distanceKm <= 0 || pricePerKm < 0) {
            throw new IllegalArgumentException(
                    "Distance must be positive and price cannot be negative."
            );
        }

        return Math.round(distanceKm * pricePerKm * 100.0) / 100.0;
    }

    public boolean canCarryPassengers(Vehicle vehicle, int passengers) {
        return vehicle != null
                && passengers > 0
                && passengers <= vehicle.getCapacity();
    }

    public boolean isVehicleAvailable(Vehicle vehicle) {
        return vehicle != null
                && "Available".equalsIgnoreCase(vehicle.getStatus());
    }
}