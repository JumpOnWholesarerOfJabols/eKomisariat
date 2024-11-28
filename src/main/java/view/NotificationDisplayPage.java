package main.java.view;

import main.java.Main;
import main.java.database.Database;
import main.java.model.Notification;
import main.java.model.NotificationType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class NotificationDisplayPage extends AbstractTablePage<Notification> {
    private final Map<Integer, Notification> baseReports;
    private Map<Integer, Notification> displayedReports;

    private JPanel reportPanel;
    private JTable reportTable;
    private JScrollPane scrollPane;
    private JPanel buttonPanel;
    private JButton backButton;

    public NotificationDisplayPage(Predicate<Notification> defaultFilter) {
        super(defaultFilter);
        baseReports = new HashMap<>(Database.getInstance().getNotificationDatabase().getFiltered(this.defaultFilter));
        displayedReports = baseReports;
    }

    public void changeDisplayedReports(Predicate<Notification> newFilter) {
        displayedReports = new HashMap<>(Database.getInstance().getNotificationDatabase().getFiltered(newFilter));
        updateReportTable();
    }

    @Override
    public JPanel generatePage(CardLayout cardLayout, JPanel mainPanel) {
        CardLayout rootCardLayout = new CardLayout();
        rootPanel.setLayout(rootCardLayout);

        reportPanel = generateReportPage(cardLayout, mainPanel);
        rootPanel.add(reportPanel, "notificationPanel");


        reportTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = reportTable.rowAtPoint(e.getPoint());
                if (row != -1 && e.getClickCount() == 2) {
                    String type = (String) reportTable.getValueAt(row, 0);
                    if(type.equals(NotificationType.REPORT_CREATED.value()) || type.equals(NotificationType.REPORT_ASSIGNED.value()) || type.equals(NotificationType.REPORT_MODIFIED.value())){
                        cardLayout.show(mainPanel, "reportDisplayPage");
                    }
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

        backButton = new JButton("Powrót");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "homePage"));

        //buttonPanel.add(filterButton);
        buttonPanel.add(backButton);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        return contentPanel;
    }

    private JTable createReportTable() {
        NotificationTable reportTableCreator = new NotificationTable(displayedReports);
        JTable createdTable = reportTableCreator.createTable();
        return createdTable;
    }

    private void updateReportTable() {
        reportTable.setModel(new NotificationTable(displayedReports).createTable().getModel());
    }
}
