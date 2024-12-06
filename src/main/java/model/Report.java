package main.java.model;

import main.java.database.Database;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class Report implements Serializable {

    public enum reportStatus{NEW,ASSIGNED, STARTED, POSTPONED, CLOSED, DISMISSED}

    private final int userId;
    private String title;
    private String description;
    private int assignmentWorkerId;
    private reportStatus status;
    private LocalDate date;

    private static String getCurrentDate(){
        SimpleDateFormat dayMonthYear = new SimpleDateFormat("dd/MM/yyyy");
        return dayMonthYear.format(new Date());
    }

    public Report(int userID, String title, String description) {
        this.userId = userID;
        this.title = title;
        this.description = description;
        this.assignmentWorkerId = -1;
        this.status = reportStatus.NEW;
        this.date = LocalDate.now();
    }

    public void setDate(LocalDate date) {
        this.date = date;
        Database.getInstance().getReportsDatabase().updateItemInDatabase(Database.getInstance().getReportsDatabase().getItemID(this),this);
    }

    public int getUserId() {
        return userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAssignmentWorkerID() {
        return assignmentWorkerId;
    }

    public void setAssignmentWorkerID(int assignmentWorkerID) {
        this.assignmentWorkerId = assignmentWorkerID;
        updateInDatabase();
    }

    public reportStatus getStatus() {
        return status;
    }

    public void setStatus(reportStatus status) {
        this.status = status;
        updateInDatabase();
    }

    @Override
    public String toString() {
        return userId + " - " + title + " - " + description;
    }

    private void updateInDatabase(){
        Database.getInstance().getReportsDatabase().updateItemInDatabase(Database.getInstance().getReportsDatabase().getItemID(this),this);
    }
}
