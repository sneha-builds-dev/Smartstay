package com.smartstay.service;

import com.smartstay.model.Room;

import java.util.ArrayList;
import java.util.List;

public class RecommendationService {

    public List<Room> recommendRooms(List<Room> rooms,
                                     int guests,
                                     double budget,
                                     String preferredType,
                                     String requiredFacility) {

        List<Room> recommendedRooms = new ArrayList<>();

        for (Room room : rooms) {

            if (!room.getStatus().equalsIgnoreCase("Available")) {
                continue;
            }

            if (room.getCapacity() < guests) {
                continue;
            }

            int score = 0;

            // Budget preference
            if (room.getPricePerNight() <= budget) {
                score += 3;
            }

            // Room type preference
            if (preferredType != null &&
                    !preferredType.isEmpty() &&
                    room.getRoomType().equalsIgnoreCase(preferredType)) {

                score += 2;
            }

            // Facility preference
            if (requiredFacility != null &&
                    !requiredFacility.isEmpty() &&
                    room.getFacilities() != null &&
                    room.getFacilities()
                            .toLowerCase()
                            .contains(requiredFacility.toLowerCase())) {

                score += 2;
            }

            // Add room if it matches at least one preference
            if (score > 0) {
                recommendedRooms.add(room);
            }
        }

        // Sort rooms by price
        recommendedRooms.sort(
                (room1, room2) ->
                        Double.compare(
                                room1.getPricePerNight(),
                                room2.getPricePerNight()
                        )
        );

        return recommendedRooms;
    }
}