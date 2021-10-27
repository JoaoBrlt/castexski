package fr.unice.polytech.isa.notifications.webservices;

import fr.unice.polytech.isa.notifications.exceptions.InvalidNotificationException;
import fr.unice.polytech.isa.notifications.exceptions.NotificationNotFoundException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Notification service.
 * <p>
 * Manages the notifications.
 * </p>
 */
@WebService(targetNamespace = "http://www.polytech.unice.fr/isa/castexski/notification")
public interface NotificationService {
    /**
     * Indicates whether a notification exists by identifier.
     *
     * @param identifier The notification identifier.
     * @return <code>true</code> if the notification exists; <code>false</code> otherwise.
     */
    @WebMethod
    boolean notificationExists(
        @WebParam(name="identifier") int identifier
    );

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
    @WebMethod
    int addNotification(
        @WebParam(name="channel") String channel,
        @WebParam(name="triggerType") String triggerType,
        @WebParam(name="triggerParameter") String triggerParameter,
        @WebParam(name="triggerCoolDown") String triggerCoolDown,
        @WebParam(name="weatherParameter") String weatherParameter,
        @WebParam(name="target") String target,
        @WebParam(name="targetParameter") String targetParameter,
        @WebParam(name="resortName") String resortName,
        @WebParam(name="subject") String subject,
        @WebParam(name="message") String message
    ) throws InvalidNotificationException;

    /**
     * Removes a notification by identifier.
     *
     * @param identifier The notification identifier.
     * @throws NotificationNotFoundException if the notification was not found.
     */
    @WebMethod
    void removeNotification(
        @WebParam(name="identifier") int identifier
    ) throws NotificationNotFoundException;
}
