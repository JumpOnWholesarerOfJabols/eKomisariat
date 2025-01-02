package main.java.view;

import main.java.database.Database;
import main.java.model.Report;
import main.java.utils.ReportsFilterMethods;
import main.java.utils.UsersFilterMethods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ReportDisplayPageAdmin extends ReportDisplayPage {
    private final Map<Integer, Report> baseReports;
    private Map<Integer, Report> displayedReports;

    private JPanel reportPanel;
    private JTable reportTable;
    private JScrollPane scrollPane;
    private JPanel buttonPanel;
    private JButton filterButton;
    private JButton backButton;

    private JButton statsButton;
    private final PieChartDialog pieChartDialog = new PieChartDialog();
  
    private final JComboBox<Integer> policemanComboBox = new JComboBox<>(
            Database.getInstance()
                    .getUsersDatabase()
                    .getFiltered(UsersFilterMethods.filterPolicemanEmails())
                    .keySet().toArray(new Integer[0])
    );

    public ReportDisplayPageAdmin(Predicate<Report> defaultFilter) {
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
        rootPanel.setLayout(cardLayout);

        reportPanel = generateReportPage(cardLayout, mainPanel);
        rootPanel.add(reportPanel, "reportPanel");

        reportTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = reportTable.rowAtPoint(e.getPoint());
                if (row != -1 && e.getClickCount() == 2) {
                    Integer reportId = (Integer) reportTable.getValueAt(row, 0);
                    showEditableReportDetails(reportId);
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

        reportTable = createReportTable();
        scrollPane = new JScrollPane(reportTable);
        scrollPane.setPreferredSize(new Dimension(900, 500));
        scrollPane.setMinimumSize(new Dimension(800, 400));

        contentPanel.add(scrollPane, BorderLayout.CENTER);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        filterButton = new JButton("Filtry");
        backButton = new JButton("Powrót");
        statsButton = new JButton("Statystyki");

        filterButton.addActionListener(e -> openFilterDialog());

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "adminPage"));

        statsButton.addActionListener(e -> {
            pieChartDialog.updateDatasets();
            pieChartDialog.setVisible(true);
        });

        buttonPanel.add(filterButton);
        buttonPanel.add(statsButton);
        buttonPanel.add(backButton);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        return contentPanel;
    }

    private JTable createReportTable() {
        ReportTable reportTableCreator = new ReportTable(displayedReports, editEnabled);
        JTable createdTable = reportTableCreator.createTable();
        createdTable.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(policemanComboBox));
        return createdTable;
    }

    protected void updateReportTable() {
        reportTable.setModel(new ReportTable(displayedReports, editEnabled).createTable().getModel());
        reportTable.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(policemanComboBox));
    }


    private void showEditableReportDetails(Integer reportId) {
        Report report = displayedReports.get(reportId);
        if (report != null) {
            System.out.println("rid: " + reportId);
            EditReportDialogAdmin editDialog = new EditReportDialogAdmin(null, reportId, report);
            editDialog.setVisible(true);

            try {
                Predicate<Report> emptyFilter = r -> true;

                changeDisplayedReports(emptyFilter);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
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