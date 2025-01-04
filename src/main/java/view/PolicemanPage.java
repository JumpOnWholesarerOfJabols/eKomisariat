package main.java.view;

import main.java.database.Database;
import main.java.utils.ReportsFilterMethods;

import javax.swing.*;
import java.awt.*;

public class PolicemanPage extends HomePage{

    private final PieChartDialog pieChartDialog = new PieChartDialog();

    @Override
    protected JButton createButton1(CardLayout cardLayout, JPanel mainPanel) {
        ReportPage reportPage = new ReportPage();
        mainPanel.add(reportPage.generatePage(cardLayout, mainPanel), "reportPage");

        JButton button = createHoverButton("Statystyki", "Moje statystyki", new Color(40, 50, 90), cardLayout, mainPanel, "reportPage");
        button.setBackground(new Color(30, 40, 90));
        button.setForeground(Color.WHITE);
        button.addActionListener(_ -> {
            pieChartDialog.updateDatasets(ReportsFilterMethods.filterReportAssigmentWorker(Database.getInstance().getCurrentUserId()));
            pieChartDialog.setVisible(true);
        });
        return button;
    }

    @Override
    protected JButton createButton2(CardLayout cardLayout, JPanel mainPanel) {
        ReportDisplayPage reportDisplayPage = new ReportDisplayPagePoliceman(ReportsFilterMethods.filterReportAssigmentWorker(Database.getInstance().getCurrentUserId()));
        mainPanel.add(reportDisplayPage.generatePage(cardLayout, mainPanel), "reportDisplayPage");

        JButton button = createHoverButton("Moje sprawy", "Przypisane sprawy", new Color(40, 50, 90), cardLayout, mainPanel, "reportPage");
        button.setBackground(new Color(30, 40, 90));
        button.setForeground(Color.WHITE);
        button.addActionListener(e -> {
            reportDisplayPage.changeDisplayedReports(reportDisplayPage.defaultFilter);
            cardLayout.show(mainPanel, "reportDisplayPage");
        });
        return button;
    }
}
