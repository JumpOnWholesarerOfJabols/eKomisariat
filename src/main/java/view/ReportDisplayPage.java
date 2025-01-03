package main.java.view;

import main.java.database.Database;
import main.java.model.Report;
import main.java.utils.ReportsFilterMethods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class ReportDisplayPage extends AbstractTablePage<Report> {
    protected final Map<Integer, Report> baseReports;
    protected Map<Integer, Report> displayedReports;

    protected JPanel reportPanel;
    protected JTable reportTable;
    protected JScrollPane scrollPane;
    protected JPanel buttonPanel;
    protected JButton filterButton;
    protected JButton backButton;

    public ReportDisplayPage(Predicate<Report> defaultFilter) {
        super(defaultFilter);
        baseReports = new HashMap<>(Database.getInstance().getReportsDatabase().getFiltered(this.defaultFilter));
        displayedReports = baseReports;
    }

    private Predicate<Report> getFilter(Predicate<Report> newFilter) {
        return ReportsFilterMethods.combinedFilter(newFilter, defaultFilter);
    }

    public void changeDisplayedReports(Predicate<Report> newFilter) {
        displayedReports = new HashMap<>(Database.getInstance().getReportsDatabase().getFiltered(newFilter));
        updateReportTable();
    }

    @Override
    public JPanel generatePage(CardLayout cardLayout, JPanel mainPanel) {
        CardLayout rootCardLayout = new CardLayout();
        rootPanel.setLayout(rootCardLayout);

        reportPanel = generateReportPage(cardLayout, mainPanel);
        rootPanel.add(reportPanel, "reportPanel");

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
        return rootPanel;
    }

    public JPanel generateReportPage(CardLayout cardLayout, JPanel mainPanel) {
        JPanel contentPanel = createContentPanel(cardLayout, mainPanel);
        reportPanel = createReportPanel();
        reportPanel.add(contentPanel, new GridBagConstraints());
        return reportPanel;
    }

    protected JPanel createReportPanel() {
        JPanel reportPanel = new JPanel(new GridBagLayout());
        reportPanel.setBackground(new Color(35, 78, 117));
        reportPanel.setMinimumSize(new Dimension(800, 600));
        return reportPanel;
    }

    protected JPanel createContentPanel(CardLayout cardLayout, JPanel mainPanel) {
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

        filterButton.addActionListener(e -> openFilterDialog());

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "homePage"));

        buttonPanel.add(filterButton);
        buttonPanel.add(backButton);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        return contentPanel;
    }

    protected JTable createReportTable() {
        ReportTable reportTableCreator = new ReportTable(displayedReports, editEnabled);
        return reportTableCreator.createTable();
    }

    protected void updateReportTable() {
        reportTable.setModel(new ReportTable(displayedReports, editEnabled).createTable().getModel());
    }

    protected void openFilterDialog() {
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

    protected void showReportDetails(Integer reportId) {
        Report report = displayedReports.get(reportId);
        if (report != null) {
            ReportDetailsDialog detailsDialog = new ReportDetailsDialog(null, reportId, report);
            detailsDialog.setVisible(true);
        }
    }
}
