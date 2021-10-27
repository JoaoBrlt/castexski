package fr.unice.polytech.isa.common.entities.notifications;

import javax.ejb.TimerHandle;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Notification.
 * <p>
 * Represents a notification to be sent to the customers.
 * </p>
 */
@Entity
public class Notification implements Serializable {
    /**
     * The notification identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    /**
     * The communication channel of the notification.
     *
     * Available communication channels:
     * - ALL        : Send by all channels.
     * - EMAIL      : Send by email.
     * - SMS        : Send by text message.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    private NotificationChannel channel;

    /**
     * The trigger of the notification.
     *
     * Available triggers:
     * - CRON       : Triggered by a cron.
     * - DATE       : Triggered by a date.
     * - TIME       : Triggered by a time.
     * - DATETIME   : Triggered by a date and time.
     * - WEATHER    : Triggered by the weather.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    private NotificationTrigger triggerType;

    /**
     * The trigger parameter of the notification.
     *
     * Depends on the trigger:
     * - CR0N       : Cron expression ("s m h d M e").
     * - DATE       : Date ("yyyy-MM-dd").
     * - TIME       : Time ("HH:mm:ss").
     * - DATETIME   : Date and time ("yyyy-MM-dd HH:mm:ss").
     * - WEATHER    : Cron expression ("s m h d M e") to poll the weather service.
     */
    @NotNull
    private String triggerParameter;

    /**
     * The trigger cool down of the notification.
     *
     * Depends on the trigger:
     * - CRON       : Not used.
     * - DATE       : Not used.
     * - TIME       : Not used.
     * - DATETIME   : Not used.
     * - WEATHER    : Duration in the ISO 8601 format ("PT1D2M3S").
     */
    private String triggerCoolDown;

    /**
     * The weather trigger parameter of the notification.
     *
     * Depends on the trigger:
     * - CRON       : Not used.
     * - DATE       : Not used.
     * - TIME       : Not used.
     * - DATETIME   : Not used.
     * - WEATHER    : The city to be used to poll the weather service.
     */
    private String weatherParameter;

    /**
     * The target audience of the notification.
     *
     * Available targets:
     * - ALL        : Send to all the users.
     * - MERCHANTS  : Send to the merchants.
     * - CUSTOMERS  : Send to the customers.
     * - PREMIUM    : Send to the premium customers.
     * - EMAILS     : Send to specific emails.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    private NotificationTarget target;

    /**
     * The target parameter of the notification.
     *
     * Depends on the target:
     * - ALL        : Not used.
     * - MERCHANTS  : Not used.
     * - CUSTOMERS  : Not used.
     * - PREMIUM    : Not used.
     * - EMAILS     : Comma separated list of emails.
     */
    private String targetParameter;

    /**
     * The associated resort.
     */
    private String resortName;

    /**
     * The subject of the notification.
     *
     * Depends on the communication channel:
     * - ALL: Used for emails only.
     * - SMS: Not used.
     * - EMAIL: Used.
     */
    private String subject;

    /**
     * The message of the notification.
     *
     * May contain tags:
     * - {{stats type=presence}}: The presence statistics of the associated resort.
     * - {{stats type=buying}}: The buying statistics of the associated resort.
     */
    @NotNull
    private String message;

    /**
     * The timer handle of the notification.
     */
    private TimerHandle timerHandle;

    /**
     * The last time the notification was triggered.
     */
    private LocalDateTime lastTrigger;

    /**
     * Default constructor.
     */
    public Notification() {}

