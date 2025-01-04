package main.java.view.AuthenticationPage;

import main.java.database.Database;
import main.java.model.Notification;
import main.java.model.NotificationType;
import main.java.model.User;
import main.java.utils.DataValidation;
import org.apache.commons.codec.digest.DigestUtils;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.Arrays;

public class RegisterPanel extends AuthPanel {

    private JTextField nameField;
    private JTextField surnameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField repeatPasswordField;
    private JButton registerButton;
    private JLabel titleLabel;
    private JLabel passwordInfoLabel;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public RegisterPanel(CardLayout cardLayout, JPanel mainPanel) {
        super();
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;

        this.setBackground(new Color(30, 40, 70));
        this.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        createRegisterPanel();
    }

    private void createRegisterPanel() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        titleLabel = new JLabel("Rejestracja");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(titleLabel, gbc);

        gbc.gridy = 1;
        nameField = createStyledTextField("Imię");
        this.add(nameField, gbc);

        gbc.gridy = 2;
        surnameField = createStyledTextField("Nazwisko");
        this.add(surnameField, gbc);

        gbc.gridy = 3;
        emailField = createStyledTextField("Email");
        this.add(emailField, gbc);

        gbc.gridy = 4;
        passwordField = createStyledPasswordField("Hasło");
        this.add(passwordField, gbc);

        gbc.gridy = 5;
        repeatPasswordField = createStyledPasswordField("Powtorz Hasło");
        this.add(repeatPasswordField, gbc);

        gbc.gridy = 6;
        passwordInfoLabel = new JLabel("Hasło musi zawierać min. 6 znaków, wielką literę i cyfrę");
        passwordInfoLabel.setForeground(Color.LIGHT_GRAY);
        passwordInfoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        this.add(passwordInfoLabel, gbc);

        gbc.gridy = 7;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        registerButton = createModernButton("Zarejestruj się");
        registerButton.addActionListener(e -> handleRegister());
        this.add(registerButton, gbc);
    }

    private void handleRegister() {
        if (!DataValidation.isNameValid(nameField.getText())) {
            JOptionPane.showMessageDialog(null, "Imię jest nieprawidłowe. Upewnij się, że zawiera tylko litery.");
        } else if (!DataValidation.isNameValid(surnameField.getText())) {
            JOptionPane.showMessageDialog(null, "Nazwisko jest nieprawidłowe. Upewnij się, że zawiera tylko litery.");
        } else if (!DataValidation.isEmailValid(emailField.getText())) {
            JOptionPane.showMessageDialog(null, "Adres e-mail jest nieprawidłowy. Upewnij się, że jest poprawnie sformatowany.");
        } else if (!DataValidation.isPasswordValid(new String(passwordField.getPassword()))) {
            JOptionPane.showMessageDialog(null, "Hasło musi zawierać minimum 6 znaków, jedną wielką literę i jedną cyfrę.");
        } else if (!Arrays.equals(passwordField.getPassword(), repeatPasswordField.getPassword())) {
            JOptionPane.showMessageDialog(null, "Hasła się nie zgadzają. Upewnij się, że powtórzone hasło jest takie samo.");
        } else if (!DataValidation.isEmailFree(Database.getInstance().getUsersDatabase(), emailField.getText())) {
            JOptionPane.showMessageDialog(null, "Ten adres e-mail jest już zajęty. Wybierz inny.");
        } else {
            String password = Arrays.toString(passwordField.getPassword());
            String hashedPassword = DigestUtils.sha256Hex(password);
            User user = new User(nameField.getText(), surnameField.getText(), emailField.getText(), hashedPassword);
            Database.getInstance().getUsersDatabase().addItemToDatabase(user);
            cardLayout.show(mainPanel, "loginPage");
            resetFields();
            JOptionPane.showMessageDialog(null, "Zarejestrowano pomyślnie");
            Database.getInstance().getNotificationDatabase().addItemToDatabase(new Notification(0, NotificationType.USER_CREATED, Database.getInstance().getUsersDatabase().getItemID(user), LocalDateTime.now()));
        }
    }

    private void resetFields() {
        nameField.setText("");
        surnameField.setText("");
        passwordField.setText("");
        repeatPasswordField.setText("");
        emailField.setText("");
    }

}
