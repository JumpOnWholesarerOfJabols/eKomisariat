package main.java.view;

import main.java.database.DatabaseOperations;
import main.java.model.Policeman;

import javax.swing.*;
import java.awt.*;
import java.util.function.Predicate;

public class EmployeesPage extends AbstractTablePage<Policeman> {
    private final DatabaseOperations<Policeman> policemenDatabase;

    public EmployeesPage(Predicate<Policeman> defaultFilter, DatabaseOperations<Policeman> policemenDatabase) {
        super(defaultFilter);
        this.policemenDatabase = policemenDatabase;
    }

    @Override
    public JPanel generatePage(CardLayout cardLayout, JPanel mainPanel) {
        return null;
    }
}
