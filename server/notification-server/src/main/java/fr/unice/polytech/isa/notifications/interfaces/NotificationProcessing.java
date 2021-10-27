package fr.unice.polytech.isa.notifications.interfaces;

import javax.ejb.Local;

/**
 * Notification processing.
 * <p>
 * Processes the notifications.
 * </p>
 */
@Local
public interface NotificationProcessing {
    /**
     * Processes a notification by identifier.
     *
     * @param identifier The notification identifier.
     */
    void processNotification(int identifier);
}
