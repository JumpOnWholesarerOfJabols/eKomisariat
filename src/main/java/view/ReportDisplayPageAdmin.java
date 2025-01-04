package main.java.view;

import main.java.database.Database;
import main.java.model.Report;
import main.java.model.User;
import main.java.utils.UsersFilterMethods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Predicate;

public class ReportDisplayPageAdmin extends ReportDisplayPage {
    private JButton statsButton;
    private final PieChartDialog pieChartDialog = new PieChartDialog();
  
    private final JComboBox<String> policemanComboBox = new JComboBox<>(
            Database.getInstance()
                    .getUsersDatabase()
                    .getFiltered(UsersFilterMethods.filterPolicemanEmails())
                    .values().stream().map(User::getEmail).toArray(String[]::new)
    );

    public ReportDisplayPageAdmin(Predicate<Report> defaultFilter) {
        super(defaultFilter);
    }

    @Override
    public JPanel generatePage(CardLayout cardLayout, JPanel mainPanel) {
        rootPanel.setLayout(cardLayout);

        reportPanel = generateReportPage(cardLayout, mainPanel);
        rootPanel.add(reportPanel, "reportPanel");

        reportTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = reportTable.rowAtPoint(e.getPoint());
                if (row != -1 && e.getClickCount() == 2) {
                    Integer reportId = (Integer) reportTable.getValueAt(row, 0);
                    showEditableReportDetails(reportId);
                }
            }
        });
        return rootPanel;
    }

    @Override
    protected JPanel createContentPanel(CardLayout cardLayout, JPanel mainPanel) {
        JPanel contentPanel = super.createContentPanel(cardLayout, mainPanel);

        for(ActionListener al : backButton.getActionListeners()){
            backButton.removeActionListener(al);
        }

        backButton.addActionListener(_ -> cardLayout.show(mainPanel, "adminPage"));

        statsButton = new JButton("Statystyki");

        statsButton.addActionListener(_ -> {
            pieChartDialog.updateDatasets();
            pieChartDialog.setVisible(true);
        });

        buttonPanel.add(statsButton);
        return contentPanel;
    }

    @Override
    protected JTable createReportTable() {
        JTable createdTable = super.createReportTable();
        createdTable.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(policemanComboBox));
        return createdTable;
    }

    protected void updateReportTable() {
        super.updateReportTable();
        reportTable.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(policemanComboBox));
    }


    private void showEditableReportDetails(Integer reportId) {
        Report report = displayedReports.get(reportId);
        if (report != null) {
            ReportDetailsDialogAdmin editDialog = new ReportDetailsDialogAdmin(null, reportId, report);
            editDialog.setVisible(true);

            try {
                Predicate<Report> emptyFilter = r -> true;

                changeDisplayedReports(emptyFilter);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}