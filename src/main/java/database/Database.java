package main.java.database;

import main.java.model.Notification;
import main.java.model.Report;
import main.java.model.User;

public final class Database {
    private static Database instance;
    private User currentUser;
    private int currentUserId;
    private static final String USERS_FOLDER_PATH = "src/main/resources/users/";
    private static final String REPORTS_FOLDER_PATH = "src/main/resources/reports/";
    private static final String NOTIFICATION_FOLDER_PATH = "src/main/resources/notifications/";
    private final DatabaseOperations<Report> reportsDatabase;
    private final DatabaseOperations<User> usersDatabase;
    private final DatabaseOperations<Notification> notificationDatabase;

    private Database() {
        this.reportsDatabase = new FileDatabase<>(REPORTS_FOLDER_PATH);
        this.usersDatabase = new FileDatabase<>(USERS_FOLDER_PATH);
        this.notificationDatabase = new FileDatabase<>(NOTIFICATION_FOLDER_PATH);
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

    public DatabaseOperations<Report> getReportsDatabase() {
        return reportsDatabase;
    }

    public DatabaseOperations<User> getUsersDatabase() {
        return usersDatabase;
    }

    public DatabaseOperations<Notification> getNotificationDatabase() {
        return notificationDatabase;
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }

        return instance;
    }
}
