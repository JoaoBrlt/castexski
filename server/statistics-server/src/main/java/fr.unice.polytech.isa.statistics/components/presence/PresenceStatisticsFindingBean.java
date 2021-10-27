package fr.unice.polytech.isa.statistics.components.presence;

import fr.unice.polytech.isa.common.entities.resort.SkiLift;
import fr.unice.polytech.isa.common.entities.statistics.DailyPresenceStatistics;
import fr.unice.polytech.isa.common.entities.statistics.HourlyPresenceStatistics;
import fr.unice.polytech.isa.statistics.exceptions.NoDailyStatisticsException;
import fr.unice.polytech.isa.statistics.exceptions.NoHourlyStatisticsException;
import fr.unice.polytech.isa.statistics.interfaces.presence.PresenceStatisticsFinder;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class PresenceStatisticsFindingBean implements PresenceStatisticsFinder {
    private static final Logger log = Logger.getLogger(Logger.class.getName());

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<DailyPresenceStatistics> findDailyStatisticsBySkiLiftAndResortName(String skiLiftName, String resortName) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<DailyPresenceStatistics> criteria = builder.createQuery(DailyPresenceStatistics.class);
        Root<DailyPresenceStatistics> root =  criteria.from(DailyPresenceStatistics.class);
        criteria.select(root).where(
                builder.equal(root.get("skiLiftName"), skiLiftName),
                builder.equal(root.get("resortName"), resortName));
        TypedQuery<DailyPresenceStatistics> query = manager.createQuery(criteria);
        try {
            return query.getResultList();
        } catch (NoResultException nre){
            log.log(Level.FINEST, "No result for ["+skiLiftName+"]", nre);
            return new ArrayList<>();
        }
    }

    @Override
    public DailyPresenceStatistics findPresenceStatisticsBySkiLiftAndResortNameAndDate(String skiLiftName, String resortName, LocalDate date) throws NoDailyStatisticsException {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<DailyPresenceStatistics> criteria = builder.createQuery(DailyPresenceStatistics.class);
        Root<DailyPresenceStatistics> root =  criteria.from(DailyPresenceStatistics.class);
        criteria.select(root).where(
                builder.equal(root.get("skiLiftName"), skiLiftName),
                builder.equal(root.get("resortName"), resortName),
                builder.equal(root.get("dateRaw"), date.toString()));
        TypedQuery<DailyPresenceStatistics> query = manager.createQuery(criteria);
        try {
            return query.getSingleResult();
        } catch (NoResultException nre){
            log.log(Level.FINEST, "No result for ["+skiLiftName+"]", nre);
            throw new NoDailyStatisticsException(date);
        }
    }

    @Override
    public List<DailyPresenceStatistics> findPresenceStatisticsInAWeek(String resortName, String skiLiftName, int weekNumber) {
        List<DailyPresenceStatistics> daily = findDailyStatisticsBySkiLiftAndResortName(skiLiftName,resortName);
        List<DailyPresenceStatistics> toReturn = new ArrayList<>();
        for(DailyPresenceStatistics d : daily){
            int currentYear = LocalDate.now().getYear();
            LocalDate date = d.getDate();
            TemporalField temp = WeekFields.of(Locale.FRANCE).weekOfWeekBasedYear();
            int dateWeekNumber = date.get(temp);
            if(dateWeekNumber == weekNumber && date.getYear() == currentYear) {
                toReturn.add(d);
            }
        }
        return toReturn;
    }

    @Override
    public List<DailyPresenceStatistics> findPresenceStatisticsInAMonth(String resortName, String skiLiftName, int monthNumber) {
        List<DailyPresenceStatistics> dailyPresenceStatistics = findDailyStatisticsBySkiLiftAndResortName(skiLiftName,resortName);
        List<DailyPresenceStatistics> toReturn = new ArrayList<>();
        for(DailyPresenceStatistics daily : dailyPresenceStatistics) {
            int currentYear = LocalDate.now().getYear();
            LocalDate date = daily.getDate();
            int month = date.getMonthValue();
            if (month == monthNumber && date.getYear() == currentYear) {
                toReturn.add(daily);
            }
        }
        return toReturn;
    }

    @Override
    public List<DailyPresenceStatistics> findPresenceStatisticsInAYear(String resortName, String skiLiftName, int yearNumber) {
        List<DailyPresenceStatistics> dailyPresenceStatistics = findDailyStatisticsBySkiLiftAndResortName(skiLiftName,resortName);
        List<DailyPresenceStatistics> toReturn = new ArrayList<>();
        for(DailyPresenceStatistics daily : dailyPresenceStatistics) {
            LocalDate date = daily.getDate();
            int year = date.getYear();
            if (year == yearNumber) {
                toReturn.add(daily);
            }
        }
        return toReturn;
    }

    @Override
    public List<DailyPresenceStatistics> findSkiLiftDailyStatistics(SkiLift skiLift) {
        SkiLift s = manager.merge(skiLift);
        if(s.getDailyStatistics()==null){
            s.setDailyStatistics(new ArrayList<>());
        }
        return s.getDailyStatistics();
    }

    @Override
    public List<DailyPresenceStatistics> findPresenceStatisticsByResortNameAndDate(String resortName, LocalDate date) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<DailyPresenceStatistics> criteria = builder.createQuery(DailyPresenceStatistics.class);
        Root<DailyPresenceStatistics> root =  criteria.from(DailyPresenceStatistics.class);
        criteria.select(root).where(
                builder.equal(root.get("dateRaw"), date.toString()),
                builder.equal(root.get("resortName"), resortName));
        TypedQuery<DailyPresenceStatistics> query = manager.createQuery(criteria);
        try {
            return query.getResultList();
        } catch (NoResultException nre){
            log.log(Level.FINEST, "No result for resort ["+resortName+"]", nre);
            return new ArrayList<>();
        }
    }

    @Override
    public List<HourlyPresenceStatistics> findHourlyOfDaily(DailyPresenceStatistics dailyPresenceStatistics) {
        DailyPresenceStatistics d = manager.merge(dailyPresenceStatistics);
        if(d.getHourlyPresenceStatistics()==null){
            d.setHourlyPresenceStatistics(new ArrayList<>());
        }
        return d.getHourlyPresenceStatistics();
    }

    @Override
    public List<DailyPresenceStatistics> findPresenceStatisticsByDate(LocalDate date) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<DailyPresenceStatistics> criteria = builder.createQuery(DailyPresenceStatistics.class);
        Root<DailyPresenceStatistics> root =  criteria.from(DailyPresenceStatistics.class);
        criteria.select(root).where(builder.equal(root.get("dateRaw"), date.toString()));
        TypedQuery<DailyPresenceStatistics> query = manager.createQuery(criteria);
        try {
            return query.getResultList();
        } catch (NoResultException nre){
            log.log(Level.FINEST, "No result for ["+date+"]", nre);
            return new ArrayList<>();
        }
    }

    @Override
    public DailyPresenceStatistics findPresenceStatisticsOfSkiLiftByDate(LocalDate date, SkiLift skiLift) throws NoDailyStatisticsException {
        List<DailyPresenceStatistics> statistics = findSkiLiftDailyStatistics(skiLift);
        for(DailyPresenceStatistics stats : statistics){
            if(date.equals(stats.getDate())){
                return stats;
            }
        }
        throw new NoDailyStatisticsException(date);
    }

    @Override
    public HourlyPresenceStatistics findPresenceStatisticsByHour(DailyPresenceStatistics dailyStatistics, int hour) throws NoHourlyStatisticsException {
        List<HourlyPresenceStatistics> hourlyStatistics = findHourlyOfDaily(dailyStatistics);
        for(HourlyPresenceStatistics hourly : hourlyStatistics){
            if(hourly.getStartingHour() == hour){
                return hourly;
            }
        }
        throw new NoHourlyStatisticsException(hour);
    }

    @Override
    public int getByDateFrequencyBySkiLift(SkiLift skiLift, LocalDate date) throws NoDailyStatisticsException {
        DailyPresenceStatistics statistics = findPresenceStatisticsOfSkiLiftByDate(date,skiLift);
        int totalFrequency = 0;
        computePassage(statistics, totalFrequency);
        return totalFrequency;
    }

    @Override
    public int getTotalOfPassagesBySkiLift(SkiLift skiLift) {
        List<DailyPresenceStatistics> dailyStatistics = findSkiLiftDailyStatistics(skiLift);
        int totalPassage = 0;
        for(DailyPresenceStatistics daily : dailyStatistics){
            computePassage(daily,totalPassage);
        }
        return totalPassage;
    }

    @Override
    public int getByDateFrequency(LocalDate date) {
        List<DailyPresenceStatistics> dailyStatistics = findPresenceStatisticsByDate(date);
        int totalFrequency = 0;
        for(DailyPresenceStatistics daily : dailyStatistics){
            computePassage(daily,totalFrequency);
        }
        return totalFrequency;
    }

    @Override
    public int getByDateNumberOfPeopleBySkiLift(SkiLift skiLift, LocalDate date) throws NoDailyStatisticsException {
        DailyPresenceStatistics daily = findPresenceStatisticsOfSkiLiftByDate(date, skiLift);
        int totalNumberOfPeople = 0;
        computeNumberOfPeople(daily,totalNumberOfPeople);
        return 0;
    }

    @Override
    public int getTotalOfPeopleBySkiLift(SkiLift skiLift) {
        List<DailyPresenceStatistics> dailyStatistics = findSkiLiftDailyStatistics(skiLift);
        int totalNumberOfPeople = 0;
        for(DailyPresenceStatistics daily : dailyStatistics){
            computeNumberOfPeople(daily,totalNumberOfPeople);
        }
        return totalNumberOfPeople;
    }

    private void computePassage(DailyPresenceStatistics daily, int counter){
        List<HourlyPresenceStatistics> hourlyStatistics = findHourlyOfDaily(daily);
        for(HourlyPresenceStatistics hourly : hourlyStatistics){
            counter+=hourly.getNumberOfPassage();
        }
    }

    private void computeNumberOfPeople(DailyPresenceStatistics daily, int counter){
        List<HourlyPresenceStatistics> hourlyStatistics = findHourlyOfDaily(daily);
        for(HourlyPresenceStatistics hourly : hourlyStatistics){
            counter+=hourly.getNumberOfCardsBeeped();
        }
    }
}
