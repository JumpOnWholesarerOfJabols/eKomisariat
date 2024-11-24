package main.java.utils;

import main.java.model.Notification;

import java.util.function.Predicate;

public class NotificationsFilterMethods {
    public static Predicate<Notification> filterByTargetUserID(int targetUserID) {
        return e -> e.targetUserId() == targetUserID;
    }
}
