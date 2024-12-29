package main.java.view;

import com.toedter.calendar.JDateChooser;
import main.java.database.Database;
import main.java.model.Report;
import main.java.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class EditUserDialogAdmin extends JDialog {

    public EditUserDialogAdmin(Frame owner, Integer userId, User user) {
        super(owner, "User Details", true);

        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(900, 400));
        setMinimumSize(new Dimension(800, 300));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.anchor = GridBagConstraints.NORTH;

        gbc.weightx = 0.1;
        gbc.gridx = 0;

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.insets = new Insets(10, 20, 10, 20);
        gbc2.anchor = GridBagConstraints.NORTH;
        gbc2.weightx = 0.9;

        JLabel userIdLabel = new JLabel("User Id: " + userId);
        gbc.gridy = 0;
        add(userIdLabel, gbc);

        JLabel empty = new JLabel("");
        gbc2.gridx = 1;
        add(empty, gbc2);

        JLabel nameLabel = new JLabel("Name: ");
        gbc.gridy = 1;
        add(nameLabel, gbc);

        JTextField nameField = new JTextField(user.getName());
        gbc2.gridx = 1;
        add(nameField, gbc2);

        JLabel surnameLabel = new JLabel("Surname: ");
        gbc.gridy = 2;
        add(surnameLabel, gbc);

        JTextField surnameField = new JTextField(user.getSurname());
        gbc2.gridx = 1;
        add(surnameField, gbc2);

        JLabel emailLabel = new JLabel("Email ");
        gbc.gridy = 3;
        add(emailLabel, gbc);

        JTextField emailField = new JTextField(user.getEmail());
        gbc2.gridx = 1;
        add(emailField, gbc2);

        JButton saveButton = new JButton("Zapisz");
        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                User updatedUser = new User(nameField.getText(), surnameField.getText(), emailField.getText(), user.getPassword());

                Database.getInstance().getUsersDatabase().updateItemInDatabase(userId, updatedUser);
                dispose();
            }
        });

        gbc.gridy = 6;
        add(saveButton, gbc);

        JButton delButton = new JButton("Usuń Użytkownika");
        delButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Database.getInstance().getUsersDatabase().removeItemFromDatabase(userId);

                dispose();
            }
        });

        gbc.gridy = 7;
        add(delButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        add(Box.createGlue(), gbc);

        pack();
        setLocationRelativeTo(owner);
    }
}
