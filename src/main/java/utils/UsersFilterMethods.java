package main.java.utils;

import main.java.model.User;

import java.util.function.Predicate;

public final class UsersFilterMethods {
    private UsersFilterMethods() {}

    public static Predicate<User> filterLoginField(String emailField){
        return user -> user.getEmail().equals(emailField);
    }

    public static Predicate<User> filterPolicemanEmails(String emailField){
        return user -> user.getEmail().split("@")[1].equals("eKomisariat.pl");
    }
}