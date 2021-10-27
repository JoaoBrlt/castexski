package fr.unice.polytech.isa.notifications.business;

import arquillian.AbstractNotificationTest;
import fr.unice.polytech.isa.common.entities.notifications.*;
import fr.unice.polytech.isa.notifications.exceptions.InvalidNotificationException;
import fr.unice.polytech.isa.notifications.interfaces.NotificationFinder;
import fr.unice.polytech.isa.notifications.interfaces.NotificationRegistration;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.ejb.NoSuchObjectLocalException;
import javax.ejb.TimerHandle;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class NotificationRegistryBeanTest extends AbstractNotificationTest {
    @EJB
    private NotificationFinder notificationFinder;

    @EJB
    private NotificationRegistration notificationRegistry;

    @Rule
    public final ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void addNotification() throws InvalidNotificationException {
        // Create a notification.
        Notification notification = new Notification(
            NotificationChannel.SMS,
            NotificationTrigger.CRON,
            "0 * * *",
            null,
            null,
            NotificationTarget.ALL,
            null,
            null,
            null,
            "This is the message."
        );

        // Add the notification.
        int identifier = notificationRegistry.addNotification(notification);

        // The notification has been added.
        Optional<Notification> maybeNotification = notificationFinder.getNotificationById(identifier);
        assertTrue(maybeNotification.isPresent());

        // The notification is the same.
        Notification newNotification = maybeNotification.get();
        assertEquals(notification, newNotification);
    }

    @Test
    public void removeNotification() throws InvalidNotificationException {
        // Create a notification.
        Notification notification = new Notification(
            NotificationChannel.EMAIL,
            NotificationTrigger.CRON,
            "0 * * *",
            null,
            null,
            NotificationTarget.ALL,
            null,
            null,
            "Subject",
            "This is the message."
        );

        // Add the notification.
        int identifier = notificationRegistry.addNotification(notification);
        TimerHandle timerHandle = notification.getTimerHandle();

        // Remove the notification.
        notificationRegistry.removeNotification(notification);

        // The notification has been deleted.
        Optional<Notification> maybeNotification = notificationFinder.getNotificationById(identifier);
        assertFalse(maybeNotification.isPresent());

        // The notification timer has been canceled.
        exceptionRule.expect(NoSuchObjectLocalException.class);
        timerHandle.getTimer();
    }
}
