package fr.unice.polytech.isa.statistics.interfaces.presence;

import fr.unice.polytech.isa.common.entities.resort.SkiLift;
import fr.unice.polytech.isa.statistics.exceptions.NoDailyStatisticsException;

import javax.ejb.Local;
import java.time.LocalDateTime;

@Local
public interface PresenceStatisticsUpdater {
    void addCardToStatistics(SkiLift skiLift, String cardId)
            throws NoDailyStatisticsException;
    void addCardToStatisticsWithDate(SkiLift skiLift, String cardId, LocalDateTime dateTime)
            throws NoDailyStatisticsException;
}
