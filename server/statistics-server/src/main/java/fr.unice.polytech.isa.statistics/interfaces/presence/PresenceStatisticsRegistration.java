package fr.unice.polytech.isa.statistics.interfaces.presence;

import fr.unice.polytech.isa.common.entities.resort.SkiLift;
import fr.unice.polytech.isa.statistics.exceptions.NoDailyStatisticsException;

import javax.ejb.Local;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Local
public interface PresenceStatisticsRegistration {
    void addNewDailyPresenceStatistics(SkiLift skiLift, LocalDate localDate);
    void addCurrentDailyPresenceStatistics(SkiLift skiLift);
    void addNewHourlyPresenceStatistics(SkiLift skiLift, LocalDateTime localDateTime)
            throws NoDailyStatisticsException;
    void addCurrentHourlyPresenceStatistics(SkiLift skiLift)
            throws NoDailyStatisticsException;
}
