package main.java.view;

import main.java.Main;
import main.java.model.Report;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class ReportDisplayPage {

    private final Map<Integer, Report> baseReports = new HashMap<>(Main.reportsDatabase.getAll());
    private final Map<Integer, Report> displayedReports = baseReports;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Raporty");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);

        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        ReportDisplayPage reportPage = new ReportDisplayPage();
        JPanel reportPanel = reportPage.generateReportPage(cardLayout, mainPanel);
        mainPanel.add(reportPanel, "reportPanel");

        frame.add(mainPanel);
        frame.setMinimumSize(new Dimension(800, 600));
        frame.setVisible(true);
    }

    public JPanel generateReportPage(CardLayout cardLayout, JPanel mainPanel) {
        JPanel reportPanel = createReportPanel();
        JPanel contentPanel = createContentPanel();

        reportPanel.add(contentPanel, new GridBagConstraints());

        return reportPanel;
    }

    private JPanel createReportPanel() {
        JPanel reportPanel = new JPanel(new GridBagLayout());
        reportPanel.setBackground(new Color(35, 78, 117));
        reportPanel.setMinimumSize(new Dimension(800, 600));
        return reportPanel;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(240, 240, 240));
        contentPanel.setMinimumSize(new Dimension(700, 400));

        JTable reportTable = createReportTable();
        JScrollPane scrollPane = new JScrollPane(reportTable);

        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        return contentPanel;
    }

    private JTable createReportTable() {
        String[] columnNames = {"ID Zgłoszenia", "ID Obywatela", "Tytuł Meldunku", "ID Funkcjonariusza", "Status", "Data"};

        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Map.Entry<Integer, Report> entry : displayedReports.entrySet()) {
            Integer reportId = entry.getKey();
            Report report = entry.getValue();

            Object[] row = {
                    reportId,
                    report.getUserId(),
                    report.getTitle(),
                    (report.getAssignmentWorkerID() == -1 ? "Brak" : report.getAssignmentWorkerID()),
                    report.getStatus(),
                    report.getDate()
            };

            model.addRow(row);
        }

        JTable reportTable = new JTable(model);
        reportTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        reportTable.setAutoCreateRowSorter(true);
        reportTable.setRowHeight(35);

        reportTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        reportTable.getTableHeader().setBackground(new Color(35, 78, 117));
        reportTable.getTableHeader().setForeground(Color.WHITE);
        reportTable.setFont(new Font("Arial", Font.PLAIN, 14));
        reportTable.setGridColor(new Color(220, 220, 220));

        reportTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (row % 2 == 0) {
                    c.setBackground(new Color(240, 240, 240));
                } else {
                    c.setBackground(Color.WHITE);
                }
                if (isSelected) {
                    c.setBackground(new Color(100, 150, 200));
                }
                return c;
            }
        });

        reportTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) { // Sprawdzenie, czy było podwójne kliknięcie
                    int selectedRow = reportTable.rowAtPoint(e.getPoint());

                    if (selectedRow != -1) {
                        Integer reportId = (Integer) reportTable.getValueAt(selectedRow, 0);
                        showReportDetails(reportId);
                    }
                }
            }
        });

        return reportTable;
    }


    private void showReportDetails(Integer reportId) {
        Report report = displayedReports.get(reportId);
        if (report != null) {
            String details = String.format(
                    "ID Zgłoszenia: %d | ID Obywatela: %s | Temat Meldunku: %s | ID Funkcjonariusza: %s | Status Zgłoszenia: %s | Data Zgłoszenia: %s",
                    reportId,
                    report.getUserId(),
                    report.getTitle(),
                    (report.getAssignmentWorkerID() == -1 ? "Brak" : report.getAssignmentWorkerID()),
                    report.getStatus(),
                    report.getDate()
            );

            JPanel detailsPanel = new JPanel(new BorderLayout());

            JLabel titleLabel = new JLabel(details);
            titleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            detailsPanel.add(titleLabel, BorderLayout.NORTH);

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

            JScrollPane scrollPane = createScrollPane(descriptionField);
            descriptionPanel.add(scrollPane, BorderLayout.CENTER);

            detailsPanel.add(descriptionPanel, BorderLayout.CENTER);

            JOptionPane.showMessageDialog(null, detailsPanel, "Report Details", JOptionPane.PLAIN_MESSAGE);
        }
    }

    private JScrollPane createScrollPane(JTextArea descriptionField) {
        JScrollPane scrollPane = new JScrollPane(descriptionField);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(900, 200));
        scrollPane.setMinimumSize(new Dimension(700, 200));
        return scrollPane;
    }
}
