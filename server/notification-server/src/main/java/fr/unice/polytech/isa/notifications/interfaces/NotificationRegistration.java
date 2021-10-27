package fr.unice.polytech.isa.notifications.interfaces;

import fr.unice.polytech.isa.common.entities.notifications.Notification;
import fr.unice.polytech.isa.notifications.exceptions.InvalidNotificationException;
import fr.unice.polytech.isa.notifications.exceptions.NotificationNotFoundException;

import javax.ejb.Local;

/**
 * Notification registration.
 * <p>
 * Stores and manages the notifications.
 * </p>
 */
@Local
public interface NotificationRegistration {
    /**
     * Adds a notification.
     *
     * @param notification The notification to be added.
     * @return The notification identifier.
     * @throws InvalidNotificationException if the notification is invalid.
     */
    int addNotification(Notification notification) throws InvalidNotificationException;

    /**
     * Removes a notification.
     *
     * @param notification The notification to be removed.
     */
    void removeNotification(Notification notification);

    /**
     * Removes a notification by identifier.
     *
     * @param identifier The notification identifier.
     * @throws NotificationNotFoundException if the notification was not found.
     */
    void removeNotificationById(int identifier) throws NotificationNotFoundException;
}
