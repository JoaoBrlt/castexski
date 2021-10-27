package fr.unice.polytech.isa.statistics.components;

import fr.unice.polytech.isa.common.entities.statistics.DailyPresenceStatistics;
import fr.unice.polytech.isa.common.entities.statistics.HourlyPresenceStatistics;
import fr.unice.polytech.isa.statistics.interfaces.DailyReportCreator;
import fr.unice.polytech.isa.statistics.interfaces.presence.PresenceStatisticsFinder;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.time.LocalDate;
import java.util.*;

@Stateless
public class DailyReportBean implements DailyReportCreator {

    @EJB
    private PresenceStatisticsFinder finder;

    private HashMap<Integer,Integer> result = new HashMap<>();;

    @Override
    public String constructPresenceReportOfDate(String resortName, LocalDate date){
        String introduction = "Dear subscriber,\n\nPlease find the Daily Presence Statistics of the day at station "+resortName+ " : \n\n";

        String ending = "\nThank you for subscribing to our mailing list.\n" +
                        "\nSincerly yours,\n" +
                        "\nThe CastexSki teamF.\n";
        return buildSpecificReport(introduction,ending,resortName,date);
    }

    @Override
    public String buildSpecificReport(String introduction, String ending, String resortName, LocalDate date){
        String report = introduction;
        List<DailyPresenceStatistics> todaysStatistics = finder.findPresenceStatisticsByResortNameAndDate(resortName,date);

        if (todaysStatistics.isEmpty()) {
            report+= "\nSadly, there are no statistics to display.\n";
        } else {
            result = new HashMap<>(); // will contain all active hours (8h - 18h)
            result.put(8,0);result.put(9,0);result.put(10,0);result.put(11,0);result.put(12,0);
            result.put(13,0);result.put(14,0);result.put(15,0);result.put(16,0);result.put(17,0);
            for(DailyPresenceStatistics daily : todaysStatistics){
                List<HourlyPresenceStatistics> hourlyPresenceStatistics = daily.getHourlyPresenceStatistics();
                for(HourlyPresenceStatistics hourly : hourlyPresenceStatistics){
                    int hour = hourly.getStartingHour();
                    int passage = hourly.getNumberOfCardsBeeped();
                    int oldValue = result.get(hour);
                    result.replace(hour,oldValue,oldValue+passage);
                }
            }
            List<Integer> keys = new ArrayList<>(result.keySet());
            Collections.sort(keys);
            report+="Number of people present between...\n ";
            for(Integer i : keys){
                report+=i+"h and "+(i+1)+"h : "+result.get(i) + "\n ";
            }
        }
        report += ending;
        return report;
    }
}
