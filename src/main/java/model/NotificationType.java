package main.java.model;

public enum NotificationType {
    REPORT_CREATED("Utworzono raport"),
    REPORT_MODIFIED("Zmodyfikowano raport"),
    REPORT_ASSIGNED("Zgłoszenie przypisane"),
    USER_CREATED("Utworzono nowego użytkownika"),
    REPORT_STATUS_NEW("Zgłoszenie przyjęte"),
    REPORT_STATUS_STARTED("Postępowanie rozpoczęte"),
    REPORT_STATUS_POSTPONED("Postępowanie odroczone"),
    REPORT_STATUS_DISMISSED("Postępowanie umorzone"),
    REPORT_STATUS_CLOSED("Postępowanie zamknięte");


    private final String value;

    NotificationType(String value) {
        this.value = value;
    }

    public String value(){
        return value;
    }
}
