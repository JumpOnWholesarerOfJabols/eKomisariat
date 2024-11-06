package main.java.view;

import main.java.model.Report;
import main.java.utils.ReportsFilterMethods;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.function.Predicate;

public class FilterDialog extends JDialog {

    private final ReportDisplayPage reportDisplayPage;
    private final JPanel parentPanel;

    public FilterDialog(Frame owner, ReportDisplayPage reportDisplayPage, JPanel parentPanel) {
        super(owner, "Filtry", true);
        this.reportDisplayPage = reportDisplayPage;
        this.parentPanel = parentPanel;

        setSize(400, 400);
        setLocationRelativeTo(owner);

        JPanel filterPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        filterPanel.setBackground(new Color(240, 240, 240));

        JTextField userIdField = new JTextField();
        JTextField assignmentWorkerField = new JTextField();
        JTextField reportTitleField = new JTextField();
        JTextField reportTitleFirstLetterField = new JTextField();
        JComboBox<Report.reportStatus> statusComboBox = new JComboBox<>(Report.reportStatus.values());
        JTextField startDateField = new JTextField("YYYY-MM-DD");
        JTextField endDateField = new JTextField("YYYY-MM-DD");

        filterPanel.add(new JLabel("ID Użytkownika:"));
        filterPanel.add(userIdField);
        filterPanel.add(new JLabel("Tytuł Meldunku:"));
        filterPanel.add(reportTitleField);
        filterPanel.add(new JLabel("Pierwsza litera tytułu:"));
        filterPanel.add(reportTitleFirstLetterField);
        filterPanel.add(new JLabel("ID Funkcjonariusza:"));
        filterPanel.add(assignmentWorkerField);
        filterPanel.add(new JLabel("Status:"));
        filterPanel.add(statusComboBox);
        filterPanel.add(new JLabel("Data początkowa (YYYY-MM-DD):"));
        filterPanel.add(startDateField);
        filterPanel.add(new JLabel("Data końcowa (YYYY-MM-DD):"));
        filterPanel.add(endDateField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton applyButton = new JButton("Zastosuj");
        JButton cancelButton = new JButton("Anuluj");

        buttonPanel.add(applyButton);
        buttonPanel.add(cancelButton);

        add(filterPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        cancelButton.addActionListener(e -> dispose());

        applyButton.addActionListener(e -> {
            Integer userId = userIdField.getText().isEmpty() ? null : Integer.parseInt(userIdField.getText());
            String reportTitle = reportTitleField.getText().isEmpty() ? null : reportTitleField.getText();
            Character firstLetter = reportTitleFirstLetterField.getText().isEmpty() ? null : reportTitleFirstLetterField.getText().charAt(0);
            Integer assignmentWorkerId = assignmentWorkerField.getText().isEmpty() ? null : Integer.parseInt(assignmentWorkerField.getText());
            Report.reportStatus status = (Report.reportStatus) statusComboBox.getSelectedItem();
            LocalDate startDate = startDateField.getText().isEmpty() ? null : LocalDate.parse(startDateField.getText());
            LocalDate endDate = endDateField.getText().isEmpty() ? null : LocalDate.parse(endDateField.getText());

            Predicate<Report> userIdFilter = ReportsFilterMethods.filterUserId(userId);
            Predicate<Report> titleFilter = ReportsFilterMethods.filterReportTitle(reportTitle);
            Predicate<Report> firstLetterFilter = ReportsFilterMethods.filterReportTitleFirstLetter(firstLetter);
            Predicate<Report> workerIdFilter = ReportsFilterMethods.filterReportAssigmentWorker(assignmentWorkerId);
            Predicate<Report> statusFilter = ReportsFilterMethods.filterStatus(status);
            Predicate<Report> startDateFilter = ReportsFilterMethods.filterStartDate(startDate);
            Predicate<Report> endDateFilter = ReportsFilterMethods.filterEndDate(endDate);

            Predicate<Report> combinedFilter = ReportsFilterMethods.combinedFilter(
                    userIdFilter,
                    titleFilter,
                    firstLetterFilter,
                    workerIdFilter,
                    statusFilter,
                    startDateFilter,
                    endDateFilter
            );

            reportDisplayPage.changeDisplayedReports(combinedFilter);

            dispose();
        });
    }
}
