package com.smartstay.main;

import com.smartstay.gui.LoginFrame;
import com.smartstay.service.RoomStatusMonitor;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        // Start room status monitor in a background thread
        RoomStatusMonitor monitor =
                new RoomStatusMonitor();

        Thread monitorThread =
                new Thread(
                        monitor,
                        "RoomStatusMonitorThread"
                );

        monitorThread.setDaemon(true);

        monitorThread.start();

        // Start Swing GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {

            LoginFrame loginFrame =
                    new LoginFrame();

            loginFrame.setVisible(true);
        });
    }
}