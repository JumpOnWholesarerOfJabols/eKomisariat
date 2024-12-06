package main.java.logger;

public enum LogEventType {
    FILE_NOT_FOUND("File not found: %s"),
    FILE_DELETED("File deleted: %s"),
    ITEM_EXPORTED("Item exported: %s"),
    DATABASE_EXPORTED("Database exported: %s"),
    DATABASE_LOADED("Database loaded: %s"),
    ADMIN_LOGIN("Admin logged in: %s");

    private final String value;
    LogEventType(String value) {
        this.value = value;
    }
    public String value(){
        return value;
    }
}
