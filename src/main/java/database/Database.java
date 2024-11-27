package main.java.database;

import main.java.model.Notification;
import main.java.model.User;

public class Database {
    private static Database instance;
    private User currentUser;
    private static final String usersFolderPath = "src/main/resources/users/";
    private static final String reportsFolderPath = "src/main/resources/reports/";
    private static final String notificationFolderPath = "src/main/resources/notifications/";
    private final ReportsDatabase reportsDatabase;
    private final UsersDatabase usersDatabase;
    private final FileDatabase<Notification> notificationDatabase;

    private Database() {
        this.reportsDatabase = new ReportsDatabase(reportsFolderPath);
        this.usersDatabase = new UsersDatabase(usersFolderPath);
        this.notificationDatabase = new FileDatabase<>(notificationFolderPath);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public ReportsDatabase getReportsDatabase() {
        return reportsDatabase;
    }

    public UsersDatabase getUsersDatabase() {
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
