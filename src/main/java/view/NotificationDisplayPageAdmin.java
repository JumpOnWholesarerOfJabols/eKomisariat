package main.java.view;

import main.java.model.Notification;

import javax.swing.*;
import java.awt.*;
import java.util.function.Predicate;

public class NotificationDisplayPageAdmin extends NotificationDisplayPage{

    public NotificationDisplayPageAdmin(Predicate<Notification> defaultFilter) {
        super(defaultFilter);
    }

    @Override
    public JPanel generatePage(CardLayout cardLayout, JPanel mainPanel) {
        CardLayout rootCardLayout = new CardLayout();
        rootPanel.setLayout(rootCardLayout);

        reportPanel = generateReportPage(cardLayout, mainPanel);
        rootPanel.add(reportPanel, "notificationPanel");

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
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "adminPage"));

        //buttonPanel.add(filterButton);
        buttonPanel.add(backButton);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        return contentPanel;
    }
}
