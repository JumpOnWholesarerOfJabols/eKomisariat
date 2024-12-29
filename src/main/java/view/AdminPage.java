package main.java.view;

import main.java.database.Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdminPage {
    private final String adminReportPageName = "adminReportsPage";
    private final String adminUsersPageName = "adminUsersPage";
    private final String adminNotificationPageName = "adminNotificationsPage";
    private final String adminAddPolicemanPageName = "adminAddPolicemanPage";

    public JPanel generatePage(CardLayout cardLayout, JPanel mainPanel) {
        JPanel adminPanel = new JPanel();
        adminPanel.setLayout(new GridBagLayout());
        adminPanel.setBackground(new Color(35, 78, 117));
        adminPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setOpaque(false);

        JLabel headerLabel = new JLabel("Panel Admina", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 38));
        headerLabel.setForeground(Color.WHITE);

        JButton logoutButton = new JButton("Wyloguj");
        logoutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cardLayout.show(mainPanel, "loginPage");
            }
        });

        GridBagConstraints gbcHeader = new GridBagConstraints();
        gbcHeader.gridx = 0;
        gbcHeader.gridy = 0;
        gbcHeader.weightx = 1.0;
        headerPanel.add(headerLabel, gbcHeader);
        gbcHeader.anchor = GridBagConstraints.EAST;
        headerPanel.add(logoutButton, gbcHeader);

        adminPanel.add(headerPanel, gbc);

        JPanel adminMainPanel = new JPanel();
        adminMainPanel.setLayout(new GridBagLayout());
        adminMainPanel.setOpaque(false);

        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        JLabel showReports = createNavigationLabel("Pokaż raporty", cardLayout, mainPanel, adminReportPageName);
        JLabel showUsers = createNavigationLabel("Pokaż użytkowników", cardLayout, mainPanel, adminUsersPageName);
        JLabel showNotifications = createNavigationLabel("Pokaż powiadomienia", cardLayout, mainPanel, adminNotificationPageName);
        JLabel addPoliceman = createNavigationLabel("Dodaj policjanta", cardLayout, mainPanel, adminAddPolicemanPageName);

        GridBagConstraints innerGbc = new GridBagConstraints();
        innerGbc.gridx = 0;
        innerGbc.gridy = 0;
        innerGbc.insets = new Insets(10, 0, 10, 0);
        innerGbc.fill = GridBagConstraints.HORIZONTAL;
        innerGbc.weightx = 1;

        adminMainPanel.add(showReports, innerGbc);
        innerGbc.gridy++;
        adminMainPanel.add(showUsers, innerGbc);
        innerGbc.gridy++;
        adminMainPanel.add(showNotifications, innerGbc);
        innerGbc.gridy++;
        adminMainPanel.add(addPoliceman, innerGbc);

        adminPanel.add(adminMainPanel, gbc);

        return adminPanel;
    }

    private JLabel createNavigationLabel(String text, CardLayout cardLayout, JPanel mainPanel, String targetPageName) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(Color.WHITE);
        label.setOpaque(true);
        label.setBackground(new Color(50, 90, 140));
        label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                label.setBackground(new Color(70, 110, 160));
                label.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                label.setBackground(new Color(50, 90, 140));
                label.setForeground(Color.WHITE);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                switch (targetPageName) {
                    case adminReportPageName -> {
                        ReportDisplayPageAdmin reportDisplayPageAdmin = new ReportDisplayPageAdmin(null);
                        JPanel adminPanel = reportDisplayPageAdmin.generatePage(cardLayout, mainPanel);
                        mainPanel.add(adminPanel, targetPageName);
                        cardLayout.show(mainPanel, targetPageName);
                    }
                    case adminUsersPageName -> {
                        UserDisplayPageAdmin userDisplayPageAdmin = new UserDisplayPageAdmin(null);
                        JPanel userPanel = userDisplayPageAdmin.generatePage(cardLayout, mainPanel);
                        mainPanel.add(userPanel, targetPageName);
                        cardLayout.show(mainPanel, targetPageName);
                    }
                    case adminAddPolicemanPageName -> {
                        AddPolicemanPage addPolicemanPage = new AddPolicemanPage(Database.getInstance().getUsersDatabase());
                        JPanel addPolicemanPanel = addPolicemanPage.generatePage(cardLayout, mainPanel);
                        mainPanel.add(addPolicemanPanel, targetPageName);
                        cardLayout.show(mainPanel, targetPageName);
                    }
                    case adminNotificationPageName -> {
                        NotificationDisplayPageAdmin notificationDisplayPage = new NotificationDisplayPageAdmin(null);
                        JPanel notificationPanel = notificationDisplayPage.generatePage(cardLayout, mainPanel);
                        mainPanel.add(notificationPanel, targetPageName);
                        cardLayout.show(mainPanel, targetPageName);
                    }
                }
            }
        });

        return label;
    }
}
