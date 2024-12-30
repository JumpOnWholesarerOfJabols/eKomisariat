package main.java.view;

import main.java.database.Database;
import main.java.database.DatabaseOperations;
import main.java.logger.LogEventType;
import main.java.logger.Logger;
import main.java.model.User;
import main.java.utils.UsersFilterMethods;
import org.apache.commons.codec.digest.DigestUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;

public class LoginPage extends AbstractPage {
    private final DatabaseOperations<User> usersDatabase;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public LoginPage(DatabaseOperations<User> usersDatabase) {
        super();
        this.usersDatabase = usersDatabase;
    }

    public JPanel generatePage(CardLayout cardLayout, JPanel mainPanel) {
        rootPanel.setLayout(new BorderLayout());

        JPanel centerPanel = createCenterPanel(cardLayout, mainPanel);
        rootPanel.add(centerPanel, BorderLayout.CENTER);

        return rootPanel;
    }

    private JPanel createCenterPanel(CardLayout cardLayout, JPanel mainPanel) {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        centerPanel.add(createLoginPanel(cardLayout, mainPanel), BorderLayout.CENTER);
        return centerPanel;
    }

    private JPanel createHeaderPanel() {
        JPanel headRow = new JPanel(new FlowLayout());
        headRow.setBackground(BG_COLOR);
        headRow.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        JLabel logoLabel = createLogoLabel();
        headRow.add(logoLabel);
        headRow.add(createTitlePanel());

        return headRow;
    }

    private JLabel createLogoLabel() {
        ImageIcon logo = new ImageIcon("src/main/resources/p.png");
        JLabel logoLabel = new JLabel(logo);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 30));
        return logoLabel;
    }

    private JPanel createTitlePanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(BG_COLOR);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JLabel headerTitle = new JLabel("eKomisariat™");
        headerTitle.setFont(new Font("Arial", Font.BOLD, 32));
        headerTitle.setForeground(Color.WHITE);
        headerTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel headerDesc = new JLabel("Nasze miejsce na Twoją sprawę");
        headerDesc.setFont(new Font("Arial", Font.PLAIN, 22));
        headerDesc.setForeground(Color.WHITE);
        headerDesc.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(createPanelWithComponent(headerTitle));
        headerPanel.add(createPanelWithComponent(headerDesc));

        return headerPanel;
    }

    private JPanel createPanelWithComponent(JComponent component) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setOpaque(false);
        panel.add(component);
        return panel;
    }

    private JPanel createLoginPanel(CardLayout cardLayout, JPanel mainPanel) {
        JPanel loginRow = new JPanel(new BorderLayout());
        loginRow.setBackground(BG_COLOR);
        loginRow.setBorder(BorderFactory.createEmptyBorder(75, 50, 75, 50));

        JPanel mainLoginField = new JPanel(new GridBagLayout());
        mainLoginField.setBackground(new Color(81, 145, 203));

        JTextField emailField = createTextField("Email");
        JPasswordField passwordField = createPasswordField("Hasło");
        JButton loginButton = createLoginButton(cardLayout, mainPanel, emailField, passwordField);
        JLabel registerLabel = createRegisterLabel(cardLayout, mainPanel);

        addComponentsToLoginField(mainLoginField, emailField, passwordField, loginButton, registerLabel);

        loginRow.add(mainLoginField);
        return loginRow;
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

    private JButton createLoginButton(CardLayout cardLayout, JPanel mainPanel, JTextField emailField, JPasswordField passwordField) {
        JButton loginButton = new JButton("Zaloguj się");
        loginButton.setPreferredSize(new Dimension(100, 35));
        loginButton.addActionListener(e -> handleLogin(cardLayout, mainPanel, emailField, passwordField));
        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin(cardLayout, mainPanel, emailField, passwordField);
            }
        });
        return loginButton;
    }

    private JLabel createRegisterLabel(CardLayout cardLayout, JPanel mainPanel) {
        JLabel registerLabel = new JLabel("<html><u>Nie masz konta? Zarejestruj się!</u></html>");
        registerLabel.setForeground(Color.WHITE);
        registerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "registerPage");
            }
        });
        return registerLabel;
    }

    private void addComponentsToLoginField(JPanel panel, JTextField emailField, JPasswordField passwordField, JButton loginButton, JLabel registerLabel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 25, 15, 25);
        panel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(15, 25, 35, 25);
        panel.add(loginButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets = new Insets(15, 25, 15, 25);
        panel.add(registerLabel, gbc);
    }

    private void handleLogin(CardLayout cardLayout, JPanel mainPanel, JTextField emailField, JPasswordField passwordField) {
        String passwd = new String(passwordField.getPassword());
        if (emailField.getText().equals("admin") && passwd.equals("admin")) {
            Logger.getInstance().log(LogEventType.ADMIN_LOGIN, formatter.format(LocalDateTime.now()));
            AdminPage adminPage = new AdminPage();
            JPanel adminPagePanel = adminPage.generatePage(cardLayout, mainPanel);
            mainPanel.add(adminPagePanel, "adminPage");
            cardLayout.show(mainPanel, "adminPage");
        } else if (tryLogIn(emailField, passwordField)) {
            HomePage homePage = emailField.getText().contains("@eKomisariat.pl") ? new PolicemanPage() : new HomePage();
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
