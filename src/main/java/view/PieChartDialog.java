package main.java.view;

import main.java.model.Report;

import javax.swing.*;
import java.awt.*;
import java.util.function.Predicate;

class PieChartDialog extends JDialog {
    private final PieChartPage pieChartPage = new PieChartPage();

    PieChartDialog() {
        super((JFrame) null, "Statystyki raportów", true);
        this.setMinimumSize(new Dimension(800, 650));
        this.setResizable(false);

        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        this.setContentPane(mainPanel);
        JPanel chartPanel = pieChartPage.generatePage(cardLayout, mainPanel);

        mainPanel.add(chartPanel, "chartPanel");
    }

    void updateDatasets(){
        pieChartPage.updateDatasets();
    }

    void updateDatasets(Predicate<Report> filter){
        pieChartPage.updateDatasets(filter);
    }
}
