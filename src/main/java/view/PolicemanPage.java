package main.java.view;

import main.java.database.Database;
import main.java.utils.ReportsFilterMethods;

import javax.swing.*;
import java.awt.*;

public class PolicemanPage extends HomePage{

    public JPanel generatePage(CardLayout cardLayout, JPanel mainPanel) {
        return super.generatePage(cardLayout,mainPanel);
    }

    @Override
    protected JButton firstButton(CardLayout cardLayout, JPanel mainPanel) {
        JButton reportButton = new JButton("Statystyki");
        reportButton.setPreferredSize(BTN_DIMENSION);
        return reportButton;
    }

    @Override
    protected JButton secondButton(CardLayout cardLayout, JPanel mainPanel) {
        ReportDisplayPage reportDisplayPage = new ReportDisplayPagePoliceman(ReportsFilterMethods.filterReportAssigmentWorker(Database.getInstance().getCurrentUserId()));
        mainPanel.add(reportDisplayPage.generatePage(cardLayout, mainPanel), "reportDisplayPage");

        JButton reportsButton = new JButton("Przypisane zgłoszenia");
        reportsButton.setPreferredSize(BTN_DIMENSION);
        reportsButton.addActionListener(e -> {
            reportDisplayPage.changeDisplayedReports(reportDisplayPage.defaultFilter);
            cardLayout.show(mainPanel, "reportDisplayPage");
        });

        return reportsButton;
    }
}