    /**
     * Constructs a notification.
     *
     * @param channel The communication channel of the notification.
     * @param triggerType The trigger of the notification.
     * @param triggerParameter The target parameter of the notification.
     * @param triggerCoolDown The trigger cool down of the notification.
     * @param weatherParameter The weather trigger parameter of the notification.
     * @param target The target of the notification.
     * @param targetParameter The target parameter of the notification.
     * @param resortName The associated resort.
     * @param message The message of the notification.
     */
    public Notification(
        NotificationChannel channel,
        NotificationTrigger triggerType,
        String triggerParameter,
        String triggerCoolDown,
        String weatherParameter,
        NotificationTarget target,
        String targetParameter,
        String resortName,
        String subject,
        String message
    ) {
        // Channel.
        this.channel = channel;

        // Trigger.
        this.triggerType = triggerType;
        this.triggerParameter = triggerParameter;
        this.triggerCoolDown = triggerCoolDown;
        this.weatherParameter = weatherParameter;

        // Target.
        this.target = target;
        this.targetParameter = targetParameter;

        // Resort.
        this.resortName = resortName;

        // Message.
        this.subject = subject;
        this.message = message;

        // Timer.
        this.timerHandle = null;
        this.lastTrigger = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public NotificationChannel getChannel() {
        return channel;
    }

    public void setChannel(NotificationChannel channel) {
        this.channel = channel;
    }

    public NotificationTrigger getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(NotificationTrigger triggerType) {
        this.triggerType = triggerType;
    }

    public String getTriggerParameter() {
        return triggerParameter;
    }

    public void setTriggerParameter(String triggerParameter) {
        this.triggerParameter = triggerParameter;
    }

    public String getTriggerCoolDown() {
        return triggerCoolDown;
    }

    public void setTriggerCoolDown(String triggerCoolDown) {
        this.triggerCoolDown = triggerCoolDown;
    }

    public String getWeatherParameter() {
        return weatherParameter;
    }

    public void setWeatherParameter(String weatherParameter) {
        this.weatherParameter = weatherParameter;
    }

    public NotificationTarget getTarget() {
        return target;
    }

    public void setTarget(NotificationTarget target) {
        this.target = target;
    }

    public String getTargetParameter() {
        return targetParameter;
    }

    public void setTargetParameter(String targetParameter) {
        this.targetParameter = targetParameter;
    }

    public String getResortName() {
        return resortName;
    }

    public void setResortName(String resortName) {
        this.resortName = resortName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TimerHandle getTimerHandle() {
        return timerHandle;
    }

    public void setTimerHandle(TimerHandle timerHandle) {
        this.timerHandle = timerHandle;
    }

    public LocalDateTime getLastTrigger() {
        return lastTrigger;
    }

    public void setLastTrigger(LocalDateTime lastTrigger) {
        this.lastTrigger = lastTrigger;
    }

    /**
     * Indicates whether another object is equal to this notification.
     *
     * @param object The object to be compared.
     * @return <code>true</code> if this notification is the same as the object argument; <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object) {
        // Same instance.
        if (this == object) {
            return true;
        }

        // Not same class.
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        // Same attributes.
        Notification notification = (Notification) object;
        return (
            getChannel() == notification.getChannel() &&
            getTriggerType() == notification.getTriggerType() &&
            Objects.equals(getTriggerParameter(), notification.getTriggerParameter()) &&
            Objects.equals(getTriggerCoolDown(), notification.getTriggerCoolDown()) &&
            Objects.equals(getWeatherParameter(), notification.getWeatherParameter()) &&
            getTarget() == notification.getTarget() &&
            Objects.equals(getTargetParameter(), notification.getTargetParameter()) &&
            Objects.equals(getResortName(), notification.getResortName()) &&
            Objects.equals(getSubject(), notification.getSubject()) &&
            Objects.equals(getMessage(), notification.getMessage())
        );
    }

    /**
     * Returns a hash code value for the notification.
     *
     * @return A hash code value for the notification.
     */
    @Override
    public int hashCode() {
        return Objects.hash(
            getChannel(),
            getTriggerType(),
            getTriggerParameter(),
            getTriggerCoolDown(),
            getTarget(),
            getTargetParameter(),
            getResortName(),
            getSubject(),
            getMessage()
        );
    }

    /**
     * Returns a string representation of the notification.
     *
     * @return A string representation of the notification.
     */
    @Override
    public String toString() {
        return "Notification{" +
            "id=" + id +
            ", channel=" + channel +
            ", triggerType=" + triggerType +
            ", triggerParameter='" + triggerParameter + '\'' +
            ", triggerCoolDown='" + triggerCoolDown + '\'' +
            ", target=" + target +
            ", targetParameter='" + targetParameter + '\'' +
            ", resortName=" + resortName +
            ", subject='" + subject + '\'' +
            ", message='" + message + '\'' +
            '}';
    }
}
