package main.java.view;

import main.java.model.Report;

import javax.swing.*;
import java.awt.*;

public class ReportDetailsDialog extends JDialog {

    public ReportDetailsDialog(Frame owner, Integer reportId, Report report) {
        super(owner, "Report Details", true);

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(900, 400));
        setMinimumSize(new Dimension(800, 300));

        String details = String.format(
                "ID Zgłoszenia: %d | ID Obywatela: %s | Temat Meldunku: %s | ID Funkcjonariusza: %s | Status Zgłoszenia: %s | Data Zgłoszenia: %s",
                reportId,
                report.getUserId(),
                report.getTitle(),
                (report.getAssignmentWorkerID() == -1 ? "Brak" : report.getAssignmentWorkerID()),
                report.getStatus(),
                report.getDate()
        );

        JLabel titleLabel = new JLabel(details);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        add(titleLabel, BorderLayout.NORTH);

        JPanel descriptionPanel = new JPanel(new BorderLayout());
        JLabel descriptionLabel = new JLabel("Opis:");
        descriptionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        descriptionPanel.add(descriptionLabel, BorderLayout.NORTH);

        JTextArea descriptionField = new JTextArea(report.getDescription());
        descriptionField.setWrapStyleWord(true);
        descriptionField.setLineWrap(true);
        descriptionField.setCaretPosition(0);
        descriptionField.setEditable(false);
        descriptionField.setFocusable(false);
        descriptionField.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(descriptionField);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(900, 200));
        scrollPane.setMinimumSize(new Dimension(700, 200));

        descriptionPanel.add(scrollPane, BorderLayout.CENTER);
        add(descriptionPanel, BorderLayout.CENTER);

        JButton closeButton = new JButton("Zamknij");
        closeButton.addActionListener(e -> dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
    }
}
