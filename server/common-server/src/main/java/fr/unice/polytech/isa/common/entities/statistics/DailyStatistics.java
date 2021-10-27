package fr.unice.polytech.isa.common.entities.statistics;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name="DAILY_STATISTICS")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="DAILY_STATISTICS_TYPE",
        discriminatorType = DiscriminatorType.STRING, length = 1)
public abstract class DailyStatistics {

    /**
     * The daily statistics identifier.
     */
    @Id
    @Column(name="DAILY_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    /**
     * The date of the statistics
     */
    @Transient
    private LocalDate date;

    /**
     * For the SQL queries: raw date
     */
    @NotNull
    private String dateRaw;

    /**
     * Default constructor.
     */
    public DailyStatistics() {}

    /**
     * Constructor with date initialization
     * @param date the date of the DailyStatistics
     */
    public DailyStatistics(LocalDate date){
        this.date = date;
        this.dateRaw = date.toString();
    }

    /**
     * Decodes the date from the persistence unit.
     */
    @PostLoad
    public void init() {
        this.date = this.dateRaw == null? null : LocalDate.parse(dateRaw);
    }

    /**
     * Sets the dailyStatistics identifier.
     *
     * @param id The dailyStatistics identifier.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the dailyStatistics identifier.
     *
     * @return The dailyStatistics identifier.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the dailyStatistics date.
     *
     * @return The dailyStatistics date.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the dailyStatistics date.
     *
     * @param date The dailyStatistics date.
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Returns the dailyStatistics raw date.
     *
     * @return The dailyStatistics raw date.
     */
    public String getDateRaw() {
        return dateRaw;
    }

    /**
     * Sets the dailyStatistics raw date.
     *
     * @param date The dailyStatistics raw date.
     */
    public void setDateRaw(String date) {
        this.dateRaw = date;
    }
}
