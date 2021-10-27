package fr.unice.polytech.isa.notifications.interfaces;

import fr.unice.polytech.isa.common.entities.notifications.Notification;
import fr.unice.polytech.isa.notifications.exceptions.InvalidNotificationException;

import javax.ejb.Local;
import javax.ejb.TimerHandle;

/**
 * Notification scheduling.
 * <p>
 * Schedules the notifications.
 * </p>
 */
@Local
public interface NotificationScheduling {
    /**
     * Schedules a notification.
     *
     * @param notification The notification to be scheduled.
     * @return The timer handle of the notification.
     * @throws InvalidNotificationException if the notification trigger is invalid.
     */
    TimerHandle scheduleNotification(Notification notification) throws InvalidNotificationException;
}
