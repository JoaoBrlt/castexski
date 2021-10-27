package fr.unice.polytech.isa.notifications.components;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.unice.polytech.isa.accounts.interfaces.CustomerFinder;
import fr.unice.polytech.isa.common.entities.accounts.Customer;
import fr.unice.polytech.isa.common.entities.accounts.Merchant;
import fr.unice.polytech.isa.common.entities.notifications.*;
import fr.unice.polytech.isa.merchants.interfaces.MerchantFinder;
import fr.unice.polytech.isa.notifications.exceptions.ExternalEmailServiceException;
import fr.unice.polytech.isa.notifications.exceptions.ExternalPhoneServiceException;
import fr.unice.polytech.isa.notifications.exceptions.ExternalWeatherServiceException;
import fr.unice.polytech.isa.notifications.interfaces.*;
import fr.unice.polytech.isa.statistics.interfaces.DailyReportCreator;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Notification processor bean.
 * <p>
 * Processes the notifications.
 * </p>
 */
@Stateless
public class NotificationProcessorBean implements NotificationProcessing {
    private static final Logger logger = Logger.getLogger(Logger.class.getName());

    /**
     * The entity manager.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * The notification finder.
     */
    @EJB
    private NotificationFinder notificationFinder;

    /**
     * The email notifier.
     */
    @EJB
    private EmailNotification emailNotifier;

    /**
     * The phone notifier.
     */
    @EJB
    private PhoneNotification phoneNotifier;

    /**
     * The weather forecaster.
     */
    @EJB
    private WeatherForecasting weatherForecaster;

    /**
     * The customer finder.
     */
    @EJB
    private CustomerFinder customerFinder;

    /**
     * The merchant finder.
     */
    @EJB
    private MerchantFinder merchantFinder;

    /**
     * The daily report creator.
     */
    @EJB
    private DailyReportCreator dailyReportCreator;

