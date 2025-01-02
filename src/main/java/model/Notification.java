package main.java.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public record Notification(int targetUserId, NotificationType type, int changedEntityId, LocalDateTime localDateTime) implements Serializable {}
