package main.java.utils;

import main.java.model.User;

import java.util.function.Predicate;

public final class UsersFilterMethods {
    private UsersFilterMethods() {}

    public static Predicate<User> filterLoginField(String emailField){
        return user -> user.getEmail().equalsIgnoreCase(emailField);
    }

    public static Predicate<User> filterPolicemanEmails(){
        return user -> user.getEmail().contains("eKomisariat.pl");
    }
    public static Predicate<User> filterUsersEmails(){
        return user -> !user.getEmail().contains("eKomisariat.pl");
    }
}