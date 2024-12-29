package main.java.view;

import main.java.model.Report;

import javax.swing.*;
import java.awt.*;

public class ReportDetailsDialogPoliceman extends ReportDetailsDialog {

    JButton startButton = new JButton("Rozpocznij postępowanie");
    JButton stopButton = new JButton("Odrocz postępowanie");
    JButton closeButton = new JButton("Zamknij postępowanie");
    JButton rejectButton = new JButton("Umorz postępowanie");

    public ReportDetailsDialogPoliceman(Frame owner, Integer reportId, Report report, ReportDisplayPagePoliceman parentPage) {
        super(owner, reportId, report);

        startButton.addActionListener(e -> {
            report.setStatus(Report.reportStatus.STARTED);
            updateButtons(report);
            parentPage.updateReportTable();
        });
        stopButton.addActionListener(e->{
            report.setStatus(Report.reportStatus.POSTPONED);
            updateButtons(report);
            parentPage.updateReportTable();
        });
        closeButton.addActionListener(e->{
            report.setStatus(Report.reportStatus.CLOSED);
            updateButtons(report);
            parentPage.updateReportTable();
        });
        rejectButton.addActionListener(e->{
            report.setStatus(Report.reportStatus.DISMISSED);
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

            addComponents();
            switch (report.getStatus()) {
                case NEW, ASSIGNED, POSTPONED -> ((Container) component).add(startButton);
                case STARTED -> {
                    ((Container) component).add(stopButton);
                    ((Container) component).add(rejectButton);
                    ((Container) component).add(closeButton);
                }
            }

            component.revalidate();
            component.repaint();
        }
    }

    private void addComponents() {
        ((Container) getContentPane().getComponent(2)).add(super.closeButton);
    }
}