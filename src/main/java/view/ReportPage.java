package main.java.view;

import main.java.Main;
import main.java.model.Report;

import javax.swing.*;
import java.awt.*;

public class ReportPage {

    private JTextField titleField;
    private JTextArea descriptionField;
    private JButton sendButton;
    private JScrollPane scrollPane;

    // testowanie
    public static void main(String[] args) {
        JFrame frame = new JFrame("Report Page Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);

        // Create CardLayout and mainPanel (required by generateReportPage)
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        // Create ReportPage and add its JPanel to mainPanel
        ReportPage reportPage = new ReportPage();
        JPanel reportPanel = reportPage.generateReportPage(cardLayout, mainPanel);
        mainPanel.add(reportPanel, "reportPanel");

        // Add mainPanel to frame and make it visible
        frame.add(mainPanel);
        frame.setMinimumSize(new Dimension(600, 400)); // Prevent window from shrinking too much
        frame.setVisible(true);
    }

    // Generate the report page
    public JPanel generateReportPage(CardLayout cardLayout, JPanel mainPanel) {
        JPanel reportPanel = createReportPanel();
        JPanel contentPanel = createContentPanel();

        // Add the content panel to the main report panel
        reportPanel.add(contentPanel, new GridBagConstraints());

        return reportPanel;
    }

    private JPanel createReportPanel() {
        JPanel reportPanel = new JPanel(new GridBagLayout());
        reportPanel.setBackground(new Color(35, 78, 117));
        reportPanel.setMinimumSize(new Dimension(600, 400));
        return reportPanel;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(240, 240, 240));
        contentPanel.setMinimumSize(new Dimension(500, 300));

        GridBagConstraints gbc = createGridBagConstraints();

        // Create and add title field
        titleField = createTitleField();
        contentPanel.add(titleField, gbc);

        // Create and add description field with a scroll pane
        descriptionField = createDescriptionField();
        scrollPane = createScrollPane(descriptionField);
        gbc.gridy = 1;
        contentPanel.add(scrollPane, gbc);

        // Create and add send button
        sendButton = createSendButton();
        gbc.gridy = 2;
        contentPanel.add(sendButton, gbc);

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
        scrollPane.setPreferredSize(new Dimension(750, 150));  // Preferred size
        scrollPane.setMinimumSize(new Dimension(500, 150)); // Minimum size
        return scrollPane;
    }

    private JButton createSendButton() {
        JButton sendButton = new JButton("Wykonaj obowiązek obywatelski");
        sendButton.setPreferredSize(new Dimension(120, 35));

        sendButton.addActionListener(e -> {
            // Akcja po kliknięciu przycisku
            String title = titleField.getText();
            String description = descriptionField.getText();
            int userID = Main.usersDatabase.getUserId(Main.currentUser);

            if (userID == -1) {
                JOptionPane.showMessageDialog(null, "Obywatelu, wygląda na to, że Twoja teczka zaginęła! Aby spełniać swoje obywatelskie obowiązki, musisz się najpierw zameldować w systemie!", "BrakTeczkiException", JOptionPane.ERROR_MESSAGE);
            } else if (title.isEmpty() || description.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Wypełnienie pól to obowiązek każdego obywatela! Puste miejsca to tylko przestrzeń dla wrogów społeczeństwa!!", "ZaniedbanieObowiazkuException", JOptionPane.ERROR_MESSAGE);
            } else {
                Main.reportsDatabase.addItemToDatabase(new Report(userID, title, description));

                JOptionPane.showMessageDialog(null, "Donos przyjęty, obywatelu! Twoje działania pomagają w budowaniu lepszego społeczeństwa. \nTytuł: " + title + "\n\n", "Donos złożony", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return sendButton;
    }
}
