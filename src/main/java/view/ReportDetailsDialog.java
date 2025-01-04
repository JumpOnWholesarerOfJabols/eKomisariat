package main.java.view;

import main.java.model.Report;
import main.java.utils.ColorsTemplate;

import javax.swing.*;
import java.awt.*;

public class ReportDetailsDialog extends JDialog {

    protected JButton closeButton = new JButton("Zamknij");
    protected Report report;

    public ReportDetailsDialog(Frame owner, Integer reportId, Report report) {
        super(owner, "Szczegóły Zgłoszenia", true);

        this.report = report;

        setLayout(new BorderLayout(10, 10));
        setupDialogProperties(owner);
        setupTitle(reportId, report);
        setupDescription(report);
        
        setupButton(closeButton);
        closeButton.addActionListener(e -> dispose());

        setupButtonPanel();
        pack();
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void setupDialogProperties(Frame owner) {
        getContentPane().setBackground(ColorsTemplate.BACKGROUND_COLOR.getColor());
        setUndecorated(true);
        getRootPane().setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorsTemplate.BORDER_COLOR.getColor(), 2, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        setPreferredSize(new Dimension(900, 450));
        setMinimumSize(new Dimension(800, 350));
    }

    private void setupTitle(Integer reportId, Report report) {
        String details = String.format(
                "<html><div style='font-size:12px;color:%s;'><b>ID Zgłoszenia:</b> %d<br>" +
                        "<b>ID Obywatela:</b> %s<br><b>Temat Meldunku:</b> %s<br>" +
                        "<b>ID Funkcjonariusza:</b> %s<br><b>Status Zgłoszenia:</b> %s<br>" +
                        "<b>Data Zgłoszenia:</b> %s</div></html>",
                toHex(ColorsTemplate.TITLE_TEXT_COLOR.getColor()),
                reportId,
                report.getUserId(),
                report.getTitle(),
                (report.getAssignmentWorkerID() == -1 ? "Brak" : report.getAssignmentWorkerID()),
                report.getStatus(),
                report.getDate()
        );

        JLabel titleLabel = new JLabel(details);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);
    }

    protected void setupDescription(Report report) {
        JPanel descriptionPanel = new JPanel(new BorderLayout());
        descriptionPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ColorsTemplate.BORDER_COLOR.getColor()),
                "Opis zgłoszenia", 0, 0, new Font("Arial", Font.BOLD, 14), ColorsTemplate.TITLE_TEXT_COLOR.getColor()));
        descriptionPanel.setBackground(ColorsTemplate.BACKGROUND_COLOR.getColor());

        JTextArea descriptionField = new JTextArea(report.getDescription());
        descriptionField.setWrapStyleWord(true);
        descriptionField.setLineWrap(true);
        descriptionField.setCaretPosition(0);
        descriptionField.setEditable(false);
        descriptionField.setFocusable(false);
        descriptionField.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionField.setBackground(ColorsTemplate.DESCRIPTION_BACKGROUND_COLOR.getColor());
        descriptionField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(descriptionField);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(880, 250));
        scrollPane.setBorder(BorderFactory.createLineBorder(ColorsTemplate.BORDER_COLOR.getColor()));
        descriptionPanel.add(scrollPane, BorderLayout.CENTER);

        add(descriptionPanel, BorderLayout.CENTER);
    }

    protected void setupButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(200, 40));
        button.setBackground(ColorsTemplate.BUTTON_BACKGROUND_COLOR.getColor());
        button.setForeground(ColorsTemplate.BUTTON_TEXT_COLOR.getColor());
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(ColorsTemplate.BORDER_COLOR.getColor()));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(ColorsTemplate.BUTTON_HOVER_COLOR.getColor());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(ColorsTemplate.BUTTON_BACKGROUND_COLOR.getColor());
            }
        });
    }


    private void setupButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(ColorsTemplate.BACKGROUND_COLOR.getColor());
        buttonPanel.add(closeButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private String toHex(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }
}
