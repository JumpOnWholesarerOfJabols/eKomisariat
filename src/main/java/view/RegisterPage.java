package main.java.view;

import main.java.database.DatabaseOperations;
import main.java.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegisterPage {
    private final DatabaseOperations<User> usersDatabase;

    public RegisterPage(DatabaseOperations<User> usersDatabase) {
        this.usersDatabase = usersDatabase;
    }

    JTextField nameField;
    JTextField surnameField;
    JTextField emailField;
    JPasswordField passwordField;
    JButton registerButton;
    JLabel passwordInfoLabel;

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

        nameField = new JTextField();
        nameField.setBorder(BorderFactory.createTitledBorder("Imię"));

        surnameField = new JTextField();
        surnameField.setBorder(BorderFactory.createTitledBorder("Nazwisko"));

        emailField = new JTextField();
        emailField.setBorder(BorderFactory.createTitledBorder("Email"));

        passwordField = new JPasswordField();
        passwordField.setBorder(BorderFactory.createTitledBorder("Hasło"));

        passwordInfoLabel = new JLabel("Hasło musi zawierać minimum 6 znaków, jedną literę i jedną cyfre");

        registerButton = new JButton("Zarejestruj się");
        registerButton.setPreferredSize(new Dimension(120, 35));

        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    registerButton.doClick();
                }
            }
        });

        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(isDataValid()) {
                    usersDatabase.addItemToDatabase(new User(nameField.getText(), surnameField.getText(), emailField.getText(), new String(passwordField.getPassword())));
                    cardLayout.show(mainPanel, "loginPage");
                    nameField.setText("");
                    surnameField.setText("");
                    passwordField.setText("");
                    emailField.setText("");
                    JOptionPane.showMessageDialog(null, "Zarejestrowano pomyślnie");
                } else {
                    JOptionPane.showMessageDialog(null, "Wypełnij poprawnie wszystkie pola!");
                }

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
        mainRegisterPanel.add(passwordInfoLabel,gbc);
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.EAST;
        mainRegisterPanel.add(registerButton, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 7;
        mainRegisterPanel.add(loginLabel, gbc);
        registerPanel.add(mainRegisterPanel);

        return registerPanel;
    }

    private boolean isDataValid() {
        boolean isValid = isNameValid() && isSurnameValid() && isEmailValid() && isPasswordValid();

        if(!isNameValid()) {
            nameField.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.RED),
                    "Imię"
            ));
        } else {
            nameField.setBorder(BorderFactory.createTitledBorder("Imię"));
        }

        if(!isSurnameValid()) {
            surnameField.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.RED),
                    "Nazwisko"
            ));
        } else {
            surnameField.setBorder(BorderFactory.createTitledBorder("Nazwisko"));
        }

        if(!isEmailValid()) {
            emailField.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.RED),
                    "Email"
            ));
        } else {
            emailField.setBorder(BorderFactory.createTitledBorder("Email"));
        }

        if(!isPasswordValid()) {
            passwordField.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.RED),
                    "Hasło"
            ));
        } else {
            passwordField.setBorder(BorderFactory.createTitledBorder("Hasło"));
        }

        return isValid;
    }

    private boolean isNameValid() {
        String text = nameField.getText();
        return text != null && text.matches("[A-Z].{2,}");
    }
    private boolean isSurnameValid() {
        String text = surnameField.getText();
        return text != null && text.matches("[A-Z].{2,}");
    }

    private boolean isPasswordValid() {
        String password = new String(passwordField.getPassword());
        return password.matches("^(?=.*[A-Z])(?=.*\\d).{6,}$");
    }

    private boolean isEmailValid() {
        String email = new String(emailField.getText());
        return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }
}

