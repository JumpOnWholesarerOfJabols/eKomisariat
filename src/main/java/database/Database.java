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
    private static final String policemenFolderPath = "src/main/resources/policemen/";
    private final FileDatabase<Report> reportsDatabase;
    private final FileDatabase<User> usersDatabase;
    private final FileDatabase<Notification> notificationDatabase;
    private final FileDatabase<User> policemenDatabase;

    private Database() {
        this.policemenDatabase = new FileDatabase<>(policemenFolderPath);
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

    public FileDatabase<Report> getReportsDatabase() {
        return reportsDatabase;
    }

    public FileDatabase<User> getUsersDatabase() {
        return usersDatabase;
    }

    public FileDatabase<Notification> getNotificationDatabase() {
        return notificationDatabase;
    }

    public FileDatabase<User> getPolicemenDatabase() {
        return policemenDatabase;
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }

        return instance;
    }
}
