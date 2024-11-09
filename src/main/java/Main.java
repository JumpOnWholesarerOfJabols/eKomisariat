package main.java;

import main.java.database.DatabaseOperations;
import main.java.database.ReportsDatabase;
import main.java.database.UsersDatabase;
import main.java.model.User;
import main.java.view.*;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class Main {
    public static User currentUser;
    public static final String folderPath = "src/main/resources/users/";
    public static final ReportsDatabase reportsDatabase = new ReportsDatabase();
    public static final UsersDatabase usersDatabase = new UsersDatabase(folderPath);

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
        LoginPage loginPage = new LoginPage(usersDatabase);
        JPanel loginPanel = loginPage.generateLoginPage(cardLayout, mainPanel);

        RegisterPage registerPage = new RegisterPage(usersDatabase);
        JPanel registerPanel = registerPage.generateRegisterPage(cardLayout, mainPanel);
      
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
    }
}