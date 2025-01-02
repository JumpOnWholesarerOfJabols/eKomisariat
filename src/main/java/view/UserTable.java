package main.java.view;

import main.java.database.Database;
import main.java.model.Notification;
import main.java.model.NotificationType;
import main.java.model.Report;
import main.java.model.User;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.Map;

public class UserTable {
    private final Map<Integer, User> displayedUsers;
    private final boolean editEnabled;

    public UserTable(Map<Integer, User> displayedUsers, boolean editEnabled) {
        this.displayedUsers = displayedUsers;
        this.editEnabled = editEnabled;
    }

    public JTable createTable() {
        String[] columnNames = {"ID Usera", "Imie", "Nazwisko", "Emial"};

        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return editEnabled;
            }
        };

        for (Map.Entry<Integer, User> entry : displayedUsers.entrySet()) {
            Integer userId = entry.getKey();
            User user = entry.getValue();

            Object[] row = {
                    userId,
                    user.getName(),
                    user.getSurname(),
                    user.getEmail()
            };

            model.addRow(row);
        }

        JTable userTable = new JTable(model);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.setAutoCreateRowSorter(true);
        userTable.setRowHeight(35);

        userTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        userTable.getTableHeader().setBackground(new Color(35, 78, 117));
        userTable.getTableHeader().setForeground(Color.WHITE);
        userTable.setFont(new Font("Arial", Font.PLAIN, 14));
        userTable.setGridColor(new Color(220, 220, 220));

        userTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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


        return userTable;
    }
}
