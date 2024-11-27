package main.java.database;

import main.java.model.User;

public class Database {
    public static Database instance;
    public static User currentUser;
    private static final String usersFolderPath = "src/main/resources/users/";
    private static final String reportsFolderPath = "src/main/resources/reports/";
    private final ReportsDatabase reportsDatabase;
    private final UsersDatabase usersDatabase;

    private Database() {
        this.reportsDatabase = new ReportsDatabase(reportsFolderPath);
        this.usersDatabase = new UsersDatabase(usersFolderPath);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        Database.currentUser = currentUser;
    }

    public ReportsDatabase getReportsDatabase() {
        return reportsDatabase;
    }

    public UsersDatabase getUsersDatabase() {
        return usersDatabase;
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }

        return instance;
    }
}
