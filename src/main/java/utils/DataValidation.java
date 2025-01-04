package main.java.utils;

import main.java.database.DatabaseOperations;
import main.java.model.User;


public class DataValidation {

    public static boolean isNameValid(String name) {
        return name.matches("[A-Z].{2,}");
    }

    public static boolean isEmailValid(String email) {
        return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$") && !email.contains("@eKomisariat.pl");
    }

    public static boolean isPasswordValid(String password) {
        return password.matches("^(?=.*[A-Z])(?=.*\\d).{6,}$");
    }

    public static boolean isEmailFree(DatabaseOperations<User> usersDatabase, String email) {
        boolean test = usersDatabase
                .getAll()
                .values()
                .stream()
                .noneMatch(user -> user.getEmail().equals(email));

        System.out.println(test? "True" : "False");

        return test;
    }

}
