package main.java.view;

import main.java.Main;
import main.java.model.Report;
import main.java.utils.ReportsFilterMethods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class ReportDisplayPageAdmin extends ReportDisplayPage {
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

    public JPanel generateReportPage(CardLayout cardLayout, JPanel mainPanel) {
        JPanel contentPanel = createContentPanel(cardLayout, mainPanel);
        reportPanel = createReportPanel();
        reportPanel.add(contentPanel, new GridBagConstraints());
        return reportPanel;
    }

    private JPanel createReportPanel() {
        JPanel reportPanel = new JPanel(new GridBagLayout());
        reportPanel.setBackground(new Color(35, 78, 117));
        reportPanel.setMinimumSize(new Dimension(800, 600));
        return reportPanel;
    }

    private JPanel createContentPanel(CardLayout cardLayout, JPanel mainPanel) {
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(240, 240, 240));
        contentPanel.setMinimumSize(new Dimension(1000, 600));
        contentPanel.setLayout(new BorderLayout(20, 20));

        reportTable = createReportTable();
        scrollPane = new JScrollPane(reportTable);
        scrollPane.setPreferredSize(new Dimension(900, 500));
        scrollPane.setMinimumSize(new Dimension(800, 400));

        contentPanel.add(scrollPane, BorderLayout.CENTER);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        filterButton = new JButton("Filtry");
        backButton = new JButton("Powrót");

        filterButton.addActionListener(e -> openFilterDialog());

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "loginPage"));

        buttonPanel.add(filterButton);
        buttonPanel.add(backButton);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        return contentPanel;
    }

    private void showEditableReportDetails(Integer reportId) {
        Report report = displayedReports.get(reportId);
        if (report != null) {
            EditReportDialogAdmin editDialog = new EditReportDialogAdmin(null, reportId, report);
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