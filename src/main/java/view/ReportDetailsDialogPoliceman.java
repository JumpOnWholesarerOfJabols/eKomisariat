package main.java.view;

import main.java.database.Database;
import main.java.model.Notification;
import main.java.model.NotificationType;
import main.java.model.Report;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class ReportDetailsDialogPoliceman extends ReportDetailsDialog {

    JButton startButton = new JButton("Rozpocznij postępowanie");
    JButton stopButton = new JButton("Odrocz postępowanie");
    JButton closeButton = new JButton("Zamknij postępowanie");
    JButton rejectButton = new JButton("Umorz postępowanie");

    public ReportDetailsDialogPoliceman(Frame owner, Integer reportId, Report report, ReportDisplayPagePoliceman parentPage) {
        super(owner, reportId, report);

        setupButton(startButton);
        setupButton(stopButton);
        setupButton(closeButton);
        setupButton(rejectButton);

        startButton.addActionListener(e -> {
            report.setStatus(Report.reportStatus.STARTED);
            Database.getInstance().getNotificationDatabase().addItemToDatabase(new Notification(Database.getInstance().getCurrentUserId(), NotificationType.REPORT_STATUS_STARTED, reportId, LocalDateTime.now()));
            Database.getInstance().getNotificationDatabase().addItemToDatabase(new Notification(report.getUserId(), NotificationType.REPORT_STATUS_STARTED, reportId, LocalDateTime.now()));
            updateButtons(report);
            parentPage.updateReportTable();
        });
        stopButton.addActionListener(e->{
            report.setStatus(Report.reportStatus.POSTPONED);
            Database.getInstance().getNotificationDatabase().addItemToDatabase(new Notification(Database.getInstance().getCurrentUserId(), NotificationType.REPORT_STATUS_POSTPONED, reportId, LocalDateTime.now()));
            Database.getInstance().getNotificationDatabase().addItemToDatabase(new Notification(report.getUserId(), NotificationType.REPORT_STATUS_POSTPONED, reportId, LocalDateTime.now()));
            updateButtons(report);
            parentPage.updateReportTable();
        });
        closeButton.addActionListener(e->{
            report.setStatus(Report.reportStatus.CLOSED);
            Database.getInstance().getNotificationDatabase().addItemToDatabase(new Notification(Database.getInstance().getCurrentUserId(), NotificationType.REPORT_STATUS_CLOSED, reportId, LocalDateTime.now()));
            Database.getInstance().getNotificationDatabase().addItemToDatabase(new Notification(report.getUserId(), NotificationType.REPORT_STATUS_CLOSED, reportId, LocalDateTime.now()));
            updateButtons(report);
            parentPage.updateReportTable();
        });
        rejectButton.addActionListener(e->{
            report.setStatus(Report.reportStatus.DISMISSED);
            Database.getInstance().getNotificationDatabase().addItemToDatabase(new Notification(Database.getInstance().getCurrentUserId(), NotificationType.REPORT_STATUS_DISMISSED, reportId, LocalDateTime.now()));
            Database.getInstance().getNotificationDatabase().addItemToDatabase(new Notification(report.getUserId(), NotificationType.REPORT_STATUS_DISMISSED, reportId, LocalDateTime.now()));
            updateButtons(report);
            parentPage.updateReportTable();
        });

        updateButtons(report);
    }

    private void updateButtons(Report report) {
        Container contentPane = getContentPane();
        Component component = contentPane.getComponent(2);

        if (component instanceof Container) {
            ((Container) component).removeAll();

            switch (report.getStatus()) {
                case NEW, ASSIGNED, POSTPONED -> ((Container) component).add(startButton);
                case STARTED -> {
                    ((Container) component).add(stopButton);
                    ((Container) component).add(rejectButton);
                    ((Container) component).add(closeButton);
                }
            }
            addComponents();

            component.revalidate();
            component.repaint();
        }
    }

    private void addComponents() {
        ((Container) getContentPane().getComponent(2)).add(super.closeButton);
    }
}