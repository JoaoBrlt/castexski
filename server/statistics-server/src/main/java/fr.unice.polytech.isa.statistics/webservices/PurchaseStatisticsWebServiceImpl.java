package fr.unice.polytech.isa.statistics.webservices;

import fr.unice.polytech.isa.common.entities.statistics.DailyBuyingStatistics;
import fr.unice.polytech.isa.common.entities.statistics.HourlyBuyingStatistics;
import fr.unice.polytech.isa.statistics.exceptions.NoDailyStatisticsException;
import fr.unice.polytech.isa.statistics.interfaces.purchases.PurchaseStatisticsFinder;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;
import java.time.LocalDate;
import java.util.HashMap;

@WebService(targetNamespace = "http://www.polytech.unice.fr/isa/castexski/purchasestatistics")
@Stateless(name = "PurchaseStatisticsWS")
public class PurchaseStatisticsWebServiceImpl implements PurchaseStatisticsWebService {
    @EJB
    private PurchaseStatisticsFinder purchaseStatisticsFinder;

    @Override
    public int getNumberOfAllPassesBoughtOnDate(String localDateRaw) {
        LocalDate date = LocalDate.parse(localDateRaw);
        int totalPasses = 0;
        try {
            DailyBuyingStatistics dailyBuyingStatistics = purchaseStatisticsFinder.findPurchaseStatisticsByDate(date);
            for(HourlyBuyingStatistics hourly : dailyBuyingStatistics.getHourlyBuyingStatistics()) {
                HashMap<String,Integer> passes = hourly.getNbOfPasses();
                totalPasses += computeTotalNumberOfPasses(passes);
            }
        } catch (NoDailyStatisticsException ignored ){ }
        return totalPasses;
    }

    private int computeTotalNumberOfPasses(HashMap<String,Integer> passes){
        int total = 0;
        for(String s : passes.keySet()){
            total+= passes.get(s);
        }
        return total;
    }

    @Override
    public int getNumberOfSpecificPassBoughtOnDate(String passName, String localDateRaw) {
        LocalDate date = LocalDate.parse(localDateRaw);
        int totalPass = 0;
        try {
            DailyBuyingStatistics dailyBuyingStatistics = purchaseStatisticsFinder.findPurchaseStatisticsByDate(date);
            for (HourlyBuyingStatistics hourly : dailyBuyingStatistics.getHourlyBuyingStatistics()) {
                HashMap<String,Integer> passes = hourly.getNbOfPasses();
                if(passes.containsKey(passName)) {totalPass+=passes.get(passName);}
            }
        } catch (NoDailyStatisticsException ignored){ }
        return totalPass;
    }

    @Override
    public int getNumberOfChildPassesBoughtOnDate(String localDateRaw) {
        LocalDate date = LocalDate.parse(localDateRaw);
        int totalPasses = 0;
        try {
            DailyBuyingStatistics dailyBuyingStatistics = purchaseStatisticsFinder.findPurchaseStatisticsByDate(date);
            for (HourlyBuyingStatistics hourly : dailyBuyingStatistics.getHourlyBuyingStatistics()) {
                totalPasses+= hourly.getNbOfChildPasses();
            }
        } catch (NoDailyStatisticsException ignored){ }
        return totalPasses;
    }

    @Override
    public int getNumberOfSuperCartexBoughtOnDate(String localDateRaw) {
        LocalDate date = LocalDate.parse(localDateRaw);
        int totalCards = 0;
        try {
            DailyBuyingStatistics dailyBuyingStatistics = purchaseStatisticsFinder.findPurchaseStatisticsByDate(date);
            for (HourlyBuyingStatistics hourly : dailyBuyingStatistics.getHourlyBuyingStatistics()) {
                totalCards+= hourly.getNbOfCartex();
            }
        } catch (NoDailyStatisticsException ignored){ }
        return totalCards;
    }

    @Override
    public int getNumberOfCardsBoughtOnDate(String localDateRaw) {
        LocalDate date = LocalDate.parse(localDateRaw);
        int totalCards = 0;
        try {
            DailyBuyingStatistics dailyBuyingStatistics = purchaseStatisticsFinder.findPurchaseStatisticsByDate(date);
            for (HourlyBuyingStatistics hourly : dailyBuyingStatistics.getHourlyBuyingStatistics()) {
                totalCards+= hourly.getNbOfCards();
            }
        } catch (NoDailyStatisticsException ignored){ }
        return totalCards;
    }

    @Override
    public String detailedPurchaseStatisticsByDate(String localDate) {
        String report = "";
        LocalDate date = LocalDate.parse(localDate);
        try {
            DailyBuyingStatistics dailyBuyingStatistics = purchaseStatisticsFinder.findPurchaseStatisticsByDate(date);
            for(HourlyBuyingStatistics hourly : dailyBuyingStatistics.getHourlyBuyingStatistics()){
                report+=buildForOneHour(hourly);
            }
        } catch (NoDailyStatisticsException e){
            return "There are no purchase statistics on the " + localDate;
        }
        return report;
    }

    private String buildForOneHour(HourlyBuyingStatistics hourlyBuyingStatistics){
        int hour = hourlyBuyingStatistics.getStartingHour();
        String result = "Between " + hour + "h and " + (hour+1) + "h : \n" ;
        result += "    - Cards bought ["+hourlyBuyingStatistics.getNbOfCards()+"]\n";
        result += "    - SuperCartex bought ["+hourlyBuyingStatistics.getNbOfCartex()+"]\n";
        HashMap<String, Integer> passes = hourlyBuyingStatistics.getNbOfPasses();
        if(passes.size()>0) {
            result+=buildPassesString(passes);
            result+= ", there are " + hourlyBuyingStatistics.getNbOfChildPasses() + " child passes.\n";
        }
        result+="\n \n";
        return result;
    }

    private String buildPassesString(HashMap<String, Integer> passes){
        String result = "";
            result += "    - Types of passes bought : ";
            for (String pass : passes.keySet()) {
                result += " [" + pass + ", " + passes.get(pass) + "] ";
            }
        return result;
    }
}
