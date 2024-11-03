package main.java.utils;

import main.java.model.User;

import java.util.function.Predicate;

public final class UsersFilterMethods {
    private UsersFilterMethods() {}

    public static Predicate<User> filterLoginField(String loginField){
        return user -> user.getLoginField().equals(loginField);
    }
}