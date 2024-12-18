package main.java.view;

import main.java.database.Database;
import main.java.utils.NotificationsFilterMethods;
import main.java.utils.ReportsFilterMethods;

import javax.swing.*;
import java.awt.*;

public class HomePage extends AbstractPage{
    protected static final Dimension BTN_DIMENSION = new Dimension(200, 200);

    public JPanel generatePage(CardLayout cardLayout, JPanel mainPanel) {
        validateUser(mainPanel);

        initializeRootPanel();

        addWelcomeLabel();

        JPanel spacerPanel = createSpacerPanel();
        JPanel controlsPanel = createControlsPanel(cardLayout, mainPanel);

        spacerPanel.add(controlsPanel);
        rootPanel.add(spacerPanel);

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
        rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
        rootPanel.setBackground(new Color(50, 150, 200));
    }

    private void addWelcomeLabel() {
        String welcome = "Witaj ".concat(Database.getInstance().getCurrentUser().getName());
        JLabel welcomeLabel = new JLabel(welcome);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rootPanel.add(welcomeLabel);
    }

    private JPanel createSpacerPanel() {
        return new JPanel(new GridBagLayout());
    }

    private JPanel createControlsPanel(CardLayout cardLayout, JPanel mainPanel) {
        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new FlowLayout());
        controlsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        addControlButtons(controlsPanel, cardLayout, mainPanel);

        return controlsPanel;
    }

    private void addControlButtons(JPanel controlsPanel, CardLayout cardLayout, JPanel mainPanel) {
        controlsPanel.add(firstButton(cardLayout, mainPanel));
        controlsPanel.add(secondButton(cardLayout, mainPanel));
        controlsPanel.add(thirdButton(cardLayout, mainPanel));

        JButton logoutButton = createLogoutButton(cardLayout, mainPanel);
        rootPanel.add(logoutButton);
    }

    private JButton createLogoutButton(CardLayout cardLayout, JPanel mainPanel) {
        JButton logoutButton = new JButton("Wyloguj");
        logoutButton.addActionListener(e -> cardLayout.show(mainPanel, "loginPage"));
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        return logoutButton;
    }

    protected JButton firstButton(CardLayout cardLayout, JPanel mainPanel) {
        ReportPage reportPage = new ReportPage();
        mainPanel.add(reportPage.generatePage(cardLayout, mainPanel), "reportPage");

        JButton reportButton = new JButton("Nowy donos");
        reportButton.setPreferredSize(BTN_DIMENSION);
        reportButton.addActionListener(e -> cardLayout.show(mainPanel, "reportPage"));
        return reportButton;
    }

    protected JButton secondButton(CardLayout cardLayout, JPanel mainPanel) {
        ReportDisplayPage reportDisplayPage = new ReportDisplayPage(ReportsFilterMethods.filterUserId(Database.getInstance().getCurrentUserId()));
        mainPanel.add(reportDisplayPage.generatePage(cardLayout, mainPanel), "reportDisplayPage");

        JButton reportsButton = new JButton("Wyświetl listę raportów");
        reportsButton.setPreferredSize(BTN_DIMENSION);
        reportsButton.addActionListener(e -> {
            reportDisplayPage.changeDisplayedReports(reportDisplayPage.defaultFilter);
            cardLayout.show(mainPanel, "reportDisplayPage");
        });

        return reportsButton;
    }

    protected JButton thirdButton(CardLayout cardLayout, JPanel mainPanel) {
        NotificationDisplayPage notificationDisplayPage = new NotificationDisplayPage(NotificationsFilterMethods.filterByTargetUserID(Database.getInstance().getCurrentUserId()));
        mainPanel.add(notificationDisplayPage.generatePage(cardLayout, mainPanel), "notificationDisplayPage");

        JButton notificationsButton = new JButton("Wyświetl powiadomienia");
        notificationsButton.setPreferredSize(BTN_DIMENSION);
        notificationsButton.addActionListener(e -> {
            notificationDisplayPage.changeDisplayedReports(notificationDisplayPage.defaultFilter);
            cardLayout.show(mainPanel, "notificationDisplayPage");
        });

        return notificationsButton;
    }

}
