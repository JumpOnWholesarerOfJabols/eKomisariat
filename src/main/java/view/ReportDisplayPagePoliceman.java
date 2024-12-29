package main.java.view;

import main.java.model.Report;

import java.util.function.Predicate;

public class ReportDisplayPagePoliceman extends ReportDisplayPage {

    public ReportDisplayPagePoliceman(Predicate<Report> defaultFilter) {
        super(defaultFilter);
    }

    @Override
    protected void showReportDetails(Integer reportId) {
        Report report = displayedReports.get(reportId);
        if (report != null) {

            ReportDetailsDialogPoliceman detailsDialog = new ReportDetailsDialogPoliceman(null, reportId, report, this);
            detailsDialog.setVisible(true);
        }
    }
}