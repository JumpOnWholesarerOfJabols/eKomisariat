package main.java.database;

import main.java.model.Notification;
import main.java.model.Report;
import main.java.model.User;

public class Database {
    private static Database instance;
    private User currentUser;
    private int currentUserId;
    private static final String usersFolderPath = "src/main/resources/users/";
    private static final String reportsFolderPath = "src/main/resources/reports/";
    private static final String notificationFolderPath = "src/main/resources/notifications/";
    private final FileDatabase<Report> reportsDatabase;
    private final FileDatabase<User> usersDatabase;
    private final FileDatabase<Notification> notificationDatabase;

    private Database() {
        this.reportsDatabase = new FileDatabase<>(reportsFolderPath);
        this.usersDatabase = new FileDatabase<>(usersFolderPath);
        this.notificationDatabase = new FileDatabase<>(notificationFolderPath);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public int getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        this.currentUserId = this.usersDatabase.getItemID(currentUser);
    }

    public FileDatabase<Report> getReportsDatabase() {
        return reportsDatabase;
    }

    public FileDatabase<User> getUsersDatabase() {
        return usersDatabase;
    }

    public FileDatabase<Notification> getNotificationDatabase() {
        return notificationDatabase;
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }

        return instance;
    }
}
