package fr.unice.polytech.isa.common.entities.statistics;

import javax.persistence.*;
import javax.persistence.InheritanceType;

@Entity
@Table(name="HOURLY_STATISTICS")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="HOURLY_STATISTICS_TYPE",
        discriminatorType = DiscriminatorType.STRING, length = 1)
public abstract class HourlyStatistics {
    /**
     * The hourly statistics identifier.
     */
    @Id
    @Column(name="HOURLY_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    /**
     * The hourly Statistics' starting hour
     */
    private int startingHour;

    /**
     * Default constructor.
     */
    public HourlyStatistics() {}

    /**
     * The HourlyStatistic constructor with a starting hour of the day
     * @param startingHour the hour the statistics starts
     */
    public HourlyStatistics(int startingHour){
        this.startingHour = startingHour;
    }

    /**
     * Returns the hourly statistics identifier.
     *
     * @return The hourly statistics identifier.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the hourlyStatistics identifier.
     *
     * @param id The hourlyStatistics identifier.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the hourly statistics' starting hour.
     *
     * @return The hourly statistics' starting hour.
     */
    public int getStartingHour() {
        return startingHour;
    }

    /**
     * Sets the hourlyStatistics' starting hour.
     *
     * @param startingHour the starting hour.
     */
    public void setStartingHour(int startingHour) {
        this.startingHour = startingHour;
    }

}
