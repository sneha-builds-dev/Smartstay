
package com.smartstay.gui;

import com.smartstay.service.LoginService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {

    private final Color NAVY = new Color(17, 25, 40);
    private final Color PANEL = new Color(29, 39, 56);
    private final Color GOLD = new Color(224, 184, 104);
    private final Color WHITE = new Color(245, 247, 250);
    private final Color MUTED = new Color(170, 180, 195);

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    private final LoginService loginService;

    public LoginFrame() {

        loginService = new LoginService();

        setTitle("SmartStay - Hotel Management System");
        setSize(1200, 750);
        setMinimumSize(new Dimension(1000, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        buildLoginUI();
    }

    private void buildLoginUI() {

        JPanel root = new JPanel(new GridLayout(1, 2));
        root.setBackground(NAVY);

        root.add(createBrandPanel());
        root.add(createLoginPanel());

        setContentPane(root);
    }

    private JPanel createBrandPanel() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(NAVY);
        panel.setBorder(new EmptyBorder(65, 45, 40, 35));

        JLabel symbol = new JLabel("✦");
        symbol.setFont(new Font("Serif", Font.PLAIN, 55));
        symbol.setForeground(GOLD);
        symbol.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel brand = new JLabel("SmartStay");
        brand.setFont(new Font("Serif", Font.BOLD, 43));
        brand.setForeground(GOLD);
        brand.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("HOTEL MANAGEMENT SYSTEM");
        subtitle.setFont(new Font("Arial", Font.BOLD, 12));
        subtitle.setForeground(WHITE);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel tagline = new JLabel(
                "<html>Manage Smarter<br>Serve Better. Grow Together.</html>"
        );
        tagline.setFont(new Font("Arial", Font.PLAIN, 18));
        tagline.setForeground(MUTED);
        tagline.setAlignmentX(Component.LEFT_ALIGNMENT);
        tagline.setBorder(new EmptyBorder(28, 0, 0, 0));

        JPanel featurePanel = new JPanel(new GridLayout(0, 1, 0, 15));
        featurePanel.setOpaque(false);
        featurePanel.setBorder(new EmptyBorder(45, 0, 0, 0));
        featurePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        featurePanel.add(createFeature("01", "Room Management"));
        featurePanel.add(createFeature("02", "Booking Management"));
        featurePanel.add(createFeature("03", "Smart Recommendations"));
        featurePanel.add(createFeature("04", "Billing & Reports"));

        panel.add(symbol);
        panel.add(Box.createVerticalStrut(5));
        panel.add(brand);
        panel.add(Box.createVerticalStrut(5));
        panel.add(subtitle);
        panel.add(tagline);
        panel.add(featurePanel);
        panel.add(Box.createVerticalGlue());

        JLabel footer = new JLabel("A Better Stay. Every Day.");
        footer.setForeground(GOLD);
        footer.setFont(new Font("Arial", Font.ITALIC, 13));
        footer.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(footer);

        return panel;
    }

    private JPanel createFeature(String number, String text) {

        JPanel feature = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 3));
        feature.setOpaque(false);

        JLabel numberLabel = new JLabel(number);
        numberLabel.setForeground(GOLD);
        numberLabel.setFont(new Font("Arial", Font.BOLD, 13));

        JLabel textLabel = new JLabel(text);
        textLabel.setForeground(WHITE);
        textLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        feature.add(numberLabel);
        feature.add(textLabel);

        return feature;
    }

    private JPanel createLoginPanel() {

        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(new Color(23, 32, 47));
        outer.setBorder(new EmptyBorder(35, 25, 35, 25));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(PANEL);
        card.setBorder(new EmptyBorder(35, 32, 35, 32));

        JLabel welcome = new JLabel("Welcome Back");
        welcome.setFont(new Font("Arial", Font.BOLD, 28));
        welcome.setForeground(WHITE);
        welcome.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel description = new JLabel("Sign in to your SmartStay account");
        description.setFont(new Font("Arial", Font.PLAIN, 13));
        description.setForeground(MUTED);
        description.setAlignmentX(Component.LEFT_ALIGNMENT);

        usernameField = new JTextField();
        styleField(usernameField);
        usernameField.setText("");

        passwordField = new JPasswordField();
        styleField(passwordField);

        JLabel usernameLabel = createLabel("USERNAME");
        JLabel passwordLabel = createLabel("PASSWORD");

        JCheckBox showPassword = new JCheckBox("Show password");
        showPassword.setOpaque(false);
        showPassword.setForeground(MUTED);
        showPassword.setFocusPainted(false);
        showPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

        showPassword.addActionListener(e -> {
            passwordField.setEchoChar(
                    showPassword.isSelected() ? (char) 0 : '\u2022'
            );
        });

        loginButton = new JButton("SIGN IN   →");
        loginButton.setFont(new Font("Arial", Font.BOLD, 15));
        loginButton.setForeground(new Color(30, 28, 20));
        loginButton.setBackground(GOLD);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        loginButton.setPreferredSize(new Dimension(300, 46));

        JLabel security = new JLabel("Authorized hotel staff only");
        security.setForeground(MUTED);
        security.setFont(new Font("Arial", Font.PLAIN, 11));
        security.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(welcome);
        card.add(Box.createVerticalStrut(8));
        card.add(description);
        card.add(Box.createVerticalStrut(32));

        card.add(usernameLabel);
        card.add(Box.createVerticalStrut(7));
        card.add(usernameField);
        card.add(Box.createVerticalStrut(20));

        card.add(passwordLabel);
        card.add(Box.createVerticalStrut(7));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(8));

        card.add(showPassword);
        card.add(Box.createVerticalStrut(25));

        card.add(loginButton);
        card.add(Box.createVerticalStrut(20));
        card.add(security);

        loginButton.addActionListener(e -> login());
        passwordField.addActionListener(e -> login());
        usernameField.addActionListener(e -> passwordField.requestFocusInWindow());

        outer.add(card, new GridBagConstraints());

        return outer;
    }

    private JLabel createLabel(String text) {

        JLabel label = new JLabel(text);
        label.setForeground(GOLD);
        label.setFont(new Font("Arial", Font.BOLD, 11));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        return label;
    }

    private void styleField(JTextField field) {

        field.setFont(new Font("Arial", Font.PLAIN, 15));
        field.setForeground(WHITE);
        field.setBackground(new Color(38, 49, 67));
        field.setCaretColor(GOLD);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(72, 86, 106)),
                new EmptyBorder(10, 12, 10, 12)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        field.setPreferredSize(new Dimension(300, 44));
    }

    private void login() {

        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter both username and password.",
                    "Missing Details",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        loginButton.setEnabled(false);
        loginButton.setText("SIGNING IN...");

        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {

            private Exception loginError;

            @Override
            protected Boolean doInBackground() {

                try {
                    return loginService.login(username, password);
                } catch (Exception e) {
                    loginError = e;
                    return false;
                }
            }

            @Override
            protected void done() {

                loginButton.setEnabled(true);
                loginButton.setText("SIGN IN   →");

                try {
                    boolean success = get();

                    if (success) {
                        dispose();
                        new DashboardFrame().setVisible(true);
                    } else if (loginError != null) {
                        JOptionPane.showMessageDialog(
                                LoginFrame.this,
                                "Login failed. Check your database connection.",
                                "Login Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    } else {
                        JOptionPane.showMessageDialog(
                                LoginFrame.this,
                                "Invalid username or password.",
                                "Login Failed",
                                JOptionPane.WARNING_MESSAGE
                        );
                    }

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                            LoginFrame.this,
                            "Unable to complete login: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };

        worker.execute();
    }
}
