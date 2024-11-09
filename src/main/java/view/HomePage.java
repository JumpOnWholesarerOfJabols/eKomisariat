package main.java.view;

import main.java.Main;
import main.java.utils.ReportsFilterMethods;

import javax.swing.*;
import java.awt.*;

public class HomePage {

    public JPanel generateHomePage(CardLayout cardLayout, JPanel mainPanel) {
        JPanel newPagePanel = new JPanel();
        newPagePanel.setLayout(new FlowLayout());
        newPagePanel.setBackground(new Color(50, 150, 200));

//        String welcome = "Witaj ";
//        if(Main.currentUser != null) {
//            welcome += Main.currentUser.getName();
//        }
//        JLabel welcomeLabel = new JLabel(welcome);
//        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
//        welcomeLabel.setForeground(Color.WHITE);
//        newPagePanel.add(welcomeLabel);
//
//        JButton logoutButton = new JButton("Wyloguj");
//        logoutButton.addActionListener(e -> {
//            cardLayout.show(mainPanel, "loginPage");
//        });

        ReportDisplayPage reportDisplayPage =
                new ReportDisplayPage(ReportsFilterMethods.filterUserId(Main.usersDatabase.getUserId(Main.currentUser)));
        JPanel reportDisplayPanel = reportDisplayPage.generateReportPage(cardLayout, mainPanel);

//        mainPanel.add(reportDisplayPanel, "reportPanel");

        newPagePanel.add(reportDisplayPanel);
        //newPagePanel.add(logoutButton);

        return newPagePanel;
    }
}
