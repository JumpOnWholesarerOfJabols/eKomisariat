package main.java.view;

import main.java.database.Database;
import main.java.database.DatabaseOperations;
import main.java.model.Notification;
import main.java.model.NotificationType;
import main.java.model.User;
import org.apache.commons.codec.digest.DigestUtils;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;

import static main.java.utils.DataValidation.isNameValid;
import static main.java.utils.DataValidation.isPasswordValid;

public class AddPolicemanPage extends RegisterPage{
    private String generatedEmail;

    public AddPolicemanPage(DatabaseOperations<User> usersDatabase) {
        super(usersDatabase);
    }

    @Override
    public JPanel generatePage(CardLayout cardLayout, JPanel mainPanel) {
        rootPanel.setLayout(new GridBagLayout());
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

        JLabel titleField = new JLabel("Dodaj policjanta", SwingConstants.CENTER);
        titleField.setFont(new Font("Arial", Font.BOLD, 28));
        titleField.setForeground(Color.white);

        nameField = new JTextField();
        nameField.setBorder(BorderFactory.createTitledBorder("Imię"));

        surnameField = new JTextField();
        surnameField.setBorder(BorderFactory.createTitledBorder("Nazwisko"));

        passwordField = new JPasswordField();
        passwordField.setBorder(BorderFactory.createTitledBorder("Hasło"));

        passwordInfoLabel = new JLabel("Hasło musi zawierać minimum 6 znaków, jedną wielką literę i jedną cyfre");

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
                generatedEmail = generateEmail();
                if (!isEmailUnique()) {
                    JOptionPane.showMessageDialog(null, "Podany e-mail jest już zajęty!");
                } else if (isDataValid()) {
                    String password = Arrays.toString(passwordField.getPassword());
                    String hashedPassword = DigestUtils.sha256Hex(password);
                    User policeman = new User(
                            nameField.getText(),
                            surnameField.getText(),
                            generatedEmail,
                            hashedPassword
                    );
                    Database.getInstance().getUsersDatabase().addItemToDatabase(policeman);
                    cardLayout.show(mainPanel, "adminPage");
                    nameField.setText("");
                    surnameField.setText("");
                    passwordField.setText("");
                    JOptionPane.showMessageDialog(null, "Zarejestrowano pomyślnie. Wygenerowany email: " + generatedEmail);
                    Database.getInstance().getNotificationDatabase().addItemToDatabase(
                            new Notification(0, NotificationType.USER_CREATED, Database.getInstance().getUsersDatabase().getItemID(policeman), LocalDateTime.now())
                    );
                } else {
                    JOptionPane.showMessageDialog(null, "Wypełnij poprawnie wszystkie pola!");
                }

            }
        });

        JLabel backLabel = new JLabel("<html><u>Wróć</u></html>");
        backLabel.setForeground(Color.white);

        backLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "adminPage");
            }

        });

        //gbc.anchor = GridBagConstraints.CENTER;
        mainRegisterPanel.add(titleField, gbc);
        gbc.gridy = 1;
        mainRegisterPanel.add(nameField, gbc);
        gbc.gridy = 2;
        mainRegisterPanel.add(surnameField, gbc);
        gbc.gridy = 3;
        mainRegisterPanel.add(passwordField, gbc);
        gbc.gridy = 4;
        mainRegisterPanel.add(passwordInfoLabel,gbc);
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.EAST;
        mainRegisterPanel.add(registerButton, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 7;
        mainRegisterPanel.add(backLabel, gbc);
        rootPanel.add(mainRegisterPanel);

        return rootPanel;
    }

    private boolean isEmailUnique() {
        Map<Integer, User> policemen = Database.getInstance().getUsersDatabase().getAll();
        for(User policeman : policemen.values()) {
            if(policeman.getEmail().equals(generatedEmail)) {
                return false;
            }
        }

        return true;
    }

    private String generateEmail() {
        return nameField.getText() + "." + surnameField.getText() + "@eKomisariat.pl";
    }

    protected boolean isDataValid() {
        boolean isValid = isNameValid(nameField.getText()) && isNameValid(surnameField.getText()) && isPasswordValid(new String(passwordField.getPassword()));

        if(!isNameValid(nameField.getText())) {
            nameField.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.RED),
                    "Imię"
            ));
        } else {
            nameField.setBorder(BorderFactory.createTitledBorder("Imię"));
        }

        if(!isNameValid(nameField.getText())) {
            surnameField.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.RED),
                    "Nazwisko"
            ));
        } else {
            surnameField.setBorder(BorderFactory.createTitledBorder("Nazwisko"));
        }

        if(!isPasswordValid(nameField.getText())) {
            passwordField.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.RED),
                    "Hasło"
            ));
        } else {
            passwordField.setBorder(BorderFactory.createTitledBorder("Hasło"));
        }

        return isValid;
    }
}
