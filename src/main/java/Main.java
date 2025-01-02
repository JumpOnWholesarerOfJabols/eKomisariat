package main.java;

import main.java.view.LoginPage;
import main.java.view.RegisterPage;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        JFrame f = new JFrame("eKomisariat");
        f.setMinimumSize(new Dimension(1400, 900));
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);
        f.add(mainPanel);

        LoginPage loginPage = new LoginPage();
        JPanel loginPanel = loginPage.generatePage(cardLayout, mainPanel);

        RegisterPage registerPage = new RegisterPage();
        JPanel registerPanel = registerPage.generatePage(cardLayout, mainPanel);

        mainPanel.add(loginPanel, "loginPage");
        mainPanel.add(registerPanel, "registerPage");

        f.setVisible(true);
    }


}