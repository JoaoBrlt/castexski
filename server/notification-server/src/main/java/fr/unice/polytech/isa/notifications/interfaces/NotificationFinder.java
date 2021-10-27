package fr.unice.polytech.isa.notifications.interfaces;

import fr.unice.polytech.isa.common.entities.notifications.Notification;

import javax.ejb.Local;
import java.util.List;
import java.util.Optional;

/**
 * Notification finder.
 * <p>
 * Finds the notifications.
 * </p>
 */
@Local
public interface NotificationFinder {
    /**
     * Returns the list of notifications.
     *
     * @return The list of notifications.
     */
    List<Notification> getNotifications();

    /**
     * Returns a notification by identifier.
     *
     * @param identifier The notification identifier.
     * @return The requested notification.
     */
    Optional<Notification> getNotificationById(int identifier);
}
