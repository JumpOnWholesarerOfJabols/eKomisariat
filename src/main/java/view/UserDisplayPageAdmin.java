package main.java.view;

import main.java.database.Database;
import main.java.model.Report;
import main.java.model.User;
import main.java.utils.UsersFilterMethods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class UserDisplayPageAdmin extends ReportDisplayPage{
    private boolean filterSwitch = false;
    Map<Integer, User> displayedUsers;
    public UserDisplayPageAdmin(Predicate<Report> defaultFilter) {
        super(defaultFilter);
        displayedUsers = Database.getInstance().getUsersDatabase().getFiltered(UsersFilterMethods.filterPolicemanEmails().negate());
    }

    public void changeDisplayedUsers(Predicate<User> newFilter) {
        displayedUsers = new HashMap<>(Database.getInstance().getUsersDatabase().getFiltered(newFilter));
        updateUserTable();
    }

    private void updateUserTable() {
        reportTable.setModel(new UserTable(displayedUsers, editEnabled).createTable().getModel());
    }

    @Override
    public JPanel generatePage(CardLayout cardLayout, JPanel mainPanel) {
        rootPanel.setLayout(cardLayout);

        reportPanel = generateReportPage(cardLayout, mainPanel);
        rootPanel.add(reportPanel);

        reportTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = reportTable.rowAtPoint(e.getPoint());
                if (row != -1 && e.getClickCount() == 2) {
                    Integer userId = (Integer) reportTable.getValueAt(row, 0);
                    showEditableUserDetails(userId);
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

    private JPanel createContentPanel(CardLayout cardLayout, JPanel mainPanel) {
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(240, 240, 240));
        contentPanel.setMinimumSize(new Dimension(1000, 600));
        contentPanel.setLayout(new BorderLayout(20, 20));

        reportTable = createUserTable();
        scrollPane = new JScrollPane(reportTable);
        scrollPane.setPreferredSize(new Dimension(900, 500));
        scrollPane.setMinimumSize(new Dimension(800, 400));

        contentPanel.add(scrollPane, BorderLayout.CENTER);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        backButton = new JButton("Powrót");
        filterButton = new JButton("Policjant");

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "adminPage"));
        filterButton.addActionListener( e -> {
            if(filterSwitch) {
                displayedUsers = Database.getInstance().getUsersDatabase().getFiltered(UsersFilterMethods.filterPolicemanEmails().negate());
                filterSwitch = false;
                filterButton.setText("Policjant");
            } else {
                displayedUsers = Database.getInstance().getUsersDatabase().getFiltered(UsersFilterMethods.filterPolicemanEmails());
                filterSwitch = true;
                filterButton.setText("Użytkownik");
            }
            updateUserTable();

        });

        buttonPanel.add(backButton);
        buttonPanel.add(filterButton);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        return contentPanel;
    }

    private void showEditableUserDetails(Integer userId) {
        User user = displayedUsers.get(userId);
        if (user != null) {
            EditUserDialogAdmin editDialog = new EditUserDialogAdmin(null, userId, user);
            editDialog.setVisible(true);

            try {
                Predicate<User> emptyFilter = r -> true;
                changeDisplayedUsers(emptyFilter);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected JTable createUserTable() {
        UserTable userTableCreator = new UserTable(displayedUsers, false);
        JTable createdTable = userTableCreator.createTable();
        return createdTable;
    }
}
