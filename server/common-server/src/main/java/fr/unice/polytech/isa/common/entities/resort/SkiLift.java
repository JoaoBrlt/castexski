package fr.unice.polytech.isa.common.entities.resort;

import fr.unice.polytech.isa.common.entities.statistics.DailyPresenceStatistics;
import fr.unice.polytech.isa.common.entities.statistics.DailyStatistics;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Ski Lift
 * <p>
 * Represents a ski lift of the resort.
 * </p>
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TypeOfSkiLift")
public class SkiLift implements Serializable {
    /**
     * The ski lift identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    /**
     * The ski lift name.
     */
    private String name;

    /**
     * Whether the ski lift is open.
     */
    private boolean isOpen;

    /**
     * This lift's resort
     */
    @ManyToOne
    private Resort resort;


    //TODO Location location
    /**
     * The associated daily statistics
     */

    @NotNull
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DailyPresenceStatistics> dailyStatistics = new ArrayList<>();

    /**
     * Default constructor
     */
    public SkiLift() {}

    /**
     * Constructs a ski lift.
     *
     * @param isOpen Whether the ski lift is open.
     * @param resort The resort to which ski lift belongs
     */
    public SkiLift(boolean isOpen, Resort resort) {
        this.isOpen = isOpen;
        this.resort = resort;
        this.dailyStatistics = new ArrayList<>();
    }

    public SkiLift(boolean isOpen, Resort resort, String name) {
        this(isOpen, resort);
        this.name = name;
    }


    /**
     * Returns the ski lift identifier.
     *
     * @return The ski lift identifier.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the ski lift identifier.
     *
     * @param id The new ski lift identifier.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the ski lift name.
     *
     * @return The ski lift name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the ski lift name.
     *
     * @param name The new ski lift name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Indicates whether the ski lift is open.
     *
     * @return <code>true</code> if the ski lift is open; <code>false</code> otherwise.
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * Sets the ski lift open state.
     *
     * @param isOpen Whether the ski lift is open.
     */
    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    /**
     * The list of all the daily statistics associated
     *
     * @return the list of all the daily statistics associated
     */
    public List<DailyPresenceStatistics> getDailyStatistics() {
        return dailyStatistics;
    }

    /**
     * Sets the list of daily statistics
     *
     * @param dailyStatistics the list of daily statistics
     */
    public void setDailyStatistics(List<DailyPresenceStatistics> dailyStatistics) {
        this.dailyStatistics = dailyStatistics;
    }

    /**
     * Adds a new daily statistic to the list
     *
     * @param dailyStatistics the daily statistic to add
     */
    public void addDailyStatistic(DailyPresenceStatistics dailyStatistics){
        dailyStatistics.setSkiLiftName(this.name);
        dailyStatistics.setResortName(this.resort.getName());
        this.dailyStatistics.add(dailyStatistics);
    }

    /**
     * Remove a given daily statistic from the list
     *
     * @param dailyStatistics the daily statistic to remove
     */
    public void removeDailyStatistic(DailyStatistics dailyStatistics){
        this.dailyStatistics.remove(dailyStatistics);
    }

    /**
     * Returns this left's resort
     * @return this left's resort
     */
    public Resort getResort() {
        return resort;
    }

    /**
     * Sets this lift's resort
     * @param resort this lift's resort
     */
    public void setResort(Resort resort) {
        this.resort = resort;
    }


    /**
     * Indicates whether the ski lift is equipped by a double badge terminal.
     * @return true if the ski lift is has a double badge terminal, false if it has only one.
     */
    public boolean isHasDoubleBadgeTerminal() {
        return this instanceof DoubleSkiLift;
    }

    /**
     * Indicates whether another object is equal to this ski lift.
     *
     * @param object The object to be compared.
     * @return <code>true</code> if this ski lift is the same as the object argument; <code>false</code> otherwise.
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

        // Same open state + same name
        SkiLift skiLift = (SkiLift) object;
        return isOpen() == skiLift.isOpen()
            && Objects.equals(getName(), skiLift.getName())
            && isHasDoubleBadgeTerminal() == skiLift.isHasDoubleBadgeTerminal();
    }

    /**
     * Returns the hash code value for the ski lift.
     *
     * @return The hash code value for the ski lift.
     */
    @Override
    public int hashCode() {
        return Objects.hash(isOpen(), getName(), isHasDoubleBadgeTerminal());
    }

    /**
     * Returns a string representation of the ski lift.
     *
     * @return A string representation of the ski lift.
     */
    @Override
    public String toString() {
        return "SkiLift{" +
            "id='" + getId() + '\'' +
            ", name='" + getName() + '\'' +
            ", isOpen=" + isOpen() +
            ", resort=" + getResort() +
            ", dailyStatistics=" + getDailyStatistics() +
            ", isDoubleBadgeReader=" + isHasDoubleBadgeTerminal() +
            '}';
    }
}
