package main.java.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegisterPage {

    public JPanel generateRegisterPage(CardLayout cardLayout, JPanel mainPanel) {
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new GridBagLayout());
        registerPanel.setBackground(new Color(35,78,117));
        registerPanel.setBorder(BorderFactory.createEmptyBorder(60, 145, 60, 145));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 20, 10, 20);

        JPanel mainRegisterPanel = new JPanel();
        mainRegisterPanel.setLayout(new GridBagLayout());
        mainRegisterPanel.setPreferredSize(new Dimension(800, 500));

        JTextField nameField = new JTextField();
        nameField.setBorder(BorderFactory.createTitledBorder("Imię"));

        JTextField surnameField = new JTextField();
        surnameField.setBorder(BorderFactory.createTitledBorder("Nazwisko"));

        JTextField emailField = new JTextField();
        emailField.setBorder(BorderFactory.createTitledBorder("Email"));

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBorder(BorderFactory.createTitledBorder("Hasło"));

        JLabel loginLabel = new JLabel("<html><u>Masz konto? Zaloguj się</u></html>");

        loginLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "loginPage");
            }

        });

        mainRegisterPanel.add(nameField, gbc);
        gbc.gridy = 1;
        mainRegisterPanel.add(surnameField, gbc);
        gbc.gridy = 2;
        mainRegisterPanel.add(emailField, gbc);
        gbc.gridy = 3;
        mainRegisterPanel.add(passwordField, gbc);
        gbc.gridy = 4;
        mainRegisterPanel.add(loginLabel, gbc);
        registerPanel.add(mainRegisterPanel);

        return registerPanel;
    }
}

