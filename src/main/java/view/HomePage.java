package main.java.view;

import main.java.Main;
import main.java.model.Policeman;
import main.java.utils.ReportsFilterMethods;

import javax.swing.*;
import java.awt.*;

public class HomePage extends AbstractPage{
    public JPanel generatePage(CardLayout cardLayout, JPanel mainPanel) {
        // This should never happen under normal circumstances
        if(Main.currentUser == null){
            JOptionPane.showMessageDialog(mainPanel, "WAKE UP WAKE UP WAKE UP WAKE UP WAKE UP WAKE UP WAKE UP", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        System.out.println("Logged in as user: " + Main.usersDatabase.getUserId(Main.currentUser));
        rootPanel.setLayout(new FlowLayout());
        rootPanel.setBackground(new Color(50, 150, 200));

        String welcome = "Witaj ".concat(Main.currentUser.getName());

        JLabel welcomeLabel = new JLabel(welcome);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.WHITE);
        rootPanel.add(welcomeLabel);

        JButton logoutButton = new JButton("Wyloguj");
        logoutButton.addActionListener(e -> cardLayout.show(mainPanel, "loginPage"));
        rootPanel.add(logoutButton);

        ReportPage reportPage = new ReportPage();
        mainPanel.add("reportPage", reportPage.generatePage(cardLayout, mainPanel));

        JButton reportButton = new JButton("Nowy donos");
        reportButton.addActionListener(e -> cardLayout.show(mainPanel, "reportPage"));
        rootPanel.add(reportButton);

        generateReportsButton(cardLayout, mainPanel);
        return rootPanel;
    }

    private void generateReportsButton(CardLayout cardLayout, JPanel mainPanel){
        ReportDisplayPage reportDisplayPage = new ReportDisplayPage(ReportsFilterMethods.filterReportAssigmentWorker(Main.usersDatabase.getUserId(Main.currentUser)));

        //ReportDisplayPage reportDisplayPage = new ReportDisplayPage(null);
        mainPanel.add(reportDisplayPage.generatePage(cardLayout, mainPanel), "reportDisplayPage");

        JButton reportsButton = new JButton("Wyświetl listę raportów");
        reportsButton.addActionListener(e -> cardLayout.show(mainPanel, "reportDisplayPage"));

        rootPanel.add(reportsButton);
    }
}
