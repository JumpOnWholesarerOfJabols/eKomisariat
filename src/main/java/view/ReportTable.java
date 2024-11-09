package main.java.view;

import main.java.Main;
import main.java.model.Report;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

public class ReportTable {

    private final Map<Integer, Report> displayedReports;
    private final boolean editEnabled;

    public ReportTable(Map<Integer, Report> displayedReports, boolean editEnabled) {
        this.displayedReports = displayedReports;
        this.editEnabled = editEnabled;
    }

    public JTable createTable() {
        String[] columnNames = {"ID Zgłoszenia", "ID Obywatela", "Tytuł Meldunku", "ID Funkcjonariusza", "Status", "Data"};

        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return editEnabled && column == 3;
            }
        };

        JComboBox<Integer> comboBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        for (Map.Entry<Integer, Report> entry : displayedReports.entrySet()) {
            Integer reportId = entry.getKey();
            Report report = entry.getValue();

            Object[] row = {
                    reportId,
                    report.getUserId(),
                    report.getTitle(),
                    report.getAssignmentWorkerID(),
                    report.getStatus(),
                    report.getDate()
            };

            model.addRow(row);
        }

        JTable reportTable = new JTable(model);
        reportTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        reportTable.setAutoCreateRowSorter(true);
        reportTable.setRowHeight(35);

        reportTable.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(comboBox));

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

        comboBox.addActionListener(e -> {
            if(reportTable.getSelectedRow() == -1){
                return;
            }

            int id = Integer.parseInt(String.valueOf(reportTable.getModel().getValueAt(reportTable.getSelectedRow(), 0)));
            Report report = displayedReports.get(id);
            report.setAssignmentWorkerID(Integer.parseInt(String.valueOf(comboBox.getSelectedItem())));

            Main.reportsDatabase.updateItemInDatabase(id, report);
        });

        return reportTable;
    }
}