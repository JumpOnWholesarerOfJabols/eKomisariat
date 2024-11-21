package main.java.model;

import java.io.Serializable;

public record Notification(int targetUserId, NotificationType type, int changedEntityId) implements Serializable {
}
