package fr.unice.polytech.isa.statistics.webservices;

import fr.unice.polytech.isa.common.entities.statistics.DailyPresenceStatistics;
import fr.unice.polytech.isa.common.entities.statistics.HourlyPresenceStatistics;
import fr.unice.polytech.isa.common.entities.statistics.HourlyStatistics;
import fr.unice.polytech.isa.common.entities.resort.SkiLift;
import fr.unice.polytech.isa.statistics.exceptions.NoDailyStatisticsException;
import fr.unice.polytech.isa.statistics.exceptions.NoHourlyStatisticsException;
import fr.unice.polytech.isa.statistics.interfaces.DailyReportCreator;
import fr.unice.polytech.isa.statistics.interfaces.presence.PresenceStatisticsFinder;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@WebService(targetNamespace = "http://www.polytech.unice.fr/isa/castexski/presencestatistics")
@Stateless(name = "PresenceStatisticsWS")
public class PresenceStatisticsWebServiceImpl implements PresenceStatisticsWebService {
    @EJB
    private PresenceStatisticsFinder presenceStatisticsFinder;

    @EJB
    private DailyReportCreator dailyReportCreator;

    @Override
    public int getPresenceStatisticOnSkiLiftByDateAndHour(String resortName, String skiLiftName, String localDateRaw, int hour) {
        LocalDate date = LocalDate.parse(localDateRaw);
        try {
            DailyPresenceStatistics dailyPresenceStatistics = presenceStatisticsFinder.findPresenceStatisticsBySkiLiftAndResortNameAndDate(skiLiftName, resortName, date);
            HourlyPresenceStatistics hourlyPresenceStatistics = presenceStatisticsFinder.findPresenceStatisticsByHour(dailyPresenceStatistics,hour);
            return hourlyPresenceStatistics.getNumberOfCardsBeeped();
        } catch (NoDailyStatisticsException | NoHourlyStatisticsException e){
            return 0;
        }
    }

    @Override
    public int getPresenceStatisticOnSkiLiftByDate(String resortName, String skiLiftName, String localDateRaw) {
        LocalDate date = LocalDate.parse(localDateRaw);
        try {
            DailyPresenceStatistics dailyPresenceStatistics = presenceStatisticsFinder.findPresenceStatisticsBySkiLiftAndResortNameAndDate(skiLiftName, resortName, date);
            return dailyPresenceStatistics.getNumberOfPeople();
        }
        catch (NoDailyStatisticsException e){
            return 0;
        }
    }

    @Override
    public int getPresenceStatisticOnSkiLiftOnWeek(String resortName, String skiLiftName, int weekNumber) {
        List<DailyPresenceStatistics> dailyPresenceStatistics = presenceStatisticsFinder.findPresenceStatisticsInAWeek(resortName,skiLiftName,weekNumber);
        return totalPeopleForManyDaily(dailyPresenceStatistics);
    }

    @Override
    public int getPresenceStatisticOnSkiLiftOnMonth(String resortName, String skiLiftName, int monthNumber) {
        List<DailyPresenceStatistics> dailyPresenceStatistics = presenceStatisticsFinder.findPresenceStatisticsInAMonth(resortName,skiLiftName,monthNumber);
        return totalPeopleForManyDaily(dailyPresenceStatistics);
    }

    @Override
    public int getPresenceStatisticOnSkiLiftOnYear(String resortName, String skiLiftName, int yearNumber) {
        List<DailyPresenceStatistics> dailyPresenceStatistics = presenceStatisticsFinder.findPresenceStatisticsInAYear(resortName,skiLiftName,yearNumber);
        return totalPeopleForManyDaily(dailyPresenceStatistics);
    }

    @Override
    public int getPresenceStatisticsOnResortByDate(String resortName, String localDateRaw) {
        LocalDate date = LocalDate.parse(localDateRaw);
        List<DailyPresenceStatistics> dailyPresenceStatistics = presenceStatisticsFinder.findPresenceStatisticsByResortNameAndDate(resortName,date);
        return totalPeopleForManyDaily(dailyPresenceStatistics);
    }

    private int totalPeopleForManyDaily(List<DailyPresenceStatistics> dailyPresenceStatistics){
        List<String> cardsToIdentifyPeople = new ArrayList<>();
        for(DailyPresenceStatistics daily : dailyPresenceStatistics){
            for(HourlyPresenceStatistics hourly : daily.getHourlyPresenceStatistics()){
                for(String s : hourly.getBeepedCards().keySet()){
                    if(!cardsToIdentifyPeople.contains(s)){
                        cardsToIdentifyPeople.add(s);
                    }
                }
            }
        }
        return cardsToIdentifyPeople.size();
    }

    @Override
    public String seeActualPresenceStatistics(String resortName) {
        LocalDate date = LocalDate.now();
        return dailyReportCreator.buildSpecificReport("Actual presence statistics : \n","",resortName,date);
    }

    @Override
    public String seeActualPresenceStatisticsBySkiLift(List<SkiLift> skiLifts) {
        String result = "Presence statistics by SkiLift : \n";
        LocalDate date = LocalDate.now();
        for(SkiLift lift : skiLifts){
            result+=buildAffluenceDisplayForSkiLift(date,lift)+"\n";
        }
        return result;
    }

    @Override
    public String seeActualPresenceStatisticsOnSkiLift(SkiLift skiLift) {
        LocalDate date = LocalDate.now();
        return buildAffluenceDisplayForSkiLift(date,skiLift);
    }

    @Override
    public String seePresenceStatisticsOfDay(String resortName, String localDateRaw) {
        LocalDate date = LocalDate.parse(localDateRaw);
        return dailyReportCreator.buildSpecificReport("Presence statistics of day " + localDateRaw + " : \n","",resortName,date);
    }

    private String buildAffluenceDisplayForSkiLift(LocalDate date, SkiLift skiLift) {
        String result = "For SkiLift " + skiLift.getName() +", the affluence by hour is : \n";
        try {
            DailyPresenceStatistics dailyPresenceStatistics = presenceStatisticsFinder.findPresenceStatisticsOfSkiLiftByDate(date, skiLift);
            List<HourlyPresenceStatistics> hourlyPresenceStatistics = dailyPresenceStatistics.getHourlyPresenceStatistics();
            hourlyPresenceStatistics.sort(Comparator.comparingInt(HourlyStatistics::getStartingHour));
            //TODO : vérifier l'ordre : rajouter .reverse() si c'est pas dans le bon ordre
            for(HourlyPresenceStatistics hourly : hourlyPresenceStatistics){
                int hour = hourly.getStartingHour();
                result+="Between " + hour + "h and " + (hour+1) + "h : " + hourly.getNumberOfPassage() + "\n";
            }
        } catch (NoDailyStatisticsException e){
            return "There are no statistics for SkiLift " + skiLift.getName() + " on the " + date.toString() +".\n";
        }
        return result;
    }

}
