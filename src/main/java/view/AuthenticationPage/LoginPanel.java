package main.java.view.AuthenticationPage;

import main.java.database.Database;
import main.java.logger.LogEventType;
import main.java.logger.Logger;
import main.java.model.User;
import main.java.utils.UsersFilterMethods;
import main.java.view.AdminPage;
import main.java.view.HomePage;
import main.java.view.PolicemanPage;
import org.apache.commons.codec.digest.DigestUtils;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;

public class LoginPanel extends AuthPanel {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel titleLabel;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public LoginPanel(CardLayout cardLayout, JPanel mainPanel) {
        super();
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;

        createLoginPanel();
    }

    private void createLoginPanel() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        titleLabel = new JLabel("Logowanie");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(titleLabel, gbc);

        gbc.gridy = 1;
        emailField = createStyledTextField("Email");
        emailField.setPreferredSize(new Dimension(350, 50));
        this.add(emailField, gbc);

        gbc.gridy = 2;
        passwordField = createStyledPasswordField("Password");
        passwordField.setPreferredSize(new Dimension(350, 50));
        this.add(passwordField, gbc);

        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        loginButton = createModernButton("Zaloguj się");
        loginButton.setPreferredSize(new Dimension(200, 45));

        loginButton.addActionListener(e -> this.handleLogin(emailField, passwordField));
        this.add(loginButton, gbc);
    }

    private void handleLogin(JTextField emailField, JPasswordField passwordField) {
        String passwd = new String(passwordField.getPassword());
        if (emailField.getText().equals("admin") && passwd.equals("admin")) {
            Logger.getInstance().log(LogEventType.ADMIN_LOGIN, formatter.format(LocalDateTime.now()));
            AdminPage adminPage = new AdminPage();
            JPanel adminPagePanel = adminPage.generatePage(cardLayout, mainPanel);
            mainPanel.add(adminPagePanel, "adminPage");
            cardLayout.show(mainPanel, "adminPage");
        } else if (tryLogIn(emailField, passwordField)) {
            HomePage homePage = emailField.getText().toLowerCase().contains("@ekomisariat.pl") ? new PolicemanPage() : new HomePage();
            JPanel homePagePanel = homePage.generatePage(cardLayout, mainPanel);
            mainPanel.add(homePagePanel, "homePage");
            cardLayout.show(mainPanel, "homePage");
        } else {
            JOptionPane.showMessageDialog(null, "Podano błędne dane logowania!");
        }
    }

    private boolean tryLogIn(JTextField mainLoginField, JPasswordField passwordField) {
        Optional<User> userOptional = Database.getInstance().getUsersDatabase()
                .getFiltered(UsersFilterMethods.filterLoginField(mainLoginField.getText()))
                .values()
                .stream()
                .findFirst();

        String password = Arrays.toString(passwordField.getPassword());
        String hashedPassword = DigestUtils.sha256Hex(password);

        if (userOptional.isPresent() && userOptional.get().getPassword().equals(hashedPassword)) {
            Database.getInstance().setCurrentUser(userOptional.get());
            return true;
        }

        return false;
    }
}
