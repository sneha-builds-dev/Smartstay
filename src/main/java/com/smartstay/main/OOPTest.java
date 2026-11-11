package com.smartstay.main;

import com.smartstay.model.DeluxeRoom;
import com.smartstay.model.Room;
import com.smartstay.model.StandardRoom;
import com.smartstay.model.Suite;

public class OOPTest {

    public static void main(String[] args) {

        Room standardRoom =
                new StandardRoom(
                        1,
                        "101",
                        2500,
                        2,
                        "Available",
                        "AC, WiFi"
                );

        Room deluxeRoom =
                new DeluxeRoom(
                        2,
                        "201",
                        3000,
                        2,
                        "Available",
                        "AC, WiFi, TV"
                );

        Room suite =
                new Suite(
                        3,
                        "301",
                        5000,
                        4,
                        "Available",
                        "AC, WiFi, TV, Mini Bar"
                );

        System.out.println(
                "Standard Room Price: ₹"
                        + standardRoom.getPricePerNight()
        );

        System.out.println(
                "Deluxe Room Price: ₹"
                        + deluxeRoom.getPricePerNight()
        );

        System.out.println(
                "Suite Price: ₹"
                        + suite.getPricePerNight()
        );
    }
}