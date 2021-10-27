package fr.unice.polytech.isa.common.entities.statistics;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="DAILY_BUYING_STATISTICS")
@DiscriminatorValue(value = "B") // B for Buying
@PrimaryKeyJoinColumn(name="DAILY_ID")
public class DailyBuyingStatistics extends DailyStatistics {

    /**
     * The associated list of hourly buying statistics.
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HourlyBuyingStatistics> hourlyBuyingStatistics;

    /**
     * Default constructor.
     */
    public DailyBuyingStatistics(){}

    /**
     * Constructor with date initialization
     * @param date the date of the DailyStatistics
     */
    public DailyBuyingStatistics(LocalDate date){
        super(date);
        this.hourlyBuyingStatistics = new ArrayList<>();
    }

    /**
     * @return the list of hourly statistics
     */
    public List<HourlyBuyingStatistics> getHourlyBuyingStatistics() {
        return hourlyBuyingStatistics;
    }

    /**
     * Sets the list of hourly statistics of the day
     * @param hourlyBuyingStatistics the statistics to set
     */
    public void setHourlyBuyingStatistics(List<HourlyBuyingStatistics> hourlyBuyingStatistics) {
        this.hourlyBuyingStatistics = hourlyBuyingStatistics;
    }

    /**
     * Adds an hourlyBuyingStatistics to the list of hourlyBuyingStatistics
     * @param hourly the statistics to add
     */
    public void addHourlyBuyingStatistic(HourlyBuyingStatistics hourly){
        hourlyBuyingStatistics.add(hourly);
    }

    /**
     * Removes an hourlyBuyingStatistics from the list of hourlyBuyingStatistics
     * @param hourlyStatistics the statistics to remove
     */
    public void removeHourlyBuyingStatistic(HourlyBuyingStatistics hourlyStatistics){
        hourlyBuyingStatistics.remove(hourlyStatistics);
    }
}
