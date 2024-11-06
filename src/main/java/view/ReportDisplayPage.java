package main.java.view;

import main.java.Main;
import main.java.model.Report;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

public class ReportDisplayPage {

    // Mapa przechowująca raporty z ich identyfikatorami (Integer jako ID)
    private final Map<Integer, Report> reports = new HashMap<>(Main.reportsDatabase.getAll());

    // Główna metoda uruchamiająca GUI
    public static void main(String[] args) {
        JFrame frame = new JFrame("Raporty");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);

        // Create CardLayout and mainPanel (required by generateReportPage)
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        // Create ReportDisplayPage and add its JPanel to mainPanel
        ReportDisplayPage reportPage = new ReportDisplayPage();
        JPanel reportPanel = reportPage.generateReportPage(cardLayout, mainPanel);
        mainPanel.add(reportPanel, "reportPanel");

        // Add mainPanel to frame and make it visible
        frame.add(mainPanel);
        frame.setMinimumSize(new Dimension(600, 400)); // Prevent window from shrinking too much
        frame.setVisible(true);
    }

    // Generate the report page
    public JPanel generateReportPage(CardLayout cardLayout, JPanel mainPanel) {
        JPanel reportPanel = createReportPanel();
        JPanel contentPanel = createContentPanel();

        // Add the content panel to the main report panel
        reportPanel.add(contentPanel, new GridBagConstraints());

        return reportPanel;
    }

    private JPanel createReportPanel() {
        JPanel reportPanel = new JPanel(new GridBagLayout());
        reportPanel.setBackground(new Color(35, 78, 117));
        reportPanel.setMinimumSize(new Dimension(600, 400));
        return reportPanel;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(240, 240, 240));
        contentPanel.setMinimumSize(new Dimension(500, 300));

        // Create the table to display report details
        JTable reportTable = createReportTable();
        JScrollPane scrollPane = new JScrollPane(reportTable);

        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        return contentPanel;
    }

    private JTable createReportTable() {
        // Column names for the table
        String[] columnNames = {"ReportID", "UserId", "Title", "Assigned Worker", "Status", "Date"};

        // Create a DefaultTableModel to hold report data
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            // Disable cell editing
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Fill the table model with data from the reports map
        for (Map.Entry<Integer, Report> entry : reports.entrySet()) {
            Integer reportId = entry.getKey();
            Report report = entry.getValue();

            Object[] row = {
                    reportId, // ReportID
                    report.getUserId(), // UserId
                    report.getTitle(), // Title
                    (report.getAssignmentWorkerID() == -1 ? "Brak" : report.getAssignmentWorkerID()), // Assigned Worker
                    report.getStatus(), // Status
                    report.getDate() // Date
            };

            model.addRow(row);
        }

        // Create the table with the model
        JTable reportTable = new JTable(model);

        // Set some table properties for better display
        reportTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        reportTable.setAutoCreateRowSorter(true); // Allow sorting by columns

        // Add row selection listener to handle row clicks
        reportTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Get selected row index
                int selectedRow = reportTable.getSelectedRow();
                if (selectedRow != -1) {
                    // Get the report ID from the selected row
                    Integer reportId = (Integer) reportTable.getValueAt(selectedRow, 0);
                    // Handle the display of report details based on the report ID
                    showReportDetails(reportId);
                }
            }
        });

        return reportTable;
    }

    // Method to show the details of the selected report
    private void showReportDetails(Integer reportId) {
        Report report = reports.get(reportId);
        if (report != null) {
            // Create a panel for displaying details of the report
            JPanel detailsPanel = new JPanel();
            detailsPanel.setLayout(new BorderLayout());

            // Title label
            JLabel titleLabel = new JLabel("Title: " + report.getTitle());
            titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
            detailsPanel.add(titleLabel, BorderLayout.NORTH);

            // Description (JTextArea with scroll)
            JTextArea descriptionField = new JTextArea(report.getDescription());
            descriptionField.setWrapStyleWord(true);
            descriptionField.setLineWrap(true);
            descriptionField.setCaretPosition(0);
            descriptionField.setEditable(false);

            // Create JScrollPane for the description
            JScrollPane scrollPane = createScrollPane(descriptionField);
            detailsPanel.add(scrollPane, BorderLayout.CENTER);

            // Show the details in a JOptionPane
            JOptionPane.showMessageDialog(null, detailsPanel, "Report Details", JOptionPane.PLAIN_MESSAGE);
        }
    }

    // Method to create the scroll pane for JTextArea (to ensure it's non-editable and scrollable)
    private JScrollPane createScrollPane(JTextArea descriptionField) {
        JScrollPane scrollPane = new JScrollPane(descriptionField);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(750, 150));  // Preferred size
        scrollPane.setMinimumSize(new Dimension(500, 150)); // Minimum size
        return scrollPane;
    }
}
