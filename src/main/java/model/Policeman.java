package main.java.model;

public class Policeman extends User{
    private Report assignedReport;

    public Policeman(String name, String surname, String email, String password) {
        super(name, surname, email, password);
        this.assignedReport = null;
    }

    public Report getAssignedReport() {
        return assignedReport;
    }

    public void setAssignedReport(Report assignedReport) {
        this.assignedReport = assignedReport;
    }
}