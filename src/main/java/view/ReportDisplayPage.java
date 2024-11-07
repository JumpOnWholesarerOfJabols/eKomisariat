package main.java.view;

import main.java.Main;
import main.java.model.Report;
import main.java.utils.ReportsFilterMethods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class ReportDisplayPage {

    private final Map<Integer, Report> baseReports;
    private Map<Integer, Report> displayedReports;
    private final Predicate<Report> defaultFilter;
    private Predicate<Report> currentFilter;

    private JFrame frame;
    private JPanel mainPanel;
    private JPanel reportPanel;
    private JTable reportTable;
    private JScrollPane scrollPane;
    private JPanel buttonPanel;
    private JButton filterButton;
    private JButton backButton;

    public ReportDisplayPage(Predicate<Report> defaultFilter) {
        if (defaultFilter == null) {
            this.defaultFilter = r -> true;
        } else {
            this.defaultFilter = defaultFilter;
        }
        baseReports = new HashMap<>(Main.reportsDatabase.getFiltered(this.defaultFilter));
        displayedReports = baseReports;
    }

    private Predicate<Report> getFilter(Predicate<Report> newFilter) {
        return ReportsFilterMethods.combinedFilter(newFilter, defaultFilter);
    }

    public void changeDisplayedReports(Predicate<Report> newFilter) {
        displayedReports = new HashMap<>(Main.reportsDatabase.getFiltered(newFilter));
        updateReportTable();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ReportDisplayPage reportPage = new ReportDisplayPage(null);
            reportPage.initializeGUI();
        });
    }

    private void initializeGUI() {
        frame = new JFrame("Raporty");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setMinimumSize(new Dimension(800, 600));

        CardLayout cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        reportPanel = generateReportPage(cardLayout, mainPanel);
        mainPanel.add(reportPanel, "reportPanel");

        reportTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = reportTable.rowAtPoint(e.getPoint());
                if (row != -1 && e.getClickCount() == 2) {
                    Integer reportId = (Integer) reportTable.getValueAt(row, 0);
                    showReportDetails(reportId);
                }
            }
        });

        frame.add(mainPanel);
        frame.setVisible(true);
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

        filterButton = new JButton("Filtry");
        backButton = new JButton("Powrót");

        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFilterDialog();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        buttonPanel.add(filterButton);
        buttonPanel.add(backButton);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        return contentPanel;
    }

    private JTable createReportTable() {
        ReportTable reportTableCreator = new ReportTable(displayedReports);
        return reportTableCreator.createTable();
    }

    private void updateReportTable() {
        reportTable.setModel(new ReportTable(displayedReports).createTable().getModel());
    }

    private void openFilterDialog() {
        FilterDialog filterDialog = new FilterDialog(null, this, null);

        filterDialog.setVisible(true);

        try {
            currentFilter = filterDialog.getFilter();
            currentFilter = getFilter(currentFilter);
            changeDisplayedReports(currentFilter);
        } catch (NullPointerException e){
            System.out.println("Nie naciskaj krzyżyka bo nie wiem jak to naprawić aby dzialalo ladnie :DD");
        }
    }

    private void showReportDetails(Integer reportId) {
        Report report = displayedReports.get(reportId);
        if (report != null) {
            ReportDetailsDialog detailsDialog = new ReportDetailsDialog(null, reportId, report);
            detailsDialog.setVisible(true);
        }
    }

    private JScrollPane createScrollPane(JTextArea descriptionField) {
        JScrollPane scrollPane = new JScrollPane(descriptionField);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(900, 200));
        scrollPane.setMinimumSize(new Dimension(700, 200));
        return scrollPane;
    }
}
