package main.java.view;

import main.java.database.Database;
import main.java.utils.NotificationsFilterMethods;
import main.java.utils.ReportsFilterMethods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HomePage extends AbstractPage {
    protected static final Dimension BTN_DIMENSION = new Dimension(200, 50);
    private JLabel centerLabel;
    private JPanel centerPanel;

    public JPanel generatePage(CardLayout cardLayout, JPanel mainPanel) {
        validateUser(mainPanel);
        initializeRootPanel();

        addCenterPanel();
        addButtonPanel(cardLayout, mainPanel);

        return rootPanel;
    }

    private void validateUser(JPanel mainPanel) {
        if (Database.getInstance().getCurrentUser() == null) {
            JOptionPane.showMessageDialog(mainPanel, "WAKE UP WAKE UP WAKE UP WAKE UP WAKE UP WAKE UP WAKE UP", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        System.out.println("Logged in as user: " + Database.getInstance().getCurrentUserId());
    }

    private void initializeRootPanel() {
        rootPanel.setLayout(new BorderLayout());
        rootPanel.setBackground(new Color(23, 29, 50));
    }

    private void addCenterPanel() {
        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(40, 50, 80));

        centerLabel = new JLabel("", SwingConstants.CENTER);
        centerLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        centerLabel.setForeground(Color.WHITE);
        centerPanel.add(centerLabel, BorderLayout.CENTER);
        centerLabel.setText("Witaj " + Database.getInstance().getCurrentUser().getName());

        centerPanel.setBorder(BorderFactory.createLineBorder(new Color(19, 29, 50), 10));

        rootPanel.add(centerPanel, BorderLayout.CENTER);
    }

    private void addButtonPanel(CardLayout cardLayout, JPanel mainPanel) {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        buttonPanel.setBackground(new Color(24, 35, 53));

        buttonPanel.add(createButton1(cardLayout, mainPanel));
        buttonPanel.add(createButton2(cardLayout, mainPanel));
        buttonPanel.add(createButton3(cardLayout, mainPanel));
        buttonPanel.add(createButton4(cardLayout, mainPanel));

        rootPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    protected JButton createButton1(CardLayout cardLayout, JPanel mainPanel) {
        ReportPage reportPage = new ReportPage();
        mainPanel.add(reportPage.generatePage(cardLayout, mainPanel), "reportPage");

        JButton button = createHoverButton("Nowy donos", "Donieś", new Color(40, 50, 90), cardLayout, mainPanel, "reportPage");
        button.setBackground(new Color(30, 40, 90));
        button.setForeground(Color.WHITE);
        button.addActionListener(e -> cardLayout.show(mainPanel, "reportPage"));
        return button;
    }

    protected JButton createButton2(CardLayout cardLayout, JPanel mainPanel) {
        ReportDisplayPage reportDisplayPage = new ReportDisplayPage(ReportsFilterMethods.filterUserId(Database.getInstance().getCurrentUserId()));
        mainPanel.add(reportDisplayPage.generatePage(cardLayout, mainPanel), "reportDisplayPage");

        JButton button = createHoverButton("Lista raportów", "Moje donosy", new Color(40, 50, 100), cardLayout, mainPanel, "reportDisplayPage");
        button.setBackground(new Color(30, 40, 90));
        button.setForeground(Color.WHITE);

        button.addActionListener(e -> {
            reportDisplayPage.changeDisplayedReports(reportDisplayPage.defaultFilter);
            cardLayout.show(mainPanel, "reportDisplayPage");
        });
        return button;
    }

    protected JButton createButton3(CardLayout cardLayout, JPanel mainPanel) {
        NotificationDisplayPage notificationDisplayPage = new NotificationDisplayPage(NotificationsFilterMethods.filterByTargetUserID(Database.getInstance().getCurrentUserId()));
        mainPanel.add(notificationDisplayPage.generatePage(cardLayout, mainPanel), "notificationDisplayPage");
        JButton button = createHoverButton("Powiadomienia", "Moje powiadomienia", new Color(40, 50, 90), cardLayout, mainPanel, "notificationDisplayPage");
        button.setBackground(new Color(30, 40, 90));
        button.setForeground(Color.WHITE);

        button.addActionListener(e -> {
            notificationDisplayPage.changeDisplayedReports(notificationDisplayPage.defaultFilter);
            cardLayout.show(mainPanel, "notificationDisplayPage");
        });
        return button;
    }

    protected JButton createButton4(CardLayout cardLayout, JPanel mainPanel) {
        JButton button = createHoverButton("Wyloguj", "Wyloguj mnie", new Color(40, 50, 100), cardLayout, mainPanel, "loginPage");
        button.setBackground(new Color(30, 40, 90));
        button.setForeground(Color.WHITE);

        button.addActionListener(e -> cardLayout.show(mainPanel, "loginPage"));
        return button;
    }


    protected JButton createHoverButton(String text, String hoverText, Color hoverColor, CardLayout cardLayout, JPanel mainPanel, String page) {
        JButton button = new JButton(text);
        button.setPreferredSize(BTN_DIMENSION);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setBackground(new Color(50, 60, 110));


        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                centerLabel.setText(hoverText);
                centerPanel.setBackground(hoverColor);

                button.setBackground(hoverColor);
                button.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                centerLabel.setText("Witaj " + Database.getInstance().getCurrentUser().getName());
                centerPanel.setBackground(new Color(40, 50, 80));

                button.setBackground(new Color(30, 40, 90));
                button.setForeground(Color.WHITE);
            }
        });

        return button;
    }
}
