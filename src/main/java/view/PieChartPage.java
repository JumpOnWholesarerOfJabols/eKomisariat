package main.java.view;

import main.java.database.Database;
import main.java.model.Report;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class PieChartPage extends AbstractPage{
    private final DefaultPieDataset<Report.reportStatus> byStatusDataset = new DefaultPieDataset<>();
    private final DefaultPieDataset<DayOfWeek> byDOWDataset = new DefaultPieDataset<>();
    private final Dimension btnDim = new Dimension(160, 20);
    private final Dimension chartDim = new Dimension(600, 500);

    @Override
    public JPanel generatePage(CardLayout cardLayout, JPanel mainPanel) {
        rootPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        CardLayout chartCardLayout = new CardLayout();
        JPanel contentPanel = new JPanel(chartCardLayout);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton statusButton = new JButton("Według statusu");
        JButton dowButton = new JButton("Według dnia tygodnia");

        buttonPanel.add(statusButton);
        buttonPanel.add(dowButton);

        statusButton.addActionListener(e -> chartCardLayout.show(contentPanel, "byStatus"));
        statusButton.setPreferredSize(btnDim);

        dowButton.addActionListener(e -> chartCardLayout.show(contentPanel, "byDOW"));
        dowButton.setPreferredSize(btnDim);

        updateDatasets();

        JFreeChart chartByStatus = ChartFactory.createPieChart("Raporty według statustu", byStatusDataset, true, true, false);
        JFreeChart chartByDOW = ChartFactory.createPieChart("Raporty według dnia tygodnia", byDOWDataset, true, true, false);

        ChartPanel statusPanel = new ChartPanel(chartByStatus);
        ChartPanel dowPanel = new ChartPanel(chartByDOW);

        statusPanel.setPreferredSize(chartDim);
        dowPanel.setPreferredSize(chartDim);

        contentPanel.add(statusPanel, "byStatus");
        contentPanel.add(dowPanel, "byDOW");

        rootPanel.add(contentPanel);
        rootPanel.add(buttonPanel);

        chartCardLayout.show(contentPanel, "byStatus");
        return rootPanel;
    }

    void updateDatasets(){
        Collection<Report> reps = Database.getInstance()
                .getReportsDatabase()
                .getAll()
                .values();

        Map<Report.reportStatus, List<Report>> repsByStatus = reps.stream().collect(Collectors.groupingBy(Report::getStatus));
        Map<DayOfWeek, List<Report>> repsByDOW = reps.stream().collect(Collectors.groupingBy(r -> r.getDate().getDayOfWeek()));

        repsByStatus.forEach((k, v) -> byStatusDataset.setValue(k, v.size()));
        repsByDOW.forEach((k, v) -> byDOWDataset.setValue(k, v.size()));
    }
}