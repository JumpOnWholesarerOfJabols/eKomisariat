package main.java.view;

import main.java.database.Database;
import main.java.database.DatabaseOperations;
import main.java.model.Notification;
import main.java.model.NotificationType;
import main.java.model.User;
import main.java.utils.DataValidation;
import org.apache.commons.codec.digest.DigestUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.Arrays;

public class RegisterPage extends AbstractPage {
    protected final DatabaseOperations<User> usersDatabase;

    protected JTextField nameField;
    protected JTextField surnameField;
    protected JTextField emailField;
    protected JPasswordField passwordField;
    protected JButton registerButton;
    protected JLabel passwordInfoLabel;

    public RegisterPage(DatabaseOperations<User> usersDatabase) {
        super();
        this.usersDatabase = usersDatabase;
    }

    public JPanel generatePage(CardLayout cardLayout, JPanel mainPanel) {
        rootPanel.setLayout(new GridBagLayout());
        JPanel mainRegisterPanel = createMainRegisterPanel(cardLayout, mainPanel);
        rootPanel.add(mainRegisterPanel);
        return rootPanel;
    }

    private JPanel createMainRegisterPanel(CardLayout cardLayout, JPanel mainPanel) {
        JPanel mainRegisterPanel = new JPanel(new GridBagLayout());
        mainRegisterPanel.setPreferredSize(new Dimension(800, 500));
        mainRegisterPanel.setBackground(new Color(81, 145, 203));

        addTitleLabel(mainRegisterPanel);
        addInputFields(mainRegisterPanel);
        addPasswordInfoLabel(mainRegisterPanel);
        addRegisterButton(cardLayout, mainPanel, mainRegisterPanel);
        addLoginLabel(cardLayout, mainPanel, mainRegisterPanel);

        return mainRegisterPanel;
    }

    private void addTitleLabel(JPanel panel) {
        JLabel titleField = new JLabel("Rejestracja", SwingConstants.CENTER);
        titleField.setFont(new Font("Arial", Font.BOLD, 28));
        titleField.setForeground(Color.WHITE);
        GridBagConstraints gbc = createGridBagConstraints(0, 0, 2, GridBagConstraints.HORIZONTAL);
        panel.add(titleField, gbc);
    }

    private void addInputFields(JPanel panel) {
        nameField = createTextField("Imię");
        surnameField = createTextField("Nazwisko");
        emailField = createTextField("Email");
        passwordField = createPasswordField("Hasło");

        GridBagConstraints gbc = createGridBagConstraints(0, 1, 2, GridBagConstraints.HORIZONTAL);
        panel.add(nameField, gbc);
        gbc.gridy = 2;
        panel.add(surnameField, gbc);
        gbc.gridy = 3;
        panel.add(emailField, gbc);
        gbc.gridy = 4;
        panel.add(passwordField, gbc);
    }

    private JTextField createTextField(String title) {
        JTextField textField = new JTextField();
        textField.setBorder(BorderFactory.createTitledBorder(title));
        return textField;
    }

    private JPasswordField createPasswordField(String title) {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBorder(BorderFactory.createTitledBorder(title));
        return passwordField;
    }

    private void addPasswordInfoLabel(JPanel panel) {
        passwordInfoLabel = new JLabel("Hasło musi zawierać minimum 6 znaków, jedną wielką literę i jedną cyfrę");
        GridBagConstraints gbc = createGridBagConstraints(0, 5, 2, GridBagConstraints.HORIZONTAL);
        panel.add(passwordInfoLabel, gbc);
    }

    private void addRegisterButton(CardLayout cardLayout, JPanel mainPanel, JPanel panel) {
        registerButton = new JButton("Zarejestruj się");
        registerButton.setPreferredSize(new Dimension(120, 35));
        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleRegisterButtonClick(cardLayout, mainPanel);
            }
        });

        GridBagConstraints gbc = createGridBagConstraints(1, 6, 1, GridBagConstraints.EAST);
        panel.add(registerButton, gbc);
    }

    private void addLoginLabel(CardLayout cardLayout, JPanel mainPanel, JPanel panel) {
        JLabel loginLabel = new JLabel("<html><u>Masz konto? Zaloguj się</u></html>");
        loginLabel.setForeground(Color.WHITE);
        loginLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "loginPage");
            }
        });

        GridBagConstraints gbc = createGridBagConstraints(0, 7, 2, GridBagConstraints.HORIZONTAL);
        panel.add(loginLabel, gbc);
    }

    private GridBagConstraints createGridBagConstraints(int x, int y, int gridWidth, int fill) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.weightx = 1.0;
        gbc.gridwidth = gridWidth;
        gbc.fill = fill;
        gbc.insets = new Insets(10, 20, 10, 20);
        return gbc;
    }

    private void handleRegisterButtonClick(CardLayout cardLayout, JPanel mainPanel) {
        if (isDataValid()) {
            String password = Arrays.toString(passwordField.getPassword());
            String hashedPassword = DigestUtils.sha256Hex(password);
            User user = new User(nameField.getText(), surnameField.getText(), emailField.getText(), hashedPassword);
            usersDatabase.addItemToDatabase(user);
            cardLayout.show(mainPanel, "loginPage");
            resetFields();
            JOptionPane.showMessageDialog(null, "Zarejestrowano pomyślnie");
            Database.getInstance().getNotificationDatabase().addItemToDatabase(new Notification(0, NotificationType.USER_CREATED, Database.getInstance().getUsersDatabase().getItemID(user), LocalDateTime.now()));
        } else {
            JOptionPane.showMessageDialog(null, "Wypełnij poprawnie wszystkie pola!");
        }
    }

    private void resetFields() {
        nameField.setText("");
        surnameField.setText("");
        passwordField.setText("");
        emailField.setText("");
    }

    private boolean isDataValid() {
        return DataValidation.isNameValid(nameField.getText())
                && DataValidation.isNameValid(surnameField.getText())
                && DataValidation.isEmailValid(emailField.getText())
                && DataValidation.isPasswordValid(new String(passwordField.getPassword()));
    }

}
