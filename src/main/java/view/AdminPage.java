package main.java.view;

import main.java.Main;
import main.java.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdminPage {


    public JPanel generatePage(CardLayout cardLayout, JPanel mainPanel) {
        ReportDisplayPageAdmin reportDisplayPageAdmin = new ReportDisplayPageAdmin(null);

        JPanel adminPanel = new JPanel();
        adminPanel.setLayout(new GridBagLayout());
        adminPanel.setBackground(new Color(35, 78, 117));
        adminPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 20, 10, 20);

        JPanel adminMainPanel = new JPanel();
        adminMainPanel.setLayout(new GridBagLayout());
        adminMainPanel.setBackground(new Color(81, 145, 203));

        JPanel reportsPanel = reportDisplayPageAdmin.generatePage(cardLayout, mainPanel);

        adminMainPanel.add(reportsPanel, gbc);

        adminPanel.add(adminMainPanel, gbc);

        return adminPanel;
    }

}
