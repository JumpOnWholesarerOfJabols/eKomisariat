package main.java.model;

public enum NotificationType {
    REPORT_CREATED("Utworzono raport"),
    REPORT_MODIFIED("Zmodyfikowano raport"),
    REPORT_ASSIGNED("Przypisano Ci raport"),
    USER_CREATED("Utworzono nowego użytkownika");

    private final String value;

    NotificationType(String value) {
        this.value = value;
    }

    public String value(){
        return value;
    }
}
