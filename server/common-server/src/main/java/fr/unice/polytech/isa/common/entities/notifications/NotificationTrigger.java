package fr.unice.polytech.isa.common.entities.notifications;

public enum NotificationTrigger {
    CRON,     // Triggered by a cron.
    DATE,     // Triggered by a date.
    TIME,     // Triggered by a time.
    DATETIME, // Triggered by a date and time.
    WEATHER   // Triggered by the weather.
}
