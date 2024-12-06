package main.java.view;

import main.java.model.Report;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public
class ReportDetailsDialogPoliceman extends ReportDetailsDialog {

    public ReportDetailsDialogPoliceman(Frame owner, Integer reportId, Report report) {
        super(owner, reportId, report);

        JButton startButton = new JButton("Rozpocznij");
        startButton.addActionListener(e -> {
            // Logic for starting the report processing
            System.out.println("Rozpoczęto przetwarzanie zgłoszenia!");
        });

        ((JPanel) getContentPane().getComponent(2)).add(startButton);
    }
}