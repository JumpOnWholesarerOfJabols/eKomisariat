package main.java;

import main.java.database.DatabaseOperations;
import main.java.database.UsersDatabase;
import main.java.model.User;
import main.java.view.LoginPage;
import main.java.view.RegisterPage;
import main.java.view.ReportPage;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class Main {
    public static User currentUser;

    public static void main(String[] args) {
        //testy();
        JFrame f = new JFrame("eKomisariat");
        f.setMinimumSize(new Dimension(1400, 900));
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Use CardLayout to manage multiple pages
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);
        f.add(mainPanel);

        // LOGIN PAGE PANEL
        LoginPage loginPage = new LoginPage();
        JPanel loginPanel = loginPage.generateLoginPage(cardLayout, mainPanel);

        RegisterPage registerPage = new RegisterPage();
        JPanel registerPanel = registerPage.generateRegisterPage(cardLayout, mainPanel);


            // Add both loginPanel and newPagePanel to the CardLayout
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

        usersDatabase.addItemToDatabase(11, new User("1", "user1", "user1"));
        usersDatabase.addItemToDatabase(12, new User("2", "user22", "user22"));
        usersDatabase.addItemToDatabase(13, new User("4", "user2233", "user2233"));
        usersDatabase.addItemToDatabase(14, new User("777", "user444", "4444"));

        Map<Integer, User> data = usersDatabase.importDataFromFile();
        data.forEach((id, user) -> System.out.println(user));
    }
}