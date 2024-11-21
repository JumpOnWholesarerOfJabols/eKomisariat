package main.java.view;

import main.java.Main;
import main.java.model.Notification;
import main.java.model.NotificationType;
import main.java.model.Report;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

public class NotificationTable {
    private final Map<Integer, Notification> displayedReports;

    public NotificationTable(Map<Integer, Notification> displayedReports, boolean editEnabled) {
        this.displayedReports = displayedReports;
    }

    public JTable createTable() {
        String[] columnNames = {"Rodzaj powiadomienia", "ID zmienionego obiektu"};

        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Map.Entry<Integer, Notification> entry : displayedReports.entrySet()) {
            Notification report = entry.getValue();

            Object[] row = {
                    report.type().value(),
                    report.changedEntityId()
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

        return reportTable;
    }
}
