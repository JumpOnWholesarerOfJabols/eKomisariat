package main.java;

import main.java.database.*;
import main.java.model.Notification;
import main.java.model.NotificationType;
import main.java.model.User;
import main.java.view.HomePage;
import main.java.view.LoginPage;
import main.java.view.RegisterPage;
import main.java.view.ReportPage;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.Map;

public class Main {
    //używajcie Database.getInstance().(i tutaj settery i gettery tego do czego chcecie dostęp) zamiast tego poniżej zakomentowanego

    //public static User currentUser;
    //public static final ReportsDatabase reportsDatabase = new ReportsDatabase();
    //public static final UsersDatabase usersDatabase = new UsersDatabase();
    //public static final FileDatabase<Notification> notificationDatabase = new FileDatabase<>("src/main/resources/notifications/");

    public static void main(String[] args) {
        testy();
        JFrame f = new JFrame("eKomisariat");
        f.setMinimumSize(new Dimension(1400, 900));
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Use CardLayout to manage multiple pages
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);
        f.add(mainPanel);

        // LOGIN PAGE PANEL
        LoginPage loginPage = new LoginPage(Database.getInstance().getUsersDatabase());
        JPanel loginPanel = loginPage.generatePage(cardLayout, mainPanel);

        RegisterPage registerPage = new RegisterPage(Database.getInstance().getUsersDatabase());
        JPanel registerPanel = registerPage.generatePage(cardLayout, mainPanel);
      
        mainPanel.add(loginPanel, "loginPage");
        mainPanel.add(registerPanel, "registerPage");
        //mainPanel.add(reportPanel, "reportPage");


        // Set the JFrame visible
        f.setVisible(true);
    }

    private static void testy()
    {
        String folderPath = "src/main/resources/users/";
        DatabaseOperations<User> usersDatabase = new UsersDatabase(folderPath);

//        usersDatabase.addItemToDatabase(new User("imie1", "nazwisko1", "x@gmail.com", "123"));
//        usersDatabase.addItemToDatabase(new User("imie2", "nazwisko2", "y", "123"));
//        usersDatabase.addItemToDatabase(new User("imie3", "nazwisko3", "z@gmail.com", "321"));

        Map<Integer, User> data = usersDatabase.importDataFromFile();
        data.forEach((id, user) -> System.out.println(user));

        Database.getInstance().getNotificationDatabase().addItemToDatabase(new Notification(1, NotificationType.USER_CREATED, 4, LocalDateTime.now()));
        Database.getInstance().getNotificationDatabase().addItemToDatabase(new Notification(5, NotificationType.REPORT_CREATED, 2, LocalDateTime.now().minusHours(5)));
        Database.getInstance().getNotificationDatabase().addItemToDatabase(new Notification(21, NotificationType.REPORT_ASSIGNED, 15, LocalDateTime.now().minusDays(21)));
        Database.getInstance().getNotificationDatabase().addItemToDatabase(new Notification(9, NotificationType.REPORT_MODIFIED, 3, LocalDateTime.now().minusYears(3)));
    }
}