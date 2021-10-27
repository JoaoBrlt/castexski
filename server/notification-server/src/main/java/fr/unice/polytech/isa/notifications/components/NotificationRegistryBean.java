package fr.unice.polytech.isa.notifications.components;

import fr.unice.polytech.isa.common.entities.notifications.Notification;
import fr.unice.polytech.isa.notifications.exceptions.InvalidNotificationException;
import fr.unice.polytech.isa.notifications.exceptions.NotificationNotFoundException;
import fr.unice.polytech.isa.notifications.interfaces.NotificationFinder;
import fr.unice.polytech.isa.notifications.interfaces.NotificationRegistration;
import fr.unice.polytech.isa.notifications.interfaces.NotificationScheduling;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.Timer;
import javax.ejb.TimerHandle;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

/**
 * Notification registry bean.
 * <p>
 * Stores and finds the notifications.
 * </p>
 */
@Stateless
public class NotificationRegistryBean implements NotificationRegistration, NotificationFinder {
    /**
     * The entity manager.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * The notification scheduler.
     */
    @EJB
    private NotificationScheduling notificationScheduler;

    /**
     * Adds a notification.
     *
     * @param notification The notification to be added.
     * @return The notification identifier.
     * @throws InvalidNotificationException if the notification is invalid.
     */
    @Override
    public int addNotification(Notification notification) throws InvalidNotificationException {
        // Save the notification.
        entityManager.persist(notification);

        // Schedule the notification.
        TimerHandle handle = notificationScheduler.scheduleNotification(notification);
        notification.setTimerHandle(handle);

        // Return the notification identifier.
        return notification.getId();
    }

    /**
     * Removes a notification.
     *
     * @param notification The notification to be removed.
     */
    @Override
    public void removeNotification(Notification notification) {
        // Get the notification.
        notification = entityManager.merge(notification);

        // Cancel the timer.
        TimerHandle handle = notification.getTimerHandle();
        if (handle != null) {
            Timer timer = handle.getTimer();
            timer.cancel();
        }

        // Remove the notification.
        entityManager.remove(notification);
    }

    /**
     * Removes a notification by identifier.
     *
     * @param identifier The notification identifier.
     * @throws NotificationNotFoundException if the notification was not found.
     */
    @Override
    public void removeNotificationById(int identifier) throws NotificationNotFoundException {
        // Find the notification.
        Optional<Notification> maybeNotification = getNotificationById(identifier);
        if (maybeNotification.isPresent()) {
            // Remove the notification.
            Notification notification = maybeNotification.get();
            removeNotification(notification);
        } else {
            throw new NotificationNotFoundException(identifier);
        }
    }

    /**
     * Returns the list of notifications.
     *
     * @return The list of notifications.
     */
    public List<Notification> getNotifications() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Notification> criteria = builder.createQuery(Notification.class);
        Root<Notification> root = criteria.from(Notification.class);
        CriteriaQuery<Notification> all = criteria.select(root);
        TypedQuery<Notification> query = entityManager.createQuery(all);
        return query.getResultList();
    }

    /**
     * Returns a notification by identifier.
     *
     * @param identifier The notification identifier.
     * @return The requested notification.
     */
    @Override
    public Optional<Notification> getNotificationById(int identifier) {
        return Optional.ofNullable(entityManager.find(Notification.class, identifier));
    }
}
