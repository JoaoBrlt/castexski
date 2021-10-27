package fr.unice.polytech.isa.notifications.components;

import fr.unice.polytech.isa.common.entities.notifications.Notification;
import fr.unice.polytech.isa.notifications.exceptions.InvalidNotificationException;
import fr.unice.polytech.isa.notifications.interfaces.NotificationProcessing;
import fr.unice.polytech.isa.notifications.interfaces.NotificationScheduling;

import javax.annotation.Resource;
import javax.ejb.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Notification scheduler bean.
 * <p>
 * Schedules the notifications.
 * </p>
 */
@Singleton
@Startup
public class NotificationSchedulerBean implements NotificationScheduling {
    /**
     * The timer service.
     */
    @Resource
    private TimerService timerService;

    /**
     * The notification processor.
     */
    @EJB
    private NotificationProcessing notificationProcessor;

    /**
     * Creates a cron schedule expression from a cron expression.
     *
     * Format : "s m h d M e"
     * Defaults :
     * - Seconds, Minutes, Hours = "0"
     * - Day of month, Month, Day of week = "*"
     *
     * @param cronExpression The cron expression to be converted.
     * @return The schedule expression.
     */
    private ScheduleExpression createCronSchedule(String cronExpression) {
        // Initialize the schedule expression.
        ScheduleExpression scheduleExpression = new ScheduleExpression();

        // Set the schedule options.
        String[] items = cronExpression.split("\\s+");
        for (int i = 0; i < items.length; i++) {
            String item = items[i];
            switch (i) {
                // Seconds.
                case 0:
                    scheduleExpression.second(item);
                    break;
                // Minutes.
                case 1:
                    scheduleExpression.minute(item);
                    break;
                // Hours.
                case 2:
                    scheduleExpression.hour(item);
                    break;
                // Day of month.
                case 3:
                    scheduleExpression.dayOfMonth(item);
                    break;
                // Month.
                case 4:
                    scheduleExpression.month(item);
                    break;
                // Day of week.
                case 5:
                    scheduleExpression.dayOfWeek(item);
                    break;
            }
        }

        return scheduleExpression;
    }

    /**
     * Creates a date schedule expression from a date expression.
     *
     * Format : "yyyy-MM-dd"
     *
     * @param dateExpression The date expression to be converted.
     * @return The schedule expression.
     */
    private ScheduleExpression createDateSchedule(String dateExpression) {
        // Parse the date and time.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime dateTime = LocalDateTime.parse(dateExpression, formatter);

        // Return the schedule expression.
        return new ScheduleExpression()
            .dayOfMonth(dateTime.getDayOfMonth())
            .month(dateTime.getMonthValue())
            .year(dateTime.getYear());
    }

    /**
     * Creates a time schedule expression from a time expression.
     *
     * Format : "HH:mm:ss"
     *
     * @param dateExpression The time expression to be converted.
     * @return The schedule expression.
     */
    private ScheduleExpression createTimeSchedule(String dateExpression) {
        // Parse the date and time.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime time = LocalTime.parse(dateExpression, formatter);

        // Return the schedule expression.
        return new ScheduleExpression()
            .second(time.getSecond())
            .minute(time.getMinute())
            .hour(time.getHour());
    }

    /**
     * Creates a datetime schedule expression from a date expression.
     *
     * Format : "yyyy-MM-dd HH:mm:ss"
     *
     * @param dateExpression The date expression to be converted.
     * @return The schedule expression.
     */
    private ScheduleExpression createDateTimeSchedule(String dateExpression) {
        // Parse the date and time.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateExpression, formatter);

        // Return the schedule expression.
        return new ScheduleExpression()
            .second(dateTime.getSecond())
            .minute(dateTime.getMinute())
            .hour(dateTime.getHour())
            .dayOfMonth(dateTime.getDayOfMonth())
            .month(dateTime.getMonthValue())
            .year(dateTime.getYear());
    }

    private ScheduleExpression createSchedule(Notification notification) throws InvalidNotificationException {
        // Initialize the schedule expression.
        ScheduleExpression scheduleExpression;

        switch (notification.getTriggerType()) {
            // Triggered by a timer or the weather.
            case CRON:
            case WEATHER:
                scheduleExpression = createCronSchedule(notification.getTriggerParameter());
                break;
            // Triggered by a date.
            case DATE:
                scheduleExpression = createDateSchedule(notification.getTriggerParameter());
                break;
            // Triggered by a time.
            case TIME:
                scheduleExpression = createTimeSchedule(notification.getTriggerParameter());
                break;
            // Triggered by a date and time.
            case DATETIME:
                scheduleExpression = createDateTimeSchedule(notification.getTriggerParameter());
                break;
            // Invalid trigger.
            default:
                throw new InvalidNotificationException(
                    "The trigger type of the notification with id = " + notification.getId() + " is invalid."
                );
        }

        return scheduleExpression;
    }

    /**
     * Schedules a notification.
     *
     * @param notification The notification to be scheduled.
     * @return The timer handle of the notification.
     * @throws InvalidNotificationException if the notification trigger is invalid.
     */
    public TimerHandle scheduleNotification(Notification notification) throws InvalidNotificationException {
        // Create the schedule expression.
        ScheduleExpression scheduleExpression = createSchedule(notification);

        // Create the timer configuration.
        TimerConfig timerConfig = new TimerConfig();
        timerConfig.setInfo(notification.getId());

        // Create the timer.
        Timer timer = timerService.createCalendarTimer(scheduleExpression, timerConfig);
        return timer.getHandle();
    }

    /**
     * Triggers a notification.
     *
     * @param timer The triggered timer.
     */
    @Timeout
    public void triggerNotification(Timer timer) {
        // Get the identifier.
        int identifier = (int) timer.getInfo();

        // Process the notification.
        notificationProcessor.processNotification(identifier);
    }
}
