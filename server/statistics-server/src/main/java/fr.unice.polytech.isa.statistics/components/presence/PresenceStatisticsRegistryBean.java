package fr.unice.polytech.isa.statistics.components.presence;

import fr.unice.polytech.isa.common.entities.statistics.DailyPresenceStatistics;
import fr.unice.polytech.isa.common.entities.statistics.HourlyPresenceStatistics;
import fr.unice.polytech.isa.common.entities.resort.SkiLift;
import fr.unice.polytech.isa.statistics.exceptions.NoDailyStatisticsException;
import fr.unice.polytech.isa.statistics.exceptions.NoHourlyStatisticsException;
import fr.unice.polytech.isa.statistics.interfaces.presence.PresenceStatisticsFinder;
import fr.unice.polytech.isa.statistics.interfaces.presence.PresenceStatisticsRegistration;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.Logger;

@Stateless
public class PresenceStatisticsRegistryBean implements PresenceStatisticsRegistration {
    private static final Logger log = Logger.getLogger(Logger.class.getName());

    @EJB private PresenceStatisticsFinder presenceStatisticsFinder;

    @PersistenceContext
    private EntityManager manager;

    @Override
    public void addNewDailyPresenceStatistics(SkiLift skiLift, LocalDate localDate) {
        SkiLift s = manager.merge(skiLift);
        try {
            presenceStatisticsFinder.findPresenceStatisticsOfSkiLiftByDate(localDate,s);
        } catch (NoDailyStatisticsException e) {
            DailyPresenceStatistics dailyStatistics = new DailyPresenceStatistics(localDate, skiLift.getResort().getName(), skiLift.getName());
            s.addDailyStatistic(dailyStatistics);
            manager.persist(dailyStatistics);
            manager.persist(s);
        }
    }

    @Override
    public void addCurrentDailyPresenceStatistics(SkiLift skiLift) {
        LocalDate date = LocalDate.now();
        addNewDailyPresenceStatistics(skiLift,date);
    }

    @Override
    public void addNewHourlyPresenceStatistics(SkiLift skiLift, LocalDateTime localDateTime) throws NoDailyStatisticsException {
        SkiLift s = manager.merge(skiLift);
        LocalDate date = LocalDate.of(localDateTime.getYear(),localDateTime.getMonthValue(),localDateTime.getDayOfMonth());
        DailyPresenceStatistics dailyStatistics = presenceStatisticsFinder.findPresenceStatisticsOfSkiLiftByDate(date,s);
        try {
            presenceStatisticsFinder.findPresenceStatisticsByHour(dailyStatistics, localDateTime.getHour());
        } catch (NoHourlyStatisticsException e){
            HourlyPresenceStatistics hourlyStatistics = new HourlyPresenceStatistics(localDateTime.getHour());
            dailyStatistics.addHourlyStatistics(hourlyStatistics);
            manager.persist(hourlyStatistics);
            manager.persist(dailyStatistics);
            manager.persist(s);
        }
    }

    @Override
    public void addCurrentHourlyPresenceStatistics(SkiLift skiLift) throws NoDailyStatisticsException {
        LocalDateTime date = LocalDateTime.now();
        addNewHourlyPresenceStatistics(skiLift,date);
    }
}
