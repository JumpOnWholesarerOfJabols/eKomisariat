package main.java.view;

import main.java.database.Database;
import main.java.model.Report;
import main.java.utils.NotificationsFilterMethods;
import main.java.utils.ReportsFilterMethods;

import javax.swing.*;
import java.awt.*;

public class PolicemanPage extends AbstractPage{
    private static final Dimension BTN_DIMENSION = new Dimension(200, 200);

    public JPanel generatePage(CardLayout cardLayout, JPanel mainPanel) {
        if(Database.getInstance().getCurrentUser() == null){
            JOptionPane.showMessageDialog(mainPanel, "WAKE UP WAKE UP WAKE UP WAKE UP WAKE UP WAKE UP WAKE UP", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        System.out.println("Logged in as user: " + Database.getInstance().getCurrentUserId());
        rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
        rootPanel.setBackground(new Color(50, 150, 200));

        String welcome = "Witaj ".concat(Database.getInstance().getCurrentUser().getName());

        JLabel welcomeLabel = new JLabel(welcome);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rootPanel.add(welcomeLabel);

        JPanel spacerPanel = new JPanel(new GridBagLayout());

        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new FlowLayout());
        controlsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton logoutButton = new JButton("Wyloguj");
        logoutButton.addActionListener(e -> cardLayout.show(mainPanel, "loginPage"));
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        rootPanel.add(logoutButton);

        ReportPage reportPage = new ReportPage();
        mainPanel.add(reportPage.generatePage(cardLayout, mainPanel), "reportPage");

        JButton reportButton = new JButton("Nowy donos");
        reportButton.setPreferredSize(BTN_DIMENSION);
        reportButton.addActionListener(e -> cardLayout.show(mainPanel, "reportPage"));
        controlsPanel.add(reportButton);

        controlsPanel.add(generateReportsButton(cardLayout, mainPanel));
        controlsPanel.add(generateNotificationsButton(cardLayout, mainPanel));

        spacerPanel.add(controlsPanel);
        rootPanel.add(spacerPanel);
        return rootPanel;
    }

    private JButton generateReportsButton(CardLayout cardLayout, JPanel mainPanel){
        ReportDisplayPagePoliceman reportDisplayPage = new ReportDisplayPagePoliceman(
                (ReportsFilterMethods.combinedFilter(
                        ReportsFilterMethods.filterReportAssigmentWorker(Database.getInstance().getCurrentUserId()))));

        mainPanel.add(reportDisplayPage.generatePage(cardLayout, mainPanel), "reportDisplayPage");

        JButton reportsButton = new JButton("Moje sprawy");
        reportsButton.setPreferredSize(BTN_DIMENSION);
        reportsButton.addActionListener(e -> {
            reportDisplayPage.changeDisplayedReports(reportDisplayPage.defaultFilter);
            cardLayout.show(mainPanel, "reportDisplayPage");
        });

        return reportsButton;
    }

    private JButton generateNotificationsButton(CardLayout cardLayout, JPanel mainPanel){
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
