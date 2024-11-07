package main.java.utils;

import main.java.model.Report;

import java.time.LocalDate;
import java.util.function.Predicate;

public final class ReportsFilterMethods {
    private ReportsFilterMethods() {}

    public static Predicate<Report> filterUserId(int id) {
        return report -> report.getUserId() == id;
    }

    public static Predicate<Report> filterReportTitle(String title) {
        return report -> report.getTitle().equals(title);
    }

    public static Predicate<Report> filterReportTitleFirstLetter(char letter) {
        return report -> report.getTitle().charAt(0) == letter;
    }

    public static Predicate<Report> filterReportAssigmentWorker(int workerId) {
        return report -> report.getAssignmentWorkerID() == workerId;
    }

    public static Predicate<Report> filterStatus(Report.reportStatus status) {
        return report -> report.getStatus() == status;
    }

    public static Predicate<Report> filterStartDate(LocalDate startDate) {
        return report -> report.getDate().isAfter(startDate);
    }

    public static Predicate<Report> filterEndDate(LocalDate endDate) {
        return report -> report.getDate().isBefore(endDate);
    }

    public static Predicate<Report> combinedFilter(Predicate<Report>... filters) {
        Predicate<Report> combinedPredicate = report -> true;

        for (Predicate<Report> filter : filters) {
            combinedPredicate = combinedPredicate.and(filter);
        }

        return combinedPredicate;
    }
}