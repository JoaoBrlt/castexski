package fr.unice.polytech.isa.common.entities.notifications;

public enum NotificationTarget {
    ALL,       // Send to all the users.
    MERCHANTS, // Send to the merchants.
    CUSTOMERS, // Send to the customers.
    PREMIUM,   // Send to the premium customers.
    EMAILS     // Send to specific emails.
}
