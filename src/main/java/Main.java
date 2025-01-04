package main.java;


import main.java.view.AuthenticationPage.AuthenticationPage;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        JFrame f = new JFrame("eKomisariat");
        f.setMinimumSize(new Dimension(1400, 900));
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setUndecorated(true);

        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);
        f.add(mainPanel);

        AuthenticationPage loginPage = new AuthenticationPage();
        JPanel loginPanel = loginPage.generatePage(cardLayout, mainPanel);


        mainPanel.add(loginPanel, "loginPage");

        f.setVisible(true);
    }


}