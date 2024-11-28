package main.java.view;

import main.java.Main;
import main.java.database.Database;
import main.java.model.Notification;
import main.java.model.NotificationType;
import main.java.model.Report;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class ReportPage extends AbstractPage{
    private JTextField titleField;
    private JTextArea descriptionField;
    private JButton sendButton;
    private JScrollPane scrollPane;

    // Generate the report page
    public JPanel generatePage(CardLayout cardLayout, JPanel mainPanel) {
        rootPanel.setLayout(new CardLayout());

        JPanel reportPanel = createReportPanel();
        JPanel contentPanel = createContentPanel(cardLayout, mainPanel);

        // Add the content panel to the main report panel
        reportPanel.add(contentPanel, new GridBagConstraints());
        rootPanel.add(reportPanel, "reportForm");

        return rootPanel;
    }

    private JPanel createReportPanel() {
        JPanel reportPanel = new JPanel(new GridBagLayout());
        reportPanel.setBackground(new Color(35, 78, 117));
        reportPanel.setMinimumSize(new Dimension(600, 400));
        return reportPanel;
    }

    private JPanel createContentPanel(CardLayout cardLayout, JPanel mainPanel) {
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(240, 240, 240));
        contentPanel.setMinimumSize(new Dimension(500, 300));

        GridBagConstraints gbc = createGridBagConstraints();

        titleField = createTitleField();
        contentPanel.add(titleField, gbc);

        descriptionField = createDescriptionField();
        scrollPane = createScrollPane(descriptionField);
        gbc.gridy = 1;
        contentPanel.add(scrollPane, gbc);

        sendButton = createSendButton();
        gbc.gridy = 2;
        contentPanel.add(sendButton, gbc);

        contentPanel.add(createBackButton(cardLayout, mainPanel), gbc);

        return contentPanel;
    }

    private GridBagConstraints createGridBagConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Dynamic margins
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        return gbc;
    }

    private JTextField createTitleField() {
        JTextField titleField = new JTextField();
        titleField.setBorder(BorderFactory.createTitledBorder("Tytuł zgłoszenia"));
        return titleField;
    }

    private JTextArea createDescriptionField() {
        JTextArea descriptionField = new JTextArea();
        descriptionField.setBorder(BorderFactory.createTitledBorder("Opis sytuacji"));
        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);
        descriptionField.setRows(5);
        descriptionField.setColumns(50);
        return descriptionField;
    }

    private JScrollPane createScrollPane(JTextArea descriptionField) {
        JScrollPane scrollPane = new JScrollPane(descriptionField);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(750, 150));
        scrollPane.setMinimumSize(new Dimension(500, 150));
        return scrollPane;
    }

    private JButton createBackButton(CardLayout cardLayout, JPanel mainPanel) {
        JButton backButton = new JButton("Powrót");
        backButton.setPreferredSize(new Dimension(75, 35));
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "homePage"));

        return backButton;
    }

    private JButton createSendButton() {
        JButton sendButton = new JButton("Wykonaj obowiązek obywatelski");
        sendButton.setPreferredSize(new Dimension(120, 35));

        sendButton.addActionListener(e -> {
            String title = titleField.getText();
            String description = descriptionField.getText();
            int userID = Database.getInstance().getCurrentUserId();

            if (userID == -1) {
                JOptionPane.showMessageDialog(null, "Obywatelu, wygląda na to, że Twoja teczka zaginęła! Aby spełniać swoje obywatelskie obowiązki, musisz się najpierw zameldować w systemie!", "BrakTeczkiException", JOptionPane.ERROR_MESSAGE);
            } else if (title.isEmpty() || description.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Wypełnienie pól to obowiązek każdego obywatela! Puste miejsca to tylko przestrzeń dla wrogów społeczeństwa!!", "ZaniedbanieObowiazkuException", JOptionPane.ERROR_MESSAGE);
            } else {
                Report newReport = new Report(userID, title, description);
                Database.getInstance().getReportsDatabase().addItemToDatabase(newReport);
                Database.getInstance().getNotificationDatabase().addItemToDatabase(new Notification(0, NotificationType.REPORT_CREATED, Database.getInstance().getReportsDatabase().getItemID(newReport), LocalDateTime.now()));

                JOptionPane.showMessageDialog(null, "Donos przyjęty, obywatelu! Twoje działania pomagają w budowaniu lepszego społeczeństwa. \nTytuł: " + title + "\n\n", "Donos złożony", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return sendButton;
    }
}
