package main.java.view;

import main.java.database.DatabaseOperations;
import main.java.database.UsersDatabase;
import main.java.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class RegisterPage {
    String folderPath = "src/main/resources/users/";
    DatabaseOperations<User> usersDatabase = new UsersDatabase(folderPath);

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
        mainRegisterPanel.setBackground(new Color(81,145,203));

        JLabel titleField = new JLabel("Rejestracja", SwingConstants.CENTER);
        titleField.setFont(new Font("Arial", Font.BOLD, 28));
        titleField.setForeground(Color.white);

        JTextField nameField = new JTextField();
        nameField.setBorder(BorderFactory.createTitledBorder("Imię"));

        JTextField surnameField = new JTextField();
        surnameField.setBorder(BorderFactory.createTitledBorder("Nazwisko"));

        JTextField emailField = new JTextField();
        emailField.setBorder(BorderFactory.createTitledBorder("Email"));

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBorder(BorderFactory.createTitledBorder("Hasło"));

        JButton registerButton = new JButton("Zarejestruj się");
        registerButton.setPreferredSize(new Dimension(120, 35));
        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                usersDatabase.addItemToDatabase(new User(nameField.getText(), surnameField.getText(), emailField.getText(), new String(passwordField.getPassword())));
                cardLayout.show(mainPanel, "loginPage");
                nameField.setText("");
                surnameField.setText("");
                passwordField.setText("");
                emailField.setText("");
                JOptionPane.showMessageDialog(null, "Zarejestrowano pomyślnie");
            }
        });

        JLabel loginLabel = new JLabel("<html><u>Masz konto? Zaloguj się</u></html>");
        loginLabel.setForeground(Color.white);

        loginLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                cardLayout.show(mainPanel, "loginPage");
            }

        });

        //gbc.anchor = GridBagConstraints.CENTER;
        mainRegisterPanel.add(titleField, gbc);
        gbc.gridy = 1;
        mainRegisterPanel.add(nameField, gbc);
        gbc.gridy = 2;
        mainRegisterPanel.add(surnameField, gbc);
        gbc.gridy = 3;
        mainRegisterPanel.add(emailField, gbc);
        gbc.gridy = 4;
        mainRegisterPanel.add(passwordField, gbc);
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.EAST;
        mainRegisterPanel.add(registerButton, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 6;
        mainRegisterPanel.add(loginLabel, gbc);
        registerPanel.add(mainRegisterPanel);

        return registerPanel;
    }
}

