package fr.unice.polytech.isa.common.entities.resort;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Entity
@DiscriminatorValue(value = "double_ski_lift")
@Access(AccessType.FIELD)
public class DoubleSkiLift extends SkiLift implements Serializable {
    static final int MAX_LENGTH_AVG = 10;

    /**
     * The status of the current attendance of the ski lift
     */
    @Transient
    AlertLevel status;

    /**
     * The average limit between the two badge terminals
     */
    @Transient
    Duration averageLimit;

    /**
     * The average limit of time between the two badge terminals (raw type for JPA)
     */
    String averageLimitRaw;


    /**
     * The raw (needed for JPA) map of cards swiped a first time which are expected to be swiped a second time <key= physicalCardId, value=duration></key=>
     */
    @ElementCollection
    Map<String, String> waitingForSecondSwipeRaw = new HashMap<>();

    /**
     * The map of cards swiped a first time which are expected to be swiped a second time <key= physicalCardId, value=duration></key=>
     */
    @Transient
    Map<String, LocalDateTime> waitingForSecondSwipe = new HashMap<>();

    /**
     * The raw (needed for JPA) collection of the previous duration between two swipe
     */

    @ElementCollection
    List<String> previousDurationRaw = new LinkedList<>();

    /**
     * The collection of the previous duration between two swipe
     */
    @Transient
    List<Duration> previousDuration = new LinkedList<>();

    /**
     * Decodes the maximum duration from the persistence unit.
     */
    @PostLoad
    public void init() {
        averageLimit = averageLimitRaw == null ? null : Duration.parse(averageLimitRaw);
        previousDurationRaw.forEach(duration -> previousDuration.add(Duration.parse(duration)));
        waitingForSecondSwipeRaw.forEach((k, v) -> waitingForSecondSwipe.put(k, LocalDateTime.parse(v)));
    }

    /**
     * Default constructor
     */
    DoubleSkiLift() {}

    /**
     * Constructs a ski lift with double terminal.
     *
     * @param limit The average limit of time between two badge terminals
     */

    public DoubleSkiLift(Resort resort, Duration limit, boolean isOpen, String name) {
        super(isOpen, resort, name);
        this.averageLimit = limit;
        this.averageLimitRaw = limit.toString();
    }

    @Enumerated
    @Access(AccessType.PROPERTY)
    public AlertLevel getStatus() {
        if (averageLimit.minus(getAverageWait()).isNegative()){
            return AlertLevel.BUSY;
        }
        return AlertLevel.NORMAL;
    }

    @Transient
    public Duration getAverageWait() {
        return previousDuration.size() > 0 ?
            previousDuration.stream().reduce(Duration.ZERO, Duration::plus).dividedBy(previousDuration.size()) : Duration.ZERO;
    }

    public void setStatus(AlertLevel status) {
        this.status = status;
    }

    public Duration getAverageLimit() {
        return averageLimit;
    }

    public void setAverageLimit(Duration averageLimit) {
        this.averageLimit = averageLimit;
        this.averageLimitRaw = averageLimit.toString();
    }

    public void setAverageLimitRaw(String averageLimitRaw) {
        this.averageLimitRaw = averageLimitRaw;
        this.averageLimit = Duration.parse(averageLimitRaw);
    }

    public String getAverageLimitRaw() {
        return averageLimitRaw;
    }

    public Map<String, LocalDateTime> getWaitingForSecondSwipe() {
        return waitingForSecondSwipe;
    }

    public void setWaitingForSecondSwipe(Map<String, LocalDateTime> waitingForSecondSwipe) {
        this.waitingForSecondSwipe = waitingForSecondSwipe;
    }

    public Map<String, String> getWaitingForSecondSwipeRaw() {
        return waitingForSecondSwipeRaw;
    }

    public void setWaitingForSecondSwipeRaw(Map<String, String> waitingForSecondSwipeRaw) {
        this.waitingForSecondSwipeRaw = waitingForSecondSwipeRaw;
    }

    public boolean swipeTerminal(String card, LocalDateTime time) {
        if (waitingForSecondSwipe.containsKey(card)) {
            if (previousDuration.size() >= MAX_LENGTH_AVG){
                previousDurationRaw.remove(previousDuration.remove(0).toString());
            }
            Duration elapsedTime = Duration.between(waitingForSecondSwipe.remove(card),time);
            previousDuration.add(elapsedTime);
            previousDurationRaw.add(elapsedTime.toString());

            waitingForSecondSwipeRaw.remove(card);
            waitingForSecondSwipe.remove(card);
            return false;
        } else {
            waitingForSecondSwipe.put(card, time);
            waitingForSecondSwipeRaw.put(card, time.toString());
            return true;
        }
    }

    public void resetWaiting() {
        previousDuration.clear();
        previousDurationRaw.clear();
    }
}
