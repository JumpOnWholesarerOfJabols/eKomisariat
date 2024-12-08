package main.java.view;
import main.java.database.Database;
import main.java.database.DatabaseOperations;
import main.java.logger.LogEventType;
import main.java.logger.Logger;
import main.java.model.User;
import main.java.utils.UsersFilterMethods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());

        JPanel headRow = new JPanel(new FlowLayout());
        headRow.setBackground(BG_COLOR);
        headRow.setBorder(BorderFactory.createEmptyBorder(30,0,0,0));

        ImageIcon logo = new ImageIcon("src/main/resources/p.png");
        JLabel logoLabel = new JLabel(logo);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0,0,0,30));
        headRow.add(logoLabel);

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(BG_COLOR);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JLabel headerTitle = new JLabel("eKomisariat™");
        headerTitle.setFont(new Font("Arial", Font.BOLD, 32));
        headerTitle.setForeground(Color.WHITE);
        headerTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);
        titlePanel.add(headerTitle);

        JLabel headerDesc = new JLabel("Nasze miejsce na Twoją sprawę");
        headerDesc.setFont(new Font("Arial", Font.PLAIN, 22));
        headerDesc.setForeground(Color.WHITE);
        headerDesc.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel descPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        descPanel.setOpaque(false);
        descPanel.add(headerDesc);

        headerPanel.add(titlePanel);
        headerPanel.add(descPanel);

        headRow.add(headerPanel);

        JPanel loginRow = new JPanel();
        loginRow.setLayout(new BorderLayout());
        loginRow.setBackground(BG_COLOR);
        loginRow.setBorder(BorderFactory.createEmptyBorder(75,50,75,50));

        JPanel mainLoginField = new JPanel();
        mainLoginField.setLayout(new GridBagLayout());
        mainLoginField.setBackground(new Color(81,145,203));

        JTextField emailField = new JTextField();
        emailField.setBorder(BorderFactory.createTitledBorder("Email"));

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBorder(BorderFactory.createTitledBorder("Hasło")); // Add title to border

        JButton loginButton = new JButton("Zaloguj się");
        loginButton.setPreferredSize(new Dimension(100, 35));

        JLabel registerLabel = new JLabel("<html><u>Nie masz konta? Zarejestruj się!</u></html>");
        registerLabel.setForeground(Color.WHITE);
        registerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "registerPage");
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 25, 15, 25);
        mainLoginField.add(emailField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainLoginField.add(passwordField, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(15, 25, 35, 25);
        mainLoginField.add(loginButton, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets = new Insets(15, 25, 15, 25);
        mainLoginField.add(registerLabel, gbc);
        loginRow.add(mainLoginField);

        centerPanel.add(headRow, BorderLayout.NORTH);
        centerPanel.add(loginRow, BorderLayout.CENTER);

        rootPanel.add(centerPanel, BorderLayout.CENTER);

        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    loginButton.doClick();
                }
            }
        });

        loginButton.addActionListener(e -> {
            String passwd = new String(passwordField.getPassword());
            if(emailField.getText().equals("admin") && passwd.equals("admin")) {
                Logger.getInstance().log(LogEventType.ADMIN_LOGIN, formatter.format(LocalDateTime.now()));
                AdminPage adminPage = new AdminPage();
                JPanel adminPagePanel = adminPage.generatePage(cardLayout, mainPanel);
                mainPanel.add(adminPagePanel, "adminPage");
                cardLayout.show(mainPanel, "adminPage");
            } else if(tryLogIn(emailField, passwordField)) {
                HomePage homePage;
                if (emailField.getText().contains("@eKomisariat.pl"))
                    homePage = new PolicemanPage();
                else
                    homePage = new HomePage();

                JPanel homePagePanel = homePage.generatePage(cardLayout, mainPanel);
                mainPanel.add(homePagePanel, "homePage");
                cardLayout.show(mainPanel, "homePage");
            } else {
                JOptionPane.showMessageDialog(null, "Podano błędne dane logowania!");
            }
        });

        return rootPanel;
    }

    private boolean tryLogIn(JTextField mainLoginField, JPasswordField passwordField) {
        Optional<User> userOptional = Database.getInstance().getUsersDatabase()
                .getFiltered(UsersFilterMethods.filterLoginField(mainLoginField.getText()))
                .values()
                .stream()
                .findFirst();

        if(userOptional.isPresent() && userOptional.get().getPassword().equals(String.valueOf(passwordField.getPassword()))) {
            Database.getInstance().setCurrentUser(userOptional.get());
            System.out.println("tu" + userOptional.get());
            return true;
        }

        return false;
    }

}