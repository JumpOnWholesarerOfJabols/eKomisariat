package main.java.view;

import main.java.database.Database;

import javax.swing.*;
import java.awt.*;

public class PolicemanPage extends AbstractPage{

    public JPanel generatePage(CardLayout cardLayout, JPanel mainPanel) {

        System.out.println("Logged in as user: " + Database.getInstance().getCurrentUserId());
        rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
        rootPanel.setBackground(new Color(50, 150, 200));

        String welcome = "Witaj ".concat(Database.getInstance().getCurrentUser().getName());

        JLabel welcomeLabel = new JLabel(welcome);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rootPanel.add(welcomeLabel);

        JButton logoutButton = new JButton("Wyloguj");
        logoutButton.addActionListener(e -> cardLayout.show(mainPanel, "loginPage"));
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        rootPanel.add(logoutButton);


        return rootPanel;
    }
}
