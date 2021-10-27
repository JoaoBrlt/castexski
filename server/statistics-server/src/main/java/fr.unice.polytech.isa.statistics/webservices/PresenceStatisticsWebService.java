package fr.unice.polytech.isa.statistics.webservices;

import fr.unice.polytech.isa.common.entities.resort.SkiLift;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

@WebService(targetNamespace = "http://www.polytech.unice.fr/isa/castexski/presencestatistics")
public interface PresenceStatisticsWebService {
    /**
     * Returns the number of people that scanned their card between hour and (hour+1), at the
     * given date and ski lift
     * @param resortName the resort where the card was scanned
     * @param skiLiftName the ski lift where the card was scanned
     * @param localDateRaw the date when the card was scanned
     * @param hour when the card was scanned
     * @return the number of people that scanned their card
     */
    @WebMethod
    int getPresenceStatisticOnSkiLiftByDateAndHour(
        @WebParam (name="resortName") String resortName,
        @WebParam (name="skiLiftName")String skiLiftName,
        @WebParam (name="localDateRaw") String localDateRaw,
        @WebParam (name="hour") int hour
    );

    /**
     * Returns the number of people that scanned their card at a certain date
     * @param resortName the resort where the card was scanned
     * @param skiLiftName the ski lift where the card was scanned
     * @param localDateRaw the date when the card was scanned
     * @return the number of people that scanned their card
     */
    @WebMethod
    int getPresenceStatisticOnSkiLiftByDate(
        @WebParam (name="resortName") String resortName,
        @WebParam (name="skiLiftName")String skiLiftName,
        @WebParam (name="localDateRaw") String localDateRaw
    );

    /**
     * Returns the number of people that scanned their card during the given week
     * @param resortName the resort where the card was scanned
     * @param skiLiftName the ski lift where the card was scanned
     * @param weekNumber the number of the week (between 1 & 52)
     * @return the number of people that scanned their card
     */
    @WebMethod
    int getPresenceStatisticOnSkiLiftOnWeek(
        @WebParam (name="resortName") String resortName,
        @WebParam (name="skiLiftName")String skiLiftName,
        @WebParam (name="weekNumber") int weekNumber
    );

    /**
     * Returns the number of people that scanned their card during the given month
     * @param resortName the resort where the card was scanned
     * @param skiLiftName the ski lift where the card was scanned
     * @param monthNumber the number of the month
     * @return the number of people that scanned their card
     */
    @WebMethod
    int getPresenceStatisticOnSkiLiftOnMonth(
        @WebParam (name="resortName") String resortName,
        @WebParam (name="skiLiftName")String skiLiftName,
        @WebParam (name="monthNumber") int monthNumber
    );

    /**
     * Returns the number of people that scanned their card during the given year
     * @param resortName the resort where the card was scanned
     * @param skiLiftName the ski lift where the card was scanned
     * @param yearNumber the number of the year
     * @return the number of people that scanned their card
     */
    @WebMethod
    int getPresenceStatisticOnSkiLiftOnYear(
        @WebParam (name="resortName") String resortName,
        @WebParam (name="skiLiftName")String skiLiftName,
        @WebParam (name="yearNumber") int yearNumber
    );

    /**
     * Returns the number of people at a certain resort at a certain date
     * @param resortName the name of the resort
     * @param localDateRaw the date
     * @return the number of people present
     */
    @WebMethod
    int getPresenceStatisticsOnResortByDate(
        @WebParam (name="resortName") String resortName,
        @WebParam (name="localDateRaw") String localDateRaw
    );

    @WebMethod
    String seeActualPresenceStatistics(String resortName);

    @WebMethod
    String seeActualPresenceStatisticsBySkiLift(List<SkiLift> skiLifts);

    @WebMethod
    String seeActualPresenceStatisticsOnSkiLift(SkiLift skiLift);

    @WebMethod
    String seePresenceStatisticsOfDay(String resortName,String localDateRaw);
}
