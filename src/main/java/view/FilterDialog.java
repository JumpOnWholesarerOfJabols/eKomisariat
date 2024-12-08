package main.java.view;

import com.toedter.calendar.JDateChooser;
import main.java.model.Report;
import main.java.utils.ReportsFilterMethods;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.function.Predicate;

public class FilterDialog extends JDialog {
    private final ReportDisplayPage reportDisplayPage;
    private final JPanel parentPanel;
    private Predicate<Report> filter = null;

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

        JDateChooser startDateChooser = new JDateChooser();
        JDateChooser endDateChooser = new JDateChooser();

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
        filterPanel.add(new JLabel("Data początkowa:"));
        filterPanel.add(startDateChooser);
        filterPanel.add(new JLabel("Data końcowa:"));
        filterPanel.add(endDateChooser);

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

            LocalDate startDate = getDateFromChooser(startDateChooser);
            LocalDate endDate = getDateFromChooser(endDateChooser);

            Predicate<Report> userIdFilter = (userId != null) ? ReportsFilterMethods.filterUserId(userId) : report -> true;
            Predicate<Report> titleFilter = (reportTitle != null && !reportTitle.isEmpty()) ? ReportsFilterMethods.filterReportTitle(reportTitle) : report -> true;
            Predicate<Report> firstLetterFilter = (firstLetter != null) ? ReportsFilterMethods.filterReportTitleFirstLetter(firstLetter) : report -> true;
            Predicate<Report> workerIdFilter = (assignmentWorkerId != null) ? ReportsFilterMethods.filterReportAssigmentWorker(assignmentWorkerId) : report -> true;
            Predicate<Report> statusFilter = (status != null) ? ReportsFilterMethods.filterStatus(status) : report -> true;
            Predicate<Report> startDateFilter = (startDate != null) ? ReportsFilterMethods.filterStartDate(startDate) : report -> true;
            Predicate<Report> endDateFilter = (endDate != null) ? ReportsFilterMethods.filterEndDate(endDate) : report -> true;

            filter = ReportsFilterMethods.combinedFilter(
                    userIdFilter,
                    titleFilter,
                    firstLetterFilter,
                    workerIdFilter,
                    statusFilter,
                    startDateFilter,
                    endDateFilter
            );

            dispose();
        });

    }

    private LocalDate getDateFromChooser(JDateChooser dateChooser) {
        if (dateChooser.getDate() == null) {
            return null;
        }
        return dateChooser.getDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public Predicate<Report> getFilter() {
        return filter;
    }
}