    /**
     * Indicates whether a notification can be sent.
     *
     * @param notification The notification to be processed.
     * @return <code>true</code> if the notification can be sent; <code>false</code> otherwise.
     */
    private boolean checkTrigger(Notification notification) {
        NotificationTrigger trigger = notification.getTriggerType();

        // Triggered by the weather.
        if (trigger == NotificationTrigger.WEATHER) {
            try {
                LocalDateTime lastTrigger = notification.getLastTrigger();
                String triggerCoolDown = notification.getTriggerCoolDown();

                // Check the trigger cool down.
                if (lastTrigger != null && triggerCoolDown != null) {
                    // Compute the duration since the last trigger.
                    Duration lastTriggerDuration = Duration.between(lastTrigger, LocalDateTime.now());

                    // Parse the cool down duration.
                    Duration coolDownDuration = Duration.parse(triggerCoolDown);

                    // The trigger cool down was not exceeded.
                    if (lastTriggerDuration.compareTo(coolDownDuration) < 0) {
                        return false;
                    }
                }

                // Check the weather.
                WeatherForecast forecast = weatherForecaster.getForecast(
                    LocalDate.now(),
                    notification.getWeatherParameter()
                );
                return forecast.isPowderAlert();
            } catch (ExternalWeatherServiceException | JsonProcessingException error) {
                logger.log(Level.WARNING, "Could not get a weather forecast: " + error.getMessage());
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the target audience of a notification.
     *
     * @param notification The notification to be processed.
     * @return The target audience of the notification.
     */
    private List<Contact> getTarget(Notification notification) {
        // Initialize the contact details.
        List<Contact> contacts = new ArrayList<>();


        switch (notification.getTarget()) {
            // Send to all the users.
            case ALL:
                // Customers.
                contacts = customerFinder
                    .getCustomers()
                    .stream()
                    .map(Customer::getContact)
                    .collect(Collectors.toList());

                // Merchants.
                contacts.addAll(merchantFinder
                    .getMerchants()
                    .stream()
                    .map(Merchant::getContact)
                    .collect(Collectors.toList()));
                break;

            // Send to the merchants.
            case MERCHANTS:
                // Get the resort name.
                String resortName = notification.getResortName();

                // Get the merchants.
                contacts = merchantFinder
                    .getMerchants()
                    .stream()
                    .filter(merchant -> {
                        // All merchants subscribed to the associated resort.
                        if (resortName != null) {
                            return merchant
                                .getResorts()
                                .stream()
                                .anyMatch(
                                    resort -> resort.equals(resortName)
                                );
                        }

                        // All merchants.
                        return true;
                    })
                    .map(Merchant::getContact)
                    .collect(Collectors.toList());
                break;

            // Send to the customers.
            case CUSTOMERS:
                contacts = customerFinder
                    .getCustomers()
                    .stream()
                    .map(Customer::getContact)
                    .collect(Collectors.toList());
                break;

            // Send to the premium customers.
            case PREMIUM:
                contacts = customerFinder
                    .getPremiumCustomers()
                    .stream()
                    .map(Customer::getContact)
                    .collect(Collectors.toList());
                break;

            // Send to specific emails.
            case EMAILS:
                contacts = Arrays.stream(notification
                    .getTargetParameter()
                    .split(","))
                    .map(email -> {
                        Contact contact = new Contact();
                        contact.setEmail(email);
                        return contact;
                    })
                    .collect(Collectors.toList());
                break;
        }

        return contacts;
    }

    /**
     * Formats the message.
     *
     * @param notification The notification to be processed.
     * @return The formatted message.
     */
    private String formatMessage(Notification notification) {
        // Get the message.
        String message = notification.getMessage();

        // Resort.
        String resortName = notification.getResortName();
        if (resortName != null) {
            // Name.
            message = message.replaceAll(
                "\\{\\{resort-name\\}\\}",
                resortName
            );

            // Statistics.
            message = message.replaceAll(
                "\\{\\{resort-stats type=daily\\}\\}",
                dailyReportCreator.constructPresenceReportOfDate(
                    resortName,
                    LocalDate.now()
                )
            );
        }

        return message;
    }

    /**
     * Sends a notification.
     *
     * @param notification The notification to be processed.
     * @param contacts The target audience of the notification.
     */
    private void sendNotification(Notification notification, List<Contact> contacts) {
        NotificationChannel channel = notification.getChannel();
        String formattedMessage = formatMessage(notification);

        // Send an email.
        if (
            channel == NotificationChannel.ALL ||
            channel == NotificationChannel.EMAIL
        ) {
            // For each contact.
            for (Contact contact : contacts) {
                contact
                    .getEmail()
                    .ifPresent(email -> {
                        try {
                            emailNotifier.sendEmail(new Email(
                                "noreply@castexski.org",
                                email,
                                notification.getSubject(),
                                formattedMessage
                            ));
                        } catch (ExternalEmailServiceException | JsonProcessingException error) {
                            logger.log(Level.WARNING, "Could not send an email notification: " + error.getMessage());
                        }
                    });
            }
        }

        // Send a text message.
        if (
            channel == NotificationChannel.ALL ||
            channel == NotificationChannel.SMS
        ) {
            // For each contact.
            for (Contact contact : contacts) {
                contact
                    .getPhoneNumber()
                    .ifPresent(phoneNumber -> {
                        try {
                            phoneNotifier.sendMessage(new SMS(
                                "1234",
                                phoneNumber,
                                formattedMessage
                            ));
                        } catch (ExternalPhoneServiceException | JsonProcessingException error) {
                            logger.log(Level.WARNING, "Could not send an text message notification: " + error.getMessage());
                        }
                    });
            }
        }
    }

    /**
     * Updates a notification.
     *
     * @param notification The notification to be processed.
     */
    private void updateNotification(Notification notification) {
        // Get the notification.
        notification = entityManager.merge(notification);

        // Update the last trigger.
        notification.setLastTrigger(LocalDateTime.now());
    }

    /**
     * Processes a notification by identifier.
     *
     * @param identifier The notification identifier.
     */
    @Override
    public void processNotification(int identifier) {
        // Find the notification.
        Optional<Notification> maybeNotification = notificationFinder.getNotificationById(identifier);
        if (maybeNotification.isPresent()) {
            // Get the notification.
            Notification notification = maybeNotification.get();

            // Check the trigger.
            if (checkTrigger(notification)) {
                // Get the target audience.
                List<Contact> contacts = getTarget(notification);

                // Send the notification.
                sendNotification(notification, contacts);

                // Update the notification.
                updateNotification(notification);
            }
        }
    }
}
