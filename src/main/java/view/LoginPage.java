package main.java.view;
import main.java.Main;
import main.java.database.DatabaseOperations;
import main.java.database.UsersDatabase;
import main.java.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class LoginPage {
    String folderPath = "src/main/resources/users/";
    DatabaseOperations<User> usersDatabase = new UsersDatabase(folderPath);

    public JPanel generateLoginPage(CardLayout cardLayout, JPanel mainPanel) {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BorderLayout());
        loginPanel.setBackground(new Color(35,78,117));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(60, 145, 60, 145));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());

        JPanel headRow = new JPanel(new FlowLayout());
        headRow.setBackground(new Color(35,78,117));
        headRow.setBorder(BorderFactory.createEmptyBorder(30,0,0,0));

        ImageIcon logo = new ImageIcon("C:\\Users\\kkosz\\Desktop\\p.png");
        JLabel logoLabel = new JLabel(logo);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0,0,0,30));
        headRow.add(logoLabel);

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(35,78,117));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        // Create header title label
        JLabel headerTitle = new JLabel("eKomisariat");
        headerTitle.setFont(new Font("Arial", Font.BOLD, 32));
        headerTitle.setForeground(Color.WHITE);
        headerTitle.setAlignmentX(Component.CENTER_ALIGNMENT); // Align center in BoxLayout

        // Create a panel for headerTitle and center it
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false); // Make panel transparent
        titlePanel.add(headerTitle);

        // Create header description label
        JLabel headerDesc = new JLabel("Nasze miejsce na Twoją sprawę");
        headerDesc.setFont(new Font("Arial", Font.PLAIN, 22));
        headerDesc.setForeground(Color.WHITE);
        headerDesc.setAlignmentX(Component.CENTER_ALIGNMENT); // Align center in BoxLayout

        // Create a panel for headerDesc and center it
        JPanel descPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        descPanel.setOpaque(false); // Make panel transparent
        descPanel.add(headerDesc);

        // Add both panels to the headerPanel
        headerPanel.add(titlePanel);
        headerPanel.add(descPanel);

        headRow.add(headerPanel);

        JPanel loginRow = new JPanel();
        loginRow.setLayout(new BorderLayout());
        loginRow.setBackground(new Color(35,78,117));
        loginRow.setBorder(BorderFactory.createEmptyBorder(75,50,75,50));

        JPanel mainLoginField = new JPanel();
        mainLoginField.setLayout(new GridBagLayout());
        mainLoginField.setBackground(new Color(81,145,203));

        // Email Field
        JTextField emailField = new JTextField();
        emailField.setBorder(BorderFactory.createTitledBorder("Email"));

        // Password Field
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

        loginPanel.add(centerPanel, BorderLayout.CENTER);

        // Add ActionListener for loginButton to switch to newPage
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Switch to the new page when the login button is clicked
                if(TryLogIn(emailField, passwordField)) {
                    HomePage homePage = new HomePage();
                    JPanel homePagePanel = homePage.generateHomePage(cardLayout, mainPanel);
                    mainPanel.add(homePagePanel, "homePage");
                    cardLayout.show(mainPanel, "homePage");
                } else {
                    JOptionPane.showMessageDialog(null, "Podano błędne dane logowania!");
                }
            }
        });

        return loginPanel;
    }

    private boolean TryLogIn(JTextField mainLoginField, JPasswordField passwordField) {
        Map<Integer, User> users = usersDatabase.importDataFromFile();
        for (User user : users.values()) {
            if(Objects.equals(user.getEmail(), mainLoginField.getText()) && Arrays.equals(user.getPassword().toCharArray(), passwordField.getPassword())) {
                Main.currentUser = user;
                System.out.println("tu" + user);
                return true;
            }
        }
        return false;
    }
}
