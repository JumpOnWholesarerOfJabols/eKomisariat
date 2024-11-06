package main.java.utils;

import main.java.model.Report;

import java.util.Comparator;

public final class ReportsSortMethods {
    public static Comparator<Report> sortByUserId() {
        return Comparator.comparingInt(Report::getUserId);
    }

    public static Comparator<Report> sortByTitle() {
        return Comparator.comparing(Report::getTitle);
    }

    public static Comparator<Report> sortByAssignmentWorkerId() {
        return Comparator.comparingInt(Report::getAssignmentWorkerID);
    }

    public static Comparator<Report> sortByStatus() {
        return Comparator.comparing(Report::getStatus);
    }

    public static Comparator<Report> sortByDate() {
        return Comparator.comparing(Report::getDate);
    }

    public static Comparator<Report> reversed(Comparator<Report> comparator) {
        return comparator.reversed();
    }

    public static Comparator<Report> combinedSort(Comparator<Report>... comparators) {
        Comparator<Report> combinedComparator = (report1, report2) -> 0;

        for (Comparator<Report> comparator : comparators) {
            combinedComparator = combinedComparator.thenComparing(comparator);
        }

        return combinedComparator;
    }
}
