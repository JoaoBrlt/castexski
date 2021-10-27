package fr.unice.polytech.isa.notifications.webservices;

import fr.unice.polytech.isa.common.entities.notifications.*;
import fr.unice.polytech.isa.notifications.exceptions.InvalidNotificationException;
import fr.unice.polytech.isa.notifications.exceptions.NotificationNotFoundException;
import fr.unice.polytech.isa.notifications.interfaces.NotificationFinder;
import fr.unice.polytech.isa.notifications.interfaces.NotificationRegistration;


import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;
import java.util.Optional;

/**
 * Notification service implementation.
 * <p>
 * Manages the notifications.
 * </p>
 */
@WebService(targetNamespace = "http://www.polytech.unice.fr/isa/castexski/notification")
@Stateless(name = "NotificationWS")
public class NotificationServiceImpl implements NotificationService {
    /**
     * The notification finder.
     */
    @EJB
    private NotificationFinder notificationFinder;

    /**
     * The notification registry.
     */
    @EJB
    private NotificationRegistration notificationRegistry;

    /**
     * Indicates whether a notification exists by identifier.
     *
     * @param identifier The notification identifier.
     * @return <code>true</code> if the notification exists; <code>false</code> otherwise.
     */
    @Override
    public boolean notificationExists(int identifier) {
        Optional<Notification> maybeNotification = notificationFinder.getNotificationById(identifier);
        return maybeNotification.isPresent();
    }

    /**
     * Adds a notification.
     *
     * @param channel The notification channel (ALL, SMS, EMAIL).
     * @param triggerType The notification trigger (TIMER, DATE, TIME, DATETIME, WEATHER).
     * @param triggerParameter The notification trigger parameter (depends on the trigger).
     * @param triggerCoolDown The notification trigger cool down duration (depends on the trigger).
     * @param weatherParameter The city to be used for the weather trigger (depends on the trigger).
     * @param target The notification target (ALL, MERCHANTS, CUSTOMERS, PREMIUM, EMAILS).
     * @param targetParameter The notification target parameter (depends on the target).
     * @param resortName The name of the associated resort (if using stats in the message).
     * @param subject The notification subject (only used for email notifications).
     * @param message The notification message (may contain tags).
     * @return The notification identifier.
     * @throws InvalidNotificationException if the notification is invalid.
     */
    @Override
    public int addNotification(
        String channel,
        String triggerType,
        String triggerParameter,
        String triggerCoolDown,
        String weatherParameter,
        String target,
        String targetParameter,
        String resortName,
        String subject,
        String message
    ) throws InvalidNotificationException {
        // Add the notification.
        try {
            return notificationRegistry.addNotification(
                new Notification(
                    NotificationChannel.valueOf(channel),
                    NotificationTrigger.valueOf(triggerType),
                    triggerParameter,
                    triggerCoolDown.equals("null") ? null : triggerCoolDown,
                    weatherParameter.equals("null") ? null : weatherParameter,
                    NotificationTarget.valueOf(target),
                    targetParameter.equals("null") ? null : targetParameter,
                    resortName.equals("null") ? null : resortName,
                    subject.equals("null") ? null : subject,
                    message
                )
            );
        } catch (IllegalArgumentException error) {
            throw new InvalidNotificationException("The notification parameters were invalid.");
        }
    }

    /**
     * Removes a notification by identifier.
     *
     * @param identifier The notification identifier.
     * @throws NotificationNotFoundException if the notification was not found.
     */
    @Override
    public void removeNotification(int identifier) throws NotificationNotFoundException {
        notificationRegistry.removeNotificationById(identifier);
    }
}
