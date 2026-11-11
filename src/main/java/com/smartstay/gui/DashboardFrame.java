package com.smartstay.gui;

import com.smartstay.util.DBConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DashboardFrame extends JFrame {

    // ================= COLORS =================

    private static final Color BG =
            new Color(18, 23, 32);

    private static final Color SIDEBAR =
            new Color(13, 18, 27);

    private static final Color PANEL =
            new Color(27, 34, 46);

    private static final Color TEXT =
            new Color(240, 243, 248);

    private static final Color MUTED =
            new Color(155, 164, 178);

    private static final Color GOLD =
            new Color(218, 177, 76);

    // ================= UI =================

    private JPanel contentPanel;
    private JPanel roomGrid;

    private JLabel roomsValue;
    private JLabel bookingsValue;
    private JLabel occupiedValue;
    private JLabel revenueValue;

    private JLabel occupancyValue;
    private JLabel occupancyDetail;

    private DefaultListModel<String> bookingListModel;

    // ================= CONSTRUCTOR =================

    public DashboardFrame() {

        setTitle(
                "SmartStay - Intelligent Hotel Management System"
        );

        setSize(1250, 760);

        setMinimumSize(
                new Dimension(1050, 650)
        );

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);

        buildDashboard();

        loadDashboardData();
    }

    // =========================================================
    // BUILD DASHBOARD
    // =========================================================

    private void buildDashboard() {

        JPanel root =
                new JPanel(new BorderLayout());

        root.setBackground(BG);

        root.add(
                createSidebar(),
                BorderLayout.WEST
        );

        contentPanel =
                new JPanel(
                        new BorderLayout(18, 18)
                );

        contentPanel.setBackground(BG);

        contentPanel.setBorder(
                new EmptyBorder(
                        22,
                        22,
                        22,
                        22
                )
        );

        contentPanel.add(
                createHeader(),
                BorderLayout.NORTH
        );

        contentPanel.add(
                createMainContent(),
                BorderLayout.CENTER
        );

        root.add(
                contentPanel,
                BorderLayout.CENTER
        );

        setContentPane(root);
    }

    // =========================================================
    // SIDEBAR
    // =========================================================

    private JPanel createSidebar() {

        JPanel sidebar =
                new JPanel(
                        new BorderLayout()
                );

        sidebar.setPreferredSize(
                new Dimension(220, 0)
        );

        sidebar.setBackground(SIDEBAR);

        // Logo

        JPanel logoPanel =
                new JPanel();

        logoPanel.setLayout(
                new BoxLayout(
                        logoPanel,
                        BoxLayout.Y_AXIS
                )
        );

        logoPanel.setBackground(SIDEBAR);

        logoPanel.setBorder(
                new EmptyBorder(
                        25,
                        22,
                        20,
                        15
                )
        );

        JLabel icon =
                new JLabel("S");

        icon.setForeground(GOLD);

        icon.setFont(
                new Font(
                        "Serif",
                        Font.BOLD,
                        32
                )
        );

        JLabel logo =
                new JLabel("SMARTSTAY");

        logo.setForeground(TEXT);

        logo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        19
                )
        );

        JLabel subtitle =
                new JLabel(
                        "HOTEL MANAGEMENT"
                );

        subtitle.setForeground(MUTED);

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        9
                )
        );

        logoPanel.add(icon);

        logoPanel.add(
                Box.createVerticalStrut(3)
        );

        logoPanel.add(logo);

        logoPanel.add(subtitle);

        sidebar.add(
                logoPanel,
                BorderLayout.NORTH
        );

        // Menu

        JPanel menu =
                new JPanel();

        menu.setLayout(
                new BoxLayout(
                        menu,
                        BoxLayout.Y_AXIS
                )
        );

        menu.setBackground(SIDEBAR);

        menu.setBorder(
                new EmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        JButton dashboard =
                createMenuButton(
                        "Dashboard",
                        true
                );

        JButton rooms =
                createMenuButton(
                        "Room Management",
                        false
                );

        JButton customers =
                createMenuButton(
                        "Customer Management",
                        false
                );

        JButton bookings =
                createMenuButton(
                        "Bookings",
                        false
                );

        JButton checkInOut =
                createMenuButton(
                        "Check-in / Check-out",
                        false
                );

        JButton billing =
                createMenuButton(
                        "Billing",
                        false
                );

        JButton recommendation =
                createMenuButton(
                        "Smart Recommendation",
                        false
                );

        JButton reports =
                createMenuButton(
                        "Reports",
                        false
                );
        JButton transport = createMenuButton(
                "Transport Management",
                false
        );
        menu.add(dashboard);
        menu.add(rooms);
        menu.add(customers);
        menu.add(bookings);
        menu.add(checkInOut);
        menu.add(billing);
        menu.add(recommendation);
        menu.add(reports);
        menu.add(transport);
        sidebar.add(
                menu,
                BorderLayout.CENTER
        );

        // Logout

        JButton logout =
                createMenuButton(
                        "Logout",
                        false
                );

        JPanel logoutPanel =
                new JPanel(
                        new BorderLayout()
                );

        logoutPanel.setBackground(SIDEBAR);

        logoutPanel.setBorder(
                new EmptyBorder(
                        10,
                        10,
                        20,
                        10
                )
        );

        logoutPanel.add(
                logout,
                BorderLayout.CENTER
        );

        sidebar.add(
                logoutPanel,
                BorderLayout.SOUTH
        );

        // Actions

        rooms.addActionListener(
                e -> openModule(
                        "SmartStay - Room Management",
                        new RoomPanel()
                )
        );

        customers.addActionListener(
                e -> openModule(
                        "SmartStay - Customer Management",
                        new CustomerPanel()
                )
        );
        transport.addActionListener(
                e -> openModule(
                        "SmartStay - Transport Management",
                        new TransportPanel()
                )
        );
        bookings.addActionListener(
                e -> openModule(
                        "SmartStay - Booking Management",
                        new BookingPanel()
                )
        );

        checkInOut.addActionListener(
                e -> openModule(
                        "SmartStay - Check-in / Check-out",
                        new CheckInOutPanel()
                )
        );

        billing.addActionListener(
                e -> openModule(
                        "SmartStay - Billing",
                        new BillingPanel()
                )
        );

        recommendation.addActionListener(
                e -> openModule(
                        "SmartStay - Smart Room Recommendation",
                        new RecommendationPanel()
                )
        );

        reports.addActionListener(
                e -> openModule(
                        "SmartStay - Reports",
                        new ReportsPanel()
                )
        );

        logout.addActionListener(e -> {

            dispose();

            new LoginFrame().setVisible(true);
        });

        return sidebar;
    }

    // =========================================================
    // HEADER
    // =========================================================

    private JPanel createHeader() {

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setOpaque(false);

        JPanel left =
                new JPanel();

        left.setLayout(
                new BoxLayout(
                        left,
                        BoxLayout.Y_AXIS
                )
        );

        left.setOpaque(false);

        JLabel title =
                new JLabel(
                        "Good Morning, Admin"
                );

        title.setForeground(TEXT);

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        27
                )
        );

        JLabel description =
                new JLabel(
                        "Here's what's happening at your hotel today."
                );

        description.setForeground(MUTED);

        description.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        left.add(title);

        left.add(
                Box.createVerticalStrut(5)
        );

        left.add(description);

        JLabel admin =
                new JLabel(
                        "ADMIN  •  SmartStay"
                );

        admin.setForeground(MUTED);

        admin.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        header.add(
                left,
                BorderLayout.WEST
        );

        header.add(
                admin,
                BorderLayout.EAST
        );

        return header;
    }

    // =========================================================
    // MAIN CONTENT
    // =========================================================

    private JPanel createMainContent() {

        JPanel main =
                new JPanel(
                        new BorderLayout(15, 15)
                );

        main.setOpaque(false);

        // Cards

        JPanel cards =
                new JPanel(
                        new GridLayout(
                                1,
                                4,
                                12,
                                12
                        )
                );

        cards.setOpaque(false);

        JPanel roomsCard =
                createStatCard(
                        "TOTAL ROOMS",
                        "0",
                        "Rooms in system"
                );

        roomsValue =
                (JLabel) roomsCard.getClientProperty(
                        "valueLabel"
                );

        JPanel bookingsCard =
                createStatCard(
                        "TOTAL BOOKINGS",
                        "0",
                        "All bookings"
                );

        bookingsValue =
                (JLabel) bookingsCard.getClientProperty(
                        "valueLabel"
                );

        JPanel occupiedCard =
                createStatCard(
                        "OCCUPIED ROOMS",
                        "0",
                        "Currently occupied"
                );

        occupiedValue =
                (JLabel) occupiedCard.getClientProperty(
                        "valueLabel"
                );

        JPanel revenueCard =
                createStatCard(
                        "PAID REVENUE",
                        "₹0",
                        "Total collected"
                );

        revenueValue =
                (JLabel) revenueCard.getClientProperty(
                        "valueLabel"
                );

        cards.add(roomsCard);
        cards.add(bookingsCard);
        cards.add(occupiedCard);
        cards.add(revenueCard);

        main.add(
                cards,
                BorderLayout.NORTH
        );

        // Middle

        JPanel middle =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                15,
                                15
                        )
                );

        middle.setOpaque(false);

        middle.add(
                createOccupancyPanel()
        );

        middle.add(
                createRoomStatusPanel()
        );

        main.add(
                middle,
                BorderLayout.CENTER
        );

        // Bottom

        JPanel bottom =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                15,
                                15
                        )
                );

        bottom.setOpaque(false);

        bottom.add(
                createRecentBookingsPanel()
        );

        bottom.add(
                createSmartRecommendationPanel()
        );

        main.add(
                bottom,
                BorderLayout.SOUTH
        );

        return main;
    }

    // =========================================================
    // STAT CARD
    // =========================================================

    private JPanel createStatCard(
            String title,
            String value,
            String description
    ) {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setBackground(PANEL);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        48,
                                        58,
                                        72
                                )
                        ),
                        new EmptyBorder(
                                15,
                                18,
                                15,
                                18
                        )
                )
        );

        JLabel titleLabel =
                new JLabel(title);

        titleLabel.setForeground(MUTED);

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        10
                )
        );

        JLabel valueLabel =
                new JLabel(value);

        valueLabel.setForeground(TEXT);

        valueLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        25
                )
        );

        JLabel descriptionLabel =
                new JLabel(description);

        descriptionLabel.setForeground(MUTED);

        descriptionLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        11
                )
        );

        panel.add(
                titleLabel,
                BorderLayout.NORTH
        );

        panel.add(
                valueLabel,
                BorderLayout.CENTER
        );

        panel.add(
                descriptionLabel,
                BorderLayout.SOUTH
        );

        panel.putClientProperty(
                "valueLabel",
                valueLabel
        );

        return panel;
    }

    // =========================================================
    // OCCUPANCY PANEL
    // =========================================================

    private JPanel createOccupancyPanel() {

        JPanel panel =
                createSection(
                        "Today's Overview"
                );

        JPanel center =
                new JPanel(
                        new GridBagLayout()
                );

        center.setOpaque(false);

        JPanel circle =
                new JPanel(
                        new GridLayout(
                                2,
                                1
                        )
                );

        circle.setPreferredSize(
                new Dimension(
                        160,
                        160
                )
        );

        circle.setBackground(
                new Color(
                        42,
                        50,
                        64
                )
        );

        occupancyValue =
                new JLabel(
                        "0%",
                        SwingConstants.CENTER
                );

        occupancyValue.setForeground(TEXT);

        occupancyValue.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        occupancyDetail =
                new JLabel(
                        "0 of 0 rooms occupied",
                        SwingConstants.CENTER
                );

        occupancyDetail.setForeground(MUTED);

        occupancyDetail.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        11
                )
        );

        circle.add(occupancyValue);
        circle.add(occupancyDetail);

        center.add(circle);

        panel.add(
                center,
                BorderLayout.CENTER
        );

        return panel;
    }

    // =========================================================
    // ROOM STATUS
    // =========================================================

    private JPanel createRoomStatusPanel() {

        JPanel panel =
                createSection(
                        "Room Status"
                );

        roomGrid =
                new JPanel(
                        new GridLayout(
                                0,
                                4,
                                8,
                                8
                        )
                );

        roomGrid.setOpaque(false);

        JScrollPane scroll =
                new JScrollPane(
                        roomGrid
                );

        scroll.setBorder(null);

        scroll.getViewport()
                .setBackground(PANEL);

        panel.add(
                scroll,
                BorderLayout.CENTER
        );

        return panel;
    }

    // =========================================================
    // RECENT BOOKINGS
    // =========================================================

    private JPanel createRecentBookingsPanel() {

        JPanel panel =
                createSection(
                        "Recent Bookings"
                );

        bookingListModel =
                new DefaultListModel<>();

        JList<String> list =
                new JList<>(
                        bookingListModel
                );

        list.setBackground(PANEL);

        list.setForeground(TEXT);

        list.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        list.setFixedCellHeight(30);

        JScrollPane scroll =
                new JScrollPane(list);

        scroll.setBorder(null);

        scroll.getViewport()
                .setBackground(PANEL);

        panel.add(
                scroll,
                BorderLayout.CENTER
        );

        return panel;
    }

    // =========================================================
    // SMART RECOMMENDATION
    // =========================================================

    private JPanel createSmartRecommendationPanel() {

        JPanel panel =
                createSection(
                        "Smart Recommendation"
                );

        JPanel inner =
                new JPanel();

        inner.setLayout(
                new BoxLayout(
                        inner,
                        BoxLayout.Y_AXIS
                )
        );

        inner.setOpaque(false);

        JLabel heading =
                new JLabel(
                        "Customer-focused room suggestions"
                );

        heading.setForeground(TEXT);

        heading.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        JLabel text =
                new JLabel(
                        "<html>Use customer requirements such as " +
                                "guests, budget, room type and facilities " +
                                "to find a suitable available room.</html>"
                );

        text.setForeground(MUTED);

        text.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        JButton button =
                new JButton(
                        "Open Smart Recommendation"
                );

        button.setFocusPainted(false);

        button.setBackground(GOLD);

        button.setForeground(
                new Color(
                        30,
                        25,
                        15
                )
        );

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        button.addActionListener(
                e -> openModule(
                        "SmartStay - Smart Room Recommendation",
                        new RecommendationPanel()
                )
        );

        inner.add(heading);

        inner.add(
                Box.createVerticalStrut(10)
        );

        inner.add(text);

        inner.add(
                Box.createVerticalStrut(15)
        );

        inner.add(button);

        panel.add(
                inner,
                BorderLayout.CENTER
        );

        return panel;
    }

    // =========================================================
    // SECTION
    // =========================================================

    private JPanel createSection(
            String title
    ) {

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        panel.setBackground(PANEL);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        48,
                                        58,
                                        72
                                )
                        ),
                        new EmptyBorder(
                                14,
                                15,
                                14,
                                15
                        )
                )
        );

        JLabel label =
                new JLabel(title);

        label.setForeground(TEXT);

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        panel.add(
                label,
                BorderLayout.NORTH
        );

        return panel;
    }

    // =========================================================
    // MENU BUTTON
    // =========================================================

    private JButton createMenuButton(
            String text,
            boolean selected
    ) {

        JButton button =
                new JButton(text);

        button.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        42
                )
        );

        button.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        button.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        button.setForeground(
                selected
                        ? Color.WHITE
                        : MUTED
        );

        button.setBackground(
                selected
                        ? new Color(
                        53,
                        63,
                        80
                )
                        : SIDEBAR
        );

        button.setFont(
                new Font(
                        "Arial",
                        selected
                                ? Font.BOLD
                                : Font.PLAIN,
                        12
                )
        );

        button.setFocusPainted(false);

        button.setBorder(
                new EmptyBorder(
                        8,
                        15,
                        8,
                        8
                )
        );

        button.setOpaque(true);

        return button;
    }

    // =========================================================
    // OPEN MODULE
    // =========================================================

    private void openModule(
            String title,
            JPanel panel
    ) {

        JFrame frame =
                new JFrame(title);

        frame.setSize(
                900,
                600
        );

        frame.setLocationRelativeTo(this);

        frame.setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        frame.add(panel);

        frame.setVisible(true);
    }

    // =========================================================
    // LOAD DATABASE DATA
    // =========================================================

    private void loadDashboardData() {

        SwingWorker<Void, Void> worker =
                new SwingWorker<>() {

                    private int rooms;
                    private int bookings;
                    private int occupied;
                    private double revenue;

                    private List<RoomInfo> roomData =
                            new ArrayList<>();

                    private List<String> recentBookings =
                            new ArrayList<>();

                    @Override
                    protected Void doInBackground()
                            throws Exception {

                        try (
                                Connection connection =
                                        DBConnection.getConnection()
                        ) {

                            // Total rooms

                            try (
                                    PreparedStatement ps =
                                            connection.prepareStatement(
                                                    "SELECT COUNT(*) FROM rooms"
                                            );

                                    ResultSet rs =
                                            ps.executeQuery()
                            ) {

                                if (rs.next()) {
                                    rooms = rs.getInt(1);
                                }
                            }

                            // Total bookings

                            try (
                                    PreparedStatement ps =
                                            connection.prepareStatement(
                                                    "SELECT COUNT(*) FROM bookings"
                                            );

                                    ResultSet rs =
                                            ps.executeQuery()
                            ) {

                                if (rs.next()) {
                                    bookings = rs.getInt(1);
                                }
                            }

                            // Occupied rooms

                            try (
                                    PreparedStatement ps =
                                            connection.prepareStatement(
                                                    "SELECT COUNT(*) " +
                                                            "FROM rooms " +
                                                            "WHERE status = 'Occupied'"
                                            );

                                    ResultSet rs =
                                            ps.executeQuery()
                            ) {

                                if (rs.next()) {
                                    occupied = rs.getInt(1);
                                }
                            }

                            // Revenue

                            try (
                                    PreparedStatement ps =
                                            connection.prepareStatement(
                                                    "SELECT COALESCE(" +
                                                            "SUM(total_amount),0) " +
                                                            "FROM bills " +
                                                            "WHERE payment_status = 'Paid'"
                                            );

                                    ResultSet rs =
                                            ps.executeQuery()
                            ) {

                                if (rs.next()) {
                                    revenue = rs.getDouble(1);
                                }
                            }

                            // Room status

                            try (
                                    PreparedStatement ps =
                                            connection.prepareStatement(
                                                    "SELECT room_number, status " +
                                                            "FROM rooms " +
                                                            "ORDER BY room_number"
                                            );

                                    ResultSet rs =
                                            ps.executeQuery()
                            ) {

                                while (rs.next()) {

                                    roomData.add(
                                            new RoomInfo(
                                                    rs.getString(
                                                            "room_number"
                                                    ),
                                                    rs.getString(
                                                            "status"
                                                    )
                                            )
                                    );
                                }
                            }

                            // Recent bookings

                            try (
                                    PreparedStatement ps =
                                            connection.prepareStatement(
                                                    "SELECT b.booking_id, " +
                                                            "c.name, " +
                                                            "r.room_number, " +
                                                            "b.status " +
                                                            "FROM bookings b " +
                                                            "JOIN customers c " +
                                                            "ON b.customer_id = c.customer_id " +
                                                            "JOIN rooms r " +
                                                            "ON b.room_id = r.room_id " +
                                                            "ORDER BY b.booking_id DESC " +
                                                            "LIMIT 6"
                                            );

                                    ResultSet rs =
                                            ps.executeQuery()
                            ) {

                                while (rs.next()) {

                                    recentBookings.add(
                                            "Booking #"
                                                    + rs.getInt(
                                                    "booking_id"
                                            )
                                                    + "  •  "
                                                    + rs.getString(
                                                    "name"
                                            )
                                                    + "  •  Room "
                                                    + rs.getString(
                                                    "room_number"
                                            )
                                                    + "  •  "
                                                    + rs.getString(
                                                    "status"
                                            )
                                    );
                                }
                            }
                        }

                        return null;
                    }

                    @Override
                    protected void done() {

                        roomsValue.setText(
                                String.valueOf(rooms)
                        );

                        bookingsValue.setText(
                                String.valueOf(bookings)
                        );

                        occupiedValue.setText(
                                String.valueOf(occupied)
                        );

                        revenueValue.setText(
                                String.format(
                                        "₹%.2f",
                                        revenue
                                )
                        );

                        int percentage =
                                rooms == 0
                                        ? 0
                                        : occupied * 100 / rooms;

                        occupancyValue.setText(
                                percentage + "%"
                        );

                        occupancyDetail.setText(
                                occupied
                                        + " of "
                                        + rooms
                                        + " rooms occupied"
                        );

                        updateRoomGrid(
                                roomData
                        );

                        bookingListModel.clear();

                        if (recentBookings.isEmpty()) {

                            bookingListModel.addElement(
                                    "No bookings available."
                            );

                        } else {

                            for (
                                    String booking
                                    : recentBookings
                            ) {

                                bookingListModel.addElement(
                                        booking
                                );
                            }
                        }

                        contentPanel.revalidate();
                        contentPanel.repaint();
                    }
                };

        worker.execute();
    }

    // =========================================================
    // UPDATED ROOM GRID
    // =========================================================

    private void updateRoomGrid(
            List<RoomInfo> rooms
    ) {

        roomGrid.removeAll();

        if (rooms.isEmpty()) {

            JLabel emptyLabel =
                    new JLabel(
                            "No rooms found."
                    );

            emptyLabel.setForeground(MUTED);

            roomGrid.add(
                    emptyLabel
            );

        } else {

            for (
                    RoomInfo roomInfo
                    : rooms
            ) {

                JPanel roomPanel =
                        new JPanel(
                                new BorderLayout()
                        );

                roomPanel.setPreferredSize(
                        new Dimension(
                                80,
                                55
                        )
                );

                roomPanel.setBackground(
                        getRoomColor(
                                roomInfo.status
                        )
                );

                roomPanel.setBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        255,
                                        255,
                                        255,
                                        30
                                )
                        )
                );

                JLabel numberLabel =
                        new JLabel(
                                roomInfo.number,
                                SwingConstants.CENTER
                        );

                numberLabel.setForeground(
                        Color.WHITE
                );

                numberLabel.setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                14
                        )
                );

                JLabel statusLabel =
                        new JLabel(
                                roomInfo.status,
                                SwingConstants.CENTER
                        );

                statusLabel.setForeground(
                        new Color(
                                235,
                                235,
                                235
                        )
                );

                statusLabel.setFont(
                        new Font(
                                "Arial",
                                Font.PLAIN,
                                9
                        )
                );

                roomPanel.add(
                        numberLabel,
                        BorderLayout.CENTER
                );

                roomPanel.add(
                        statusLabel,
                        BorderLayout.SOUTH
                );

                roomGrid.add(
                        roomPanel
                );
            }
        }

        roomGrid.revalidate();
        roomGrid.repaint();
    }

    // =========================================================
    // ROOM COLOR
    // =========================================================

    private Color getRoomColor(
            String status
    ) {

        if (status == null) {
            return PANEL;
        }

        return switch (
                status.toLowerCase()
                ) {

            case "available" ->
                    new Color(
                            35,
                            125,
                            82
                    );

            case "reserved" ->
                    new Color(
                            190,
                            145,
                            45
                    );

            case "occupied" ->
                    new Color(
                            190,
                            65,
                            65
                    );

            case "cleaning" ->
                    new Color(
                            60,
                            105,
                            175
                    );

            case "maintenance" ->
                    new Color(
                            120,
                            75,
                            150
                    );

            default ->
                    PANEL;
        };
    }

    // =========================================================
    // ROOM INFO
    // =========================================================

    private static class RoomInfo {

        private final String number;
        private final String status;

        private RoomInfo(
                String number,
                String status
        ) {

            this.number = number;
            this.status = status;
        }
    }
}