package fr.unice.polytech.isa.resort.components;

import fr.unice.polytech.isa.common.entities.resort.Resort;
import fr.unice.polytech.isa.common.entities.notifications.Notification;
import fr.unice.polytech.isa.common.entities.notifications.NotificationChannel;
import fr.unice.polytech.isa.common.entities.notifications.NotificationTarget;
import fr.unice.polytech.isa.common.entities.notifications.NotificationTrigger;
import fr.unice.polytech.isa.notifications.exceptions.InvalidNotificationException;
import fr.unice.polytech.isa.notifications.interfaces.NotificationRegistration;
import fr.unice.polytech.isa.resort.exceptions.ResortNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.UnavailableNameException;
import fr.unice.polytech.isa.resort.interfaces.ResortFinder;
import fr.unice.polytech.isa.resort.interfaces.ResortRegister;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class ResortRegistryBean implements ResortFinder, ResortRegister {
    private static final Logger logger = Logger.getLogger(Logger.class.getName());

    /**
     * The entity manager.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * The notification registry.
     */
    @EJB
    private NotificationRegistration notificationRegistry;

    @Override
    public Resort findByName(String name) throws ResortNotFoundException {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Resort> criteria = builder.createQuery(Resort.class);
        Root<Resort> root = criteria.from(Resort.class);
        criteria.select(root).where(builder.equal(root.get("name"), name));
        TypedQuery<Resort> query = entityManager.createQuery(criteria);
        try {
            return query.getSingleResult();
        } catch (NoResultException nre) {
            logger.log(Level.FINEST, "No result for [" + name + "]", nre);
            throw new ResortNotFoundException(name);
        }
    }

    @Override
    public Resort findById(String id) throws ResortNotFoundException {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Resort> criteria = builder.createQuery(Resort.class);
        Root<Resort> root = criteria.from(Resort.class);
        criteria.select(root).where(builder.equal(root.get("id"), id));
        TypedQuery<Resort> query = entityManager.createQuery(criteria);
        try {
            return query.getSingleResult();
        } catch (NoResultException nre) {
            logger.log(Level.FINEST, "No result for [" + id + "]", nre);
            throw new ResortNotFoundException(id + "");
        }
    }

    @Override
    public List<Resort> findAllResorts() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Resort> criteria = builder.createQuery(Resort.class);
        Root<Resort> root = criteria.from(Resort.class);
        criteria.select(root);
        TypedQuery<Resort> query = entityManager.createQuery(criteria);
        return query.getResultList();
    }

    @Override
    public void registerResort(String name, String email, boolean isOpen, String cityName) throws UnavailableNameException {
        try {
            findByName(name);
            throw new UnavailableNameException(name);
        } catch (ResortNotFoundException error) {
            // Create the resort.
            Resort resort = new Resort(name, isOpen, email, cityName);
            entityManager.persist(resort);

            // Create the daily report notification.
            Notification dailyReportNotification = new Notification(
                NotificationChannel.EMAIL,
                NotificationTrigger.CRON,
                "*/10 * * *", // Every 10 seconds.
                null,
                null,
                NotificationTarget.EMAILS, // Sent to specific emails.
                "prefecture@example.org,mairie@example.org",
                name,
                "Daily report",
                "{{resort-stats type=daily}}"
            );

            // Create the weather alert notification.
            Notification weatherAlertNotification = new Notification(
                NotificationChannel.SMS,
                NotificationTrigger.CRON,
                "*/10 * * *", // Every 10 seconds.
                "PT30S",
                cityName,
                NotificationTarget.PREMIUM, // Sent to premium customers.
                null,
                name,
                null,
                "Hey, powdery snow is expected today at the ski resort {{resort-name}} !"
            );

            // Register the notifications.
            try {
                notificationRegistry.addNotification(dailyReportNotification);
                notificationRegistry.addNotification(weatherAlertNotification);
            } catch (InvalidNotificationException error2) {
                logger.log(Level.WARNING, "Could not create the notifications of the resort " + name + ":", error2.getMessage());
            }
        }
    }

    @Override
    public void deleteResort(String id) throws ResortNotFoundException {
        Resort resort = findById(id);
        entityManager.remove(resort);
    }
}
