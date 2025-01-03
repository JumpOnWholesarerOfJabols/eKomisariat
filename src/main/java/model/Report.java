package main.java.model;

import main.java.database.Database;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class Report implements Serializable {

    public enum reportStatus{NEW, ASSIGNED, STARTED, POSTPONED, CLOSED, DISMISSED, OPEN}

    private final int userId;
    private String title;
    private String description;
    private int assignmentWorkerId;
    private reportStatus status;
    private LocalDate date;


    public Report(int userID, String title, String description) {
        this.userId = userID;
        this.title = title;
        this.description = description;
        this.assignmentWorkerId = -1;
        this.status = reportStatus.NEW;
        this.date = LocalDate.now();
    }

    public void setDateWithoutDB(LocalDate date) {
        this.date = date;
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


    public String getDescription() {
        return description;
    }


    public int getAssignmentWorkerID() {
        return assignmentWorkerId;
    }

    public void setAssignmentWorkerID(int assignmentWorkerID) {
        this.assignmentWorkerId = assignmentWorkerID;
        updateInDatabase();
    }

    public void setAssignmentWorkerIDWithoutDB(int assignmentWorkerID) {
        this.assignmentWorkerId = assignmentWorkerID;
    }

    public reportStatus getStatus() {
        return status;
    }

    public void setStatus(reportStatus status) {
        this.status = status;
        updateInDatabase();
    }

    public void setStatusWithoutDB(reportStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return userId + " - " + title + " - " + description;
    }

    private void updateInDatabase(){
        Database.getInstance().getReportsDatabase().updateItemInDatabase(Database.getInstance().getReportsDatabase().getItemID(this),this);
    }
}
