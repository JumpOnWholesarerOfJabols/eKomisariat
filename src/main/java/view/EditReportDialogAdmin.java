package main.java.view;

import com.toedter.calendar.JDateChooser;
import main.java.database.Database;
import main.java.model.Report;
import main.java.utils.UsersFilterMethods;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;

public class EditReportDialogAdmin extends JDialog {

    public EditReportDialogAdmin(Frame owner, Integer reportId, Report report) {
        super(owner, "Report Details", true);

        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(900, 400));
        setMinimumSize(new Dimension(800, 300));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.anchor = GridBagConstraints.NORTH;

        gbc.weightx = 0.1;
        gbc.gridx = 0;

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.insets = new Insets(10, 20, 10, 20);
        gbc2.anchor = GridBagConstraints.NORTH;
        gbc2.weightx = 0.9;

        JLabel userIdLabel = new JLabel("User Id: " + report.getUserId());
        gbc.gridy = 0;
        add(userIdLabel, gbc);

        JLabel empty = new JLabel("");
        gbc2.gridx = 1;
        add(empty, gbc2);

        JLabel titleLabel = new JLabel("Title: ");
        gbc.gridy = 1;
        add(titleLabel, gbc);

        JTextField titleField = new JTextField(report.getTitle());
        gbc2.gridx = 1;
        add(titleField, gbc2);

        JLabel descriptionLabel = new JLabel("Description: ");
        gbc.gridy = 2;
        add(descriptionLabel, gbc);

        JTextArea descriptionField = new JTextArea(report.getDescription());
        descriptionField.setRows(5);
        gbc2.gridx = 1;
        add(descriptionField, gbc2);

        JLabel assignmentWorkerIdLabel = new JLabel("Assignment Worker Id: ");
        gbc.gridy = 3;
        add(assignmentWorkerIdLabel, gbc);

        JComboBox<Integer> workerComboBox = new JComboBox<>(getWorkersId());
        workerComboBox.setSelectedItem(report.getAssignmentWorkerID());
        gbc2.gridx = 1;
        add(workerComboBox, gbc2);

        JLabel statusLabel = new JLabel("Status: ");
        gbc.gridy = 4;
        add(statusLabel, gbc);

        JComboBox<Report.reportStatus> statusComboBox = new JComboBox<>(Report.reportStatus.values());
        statusComboBox.setSelectedItem(report.getStatus());
        gbc2.gridx = 1;
        add(statusComboBox, gbc2);

        JLabel reportDateLabel = new JLabel("Date: ");
        gbc.gridy = 5;
        add(reportDateLabel, gbc);

        LocalDate localDate = report.getDate();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        JDateChooser reportDate = new JDateChooser(date);
        gbc2.gridx = 1;
        add(reportDate, gbc2);

        JButton saveButton = new JButton("Zapisz");
        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Report updatedReport = new Report(report.getUserId(), titleField.getText(), descriptionField.getText());
                updatedReport.setAssignmentWorkerID( (Integer) workerComboBox.getSelectedItem());
                updatedReport.setStatus((Report.reportStatus) statusComboBox.getSelectedItem());

                Date dateFromField = reportDate.getDate();
                LocalDate localDate = dateFromField.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                updatedReport.setDate(localDate);
                Database.getInstance().getReportsDatabase().updateItemInDatabase(reportId, updatedReport);
                dispose();
            }
        });

        gbc.gridy = 6;
        add(saveButton, gbc);

        JButton delButton = new JButton("Usuń raport");
        delButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Database.getInstance().getReportsDatabase().removeItemFromDatabase(reportId);

                dispose();
            }
        });

        gbc.gridy = 7;
        add(delButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        add(Box.createGlue(), gbc);

        pack();
        setLocationRelativeTo(owner);
    }

    private Integer[] getWorkersId() {
        Integer[] tab = new Integer[0];
        tab = Database.getInstance().getUsersDatabase().getFiltered(UsersFilterMethods.filterPolicemanEmails()).keySet().toArray(tab);
        Arrays.stream(tab).forEach(System.out::println);
        return tab;
    }
}
