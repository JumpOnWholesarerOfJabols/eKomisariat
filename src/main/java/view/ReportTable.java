package main.java.view;

import main.java.Main;
import main.java.database.Database;
import main.java.model.Notification;
import main.java.model.NotificationType;
import main.java.model.Report;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.Map;

public class ReportTable {
    private static final int REPORT_ID_COLUMN = 0;
    private static final int POLICEMAN_COLUMN = 3;


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
                return editEnabled && column == POLICEMAN_COLUMN;
            }
        };

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

        model.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                if (e.getColumn() == POLICEMAN_COLUMN) {
                    int id = Integer.parseInt(String.valueOf(model.getValueAt(e.getFirstRow(), REPORT_ID_COLUMN)));
                    Report report = displayedReports.get(id);

                    int policemanId = Integer.parseInt(model.getValueAt(e.getFirstRow(), POLICEMAN_COLUMN).toString());
                    report.setAssignmentWorkerID(policemanId);

                    Database.getInstance().getReportsDatabase().updateItemInDatabase(id, report);
                    Database.getInstance().getNotificationDatabase().addItemToDatabase(new Notification(policemanId, NotificationType.REPORT_ASSIGNED, id, LocalDateTime.now()));
                }
            }
        });

        return reportTable;
    }
}
