package main.java.utils;

import main.java.model.User;

import java.util.function.Predicate;

public final class UsersFilterMethods {
    private UsersFilterMethods() {}

    public static Predicate<User> filterLoginField(String email){
        return user -> user.getEmail().equals(email);
    }
}