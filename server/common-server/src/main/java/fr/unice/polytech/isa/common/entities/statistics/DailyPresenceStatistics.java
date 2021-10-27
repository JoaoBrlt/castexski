package fr.unice.polytech.isa.common.entities.statistics;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="DAILY_PRESENCE_STATISTICS")
@DiscriminatorValue(value = "P") // P for Presence
@PrimaryKeyJoinColumn(name="DAILY_ID")
public class DailyPresenceStatistics extends DailyStatistics {

    /**
     * The associated list of hourly presence statistics.
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HourlyPresenceStatistics> hourlyPresenceStatistics;

    /**
     * The associated resort
     */
    private String resortName;

    /**
     * The associated ski lift
     */
    private String skiLiftName;

    /**
     * Default constructor.
     */
    public DailyPresenceStatistics(){}

    /**
     * Constructor with date initialization
     * @param date the date of the DailyStatistics
     */
    public DailyPresenceStatistics(LocalDate date, String resortName, String skiLiftName){
        super(date);
        this.hourlyPresenceStatistics = new ArrayList<>();
        this.resortName = resortName;
        this.skiLiftName = skiLiftName;
    }

    /**
     * @return the list of hourly statistics
     */
    public List<HourlyPresenceStatistics> getHourlyPresenceStatistics() {
        return hourlyPresenceStatistics;
    }

    /**
     * Sets the list of hourly statistics of the day
     * @param hourlyPresenceStatistics the statistics to set
     */
    public void setHourlyPresenceStatistics(List<HourlyPresenceStatistics> hourlyPresenceStatistics) {
        this.hourlyPresenceStatistics = hourlyPresenceStatistics;
    }

    /**
     * Adds an hourlyPresenceStatistics to the list of hourlyPresenceStatistics
     * @param hourlyStatistics the statistics to add
     */
    public void addHourlyStatistics(HourlyPresenceStatistics hourlyStatistics){
        this.hourlyPresenceStatistics.add(hourlyStatistics);
    }

    /**
     * Removes an hourlyPresenceStatistics from the list of hourlyPresenceStatistics
     * @param hourlyStatistics the statistics to remove
     */
    public void removeHourlyStatistics(HourlyPresenceStatistics hourlyStatistics){
        this.hourlyPresenceStatistics.remove(hourlyStatistics);
    }

    /**
     * @return the name of the resort associated to the stats
     */
    public String getResortName(){
        return this.resortName;
    }

    /**
     * Sets the name of the resort associated to the stats
     * @param resortName the name of the resort
     */
    public void setResortName(String resortName) {
        this.resortName = resortName;
    }

    /**
     * @return the name of the ski lift associated to the stats
     */
    public String getSkiLiftName() {
        return skiLiftName;
    }

    /**
     * Sets the name of the ski lift associated to the stats
     * @param skiLiftName the name of the ski lift
     */
    public void setSkiLiftName(String skiLiftName) {
        this.skiLiftName = skiLiftName;
    }

    public int getNumberOfPeople(){
        List<String> cardIds = new ArrayList<>();
        for(HourlyPresenceStatistics hourly : hourlyPresenceStatistics){
            Set<String> hourlyCards = hourly.getBeepedCards().keySet();
            for(String s : hourlyCards){
                if(!cardIds.contains(s)){
                    cardIds.add(s);
                }
            }
        }
        return cardIds.size();
    }
}
