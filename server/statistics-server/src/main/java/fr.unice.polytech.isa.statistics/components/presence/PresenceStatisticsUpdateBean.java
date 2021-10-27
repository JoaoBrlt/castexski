package fr.unice.polytech.isa.statistics.components.presence;

import fr.unice.polytech.isa.common.entities.statistics.DailyPresenceStatistics;
import fr.unice.polytech.isa.common.entities.statistics.HourlyPresenceStatistics;
import fr.unice.polytech.isa.common.entities.resort.SkiLift;
import fr.unice.polytech.isa.statistics.exceptions.NoDailyStatisticsException;
import fr.unice.polytech.isa.statistics.exceptions.NoHourlyStatisticsException;
import fr.unice.polytech.isa.statistics.interfaces.presence.PresenceStatisticsFinder;
import fr.unice.polytech.isa.statistics.interfaces.presence.PresenceStatisticsRegistration;
import fr.unice.polytech.isa.statistics.interfaces.presence.PresenceStatisticsUpdater;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.Logger;

@Stateless
public class PresenceStatisticsUpdateBean implements PresenceStatisticsUpdater {
    private static final Logger log = Logger.getLogger(Logger.class.getName());

    @EJB private PresenceStatisticsFinder presenceStatisticsFinder;
    @EJB private PresenceStatisticsRegistration presenceStatisticsRegistration;

    @PersistenceContext
    private EntityManager manager;

    @Override
    public void addCardToStatistics(SkiLift skiLift, String cardId) throws NoDailyStatisticsException {
        LocalDateTime dateTime = LocalDateTime.now();
        addCardToStatisticsWithDate(skiLift,cardId,dateTime);
    }

    @Override
    public void addCardToStatisticsWithDate(SkiLift skiLift, String cardId, LocalDateTime dateTime) throws NoDailyStatisticsException {
        SkiLift s = manager.merge(skiLift);
        LocalDate date = LocalDate.of(dateTime.getYear(),dateTime.getMonthValue(),dateTime.getDayOfMonth());
        presenceStatisticsRegistration.addNewDailyPresenceStatistics(s, date);
        presenceStatisticsRegistration.addNewHourlyPresenceStatistics(s,dateTime);
        DailyPresenceStatistics dailyStatistics = presenceStatisticsFinder.findPresenceStatisticsOfSkiLiftByDate(date,skiLift);
        int hour = dateTime.getHour();
        HourlyPresenceStatistics hourlyStatistics = new HourlyPresenceStatistics(hour);
        try {
            hourlyStatistics = presenceStatisticsFinder.findPresenceStatisticsByHour(dailyStatistics,hour);
            hourlyStatistics.addCardToBeepedCards(cardId);
        } catch (NoHourlyStatisticsException e) {
            hourlyStatistics.addCardToBeepedCards(cardId);
            dailyStatistics.addHourlyStatistics(hourlyStatistics);
        }
        manager.persist(hourlyStatistics);
        manager.persist(dailyStatistics);
        manager.persist(s);
    }
}
