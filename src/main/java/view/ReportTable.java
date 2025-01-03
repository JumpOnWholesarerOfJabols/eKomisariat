package main.java.view;

import main.java.database.Database;
import main.java.model.Notification;
import main.java.model.NotificationType;
import main.java.model.Report;
import main.java.model.User;
import main.java.utils.UsersFilterMethods;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
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

            User policeman = Database.getInstance().getUsersDatabase().getAll().get(report.getAssignmentWorkerID());
            String email = "brak";
            if(policeman != null) {
                email = policeman.getEmail();
            }

            Object[] row = {
                    reportId,
                    report.getUserId(),
                    report.getTitle(),
                    email,
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
                    int reportId = Integer.parseInt(String.valueOf(model.getValueAt(e.getFirstRow(), REPORT_ID_COLUMN)));
                    Report report = displayedReports.get(reportId);

                    String policemanEmail = model.getValueAt(e.getFirstRow(), POLICEMAN_COLUMN).toString();
                    int policemanId = Database.getInstance()
                            .getUsersDatabase()
                            .getFiltered(UsersFilterMethods.filterLoginField(policemanEmail))
                                    .entrySet().stream().findFirst().get().getKey();

                    report.setAssignmentWorkerID(policemanId);
                    report.setStatus(Report.reportStatus.ASSIGNED);

                    int userId = report.getUserId();

                    Database.getInstance().getReportsDatabase().updateItemInDatabase(reportId, report);
                    Database.getInstance().getNotificationDatabase().addItemToDatabase(new Notification(policemanId, NotificationType.REPORT_ASSIGNED, reportId, LocalDateTime.now()));
                    Database.getInstance().getNotificationDatabase().addItemToDatabase(new Notification(userId, NotificationType.REPORT_ASSIGNED, reportId, LocalDateTime.now()));
                }
            }
        });

        return reportTable;
    }
}
