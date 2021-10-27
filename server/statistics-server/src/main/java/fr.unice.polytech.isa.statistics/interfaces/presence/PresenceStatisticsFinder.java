package fr.unice.polytech.isa.statistics.interfaces.presence;

import fr.unice.polytech.isa.common.entities.statistics.DailyPresenceStatistics;
import fr.unice.polytech.isa.common.entities.statistics.HourlyPresenceStatistics;
import fr.unice.polytech.isa.common.entities.resort.SkiLift;
import fr.unice.polytech.isa.statistics.exceptions.NoDailyStatisticsException;
import fr.unice.polytech.isa.statistics.exceptions.NoHourlyStatisticsException;

import javax.ejb.Local;
import java.time.LocalDate;
import java.util.List;

@Local
public interface PresenceStatisticsFinder {
    List<DailyPresenceStatistics> findDailyStatisticsBySkiLiftAndResortName(String skiLiftName,String resortName);
    DailyPresenceStatistics findPresenceStatisticsBySkiLiftAndResortNameAndDate(String skiLiftName, String resortName, LocalDate date) throws NoDailyStatisticsException;
    List<DailyPresenceStatistics> findPresenceStatisticsInAWeek(String resortName,String skiLiftName, int weekNumber);
    List<DailyPresenceStatistics> findPresenceStatisticsInAMonth(String resortName, String skiLiftName, int monthNumber);
    List<DailyPresenceStatistics> findPresenceStatisticsInAYear(String resortName, String skiLiftName, int year);
    List<DailyPresenceStatistics> findSkiLiftDailyStatistics(SkiLift skiLift);
    List<DailyPresenceStatistics> findPresenceStatisticsByResortNameAndDate(String resortName, LocalDate date);
    List<HourlyPresenceStatistics> findHourlyOfDaily(DailyPresenceStatistics dailyPresenceStatistics);
    List<DailyPresenceStatistics> findPresenceStatisticsByDate(LocalDate date);
    DailyPresenceStatistics findPresenceStatisticsOfSkiLiftByDate(LocalDate date, SkiLift skiLift)
            throws NoDailyStatisticsException;
    HourlyPresenceStatistics findPresenceStatisticsByHour(DailyPresenceStatistics dailyStatistics, int hour)
            throws NoHourlyStatisticsException;
    int getByDateFrequencyBySkiLift(SkiLift skiLift, LocalDate date) throws NoDailyStatisticsException;
    int getTotalOfPassagesBySkiLift(SkiLift skiLift);
    int getByDateFrequency(LocalDate date);
    int getByDateNumberOfPeopleBySkiLift(SkiLift skiLift, LocalDate date) throws NoDailyStatisticsException;
    int getTotalOfPeopleBySkiLift(SkiLift skiLift);
}
