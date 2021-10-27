package fr.unice.polytech.isa.statistics.webservices;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(targetNamespace = "http://www.polytech.unice.fr/isa/castexski/purchasestatistics")
public interface PurchaseStatisticsWebService {
    /**
     * Returns the total of all passes bought at a certain date
     * @param localDateRaw the date in LocalDate.toString() format
     * @return the total of passes
     */
    @WebMethod
    int getNumberOfAllPassesBoughtOnDate(
        @WebParam(name="localDateRaw") String localDateRaw
    );

    /**
     * Returns the total of a certain pass bought at a certain date
     * @param passName the pass concerned
     * @param localDateRaw the date in LocalDate.toString() format
     * @return the total of passes
     */
    @WebMethod
    int getNumberOfSpecificPassBoughtOnDate(
        @WebParam(name="passName") String passName,
        @WebParam(name="localDateRaw") String localDateRaw
    );

    /**
     * Returns the total of child passes bought on a certain date
     * @param localDateRaw the date in LocalDate.toString() format
     * @return the total of passes
     */
    @WebMethod
    int getNumberOfChildPassesBoughtOnDate(
        @WebParam(name="localDateRaw") String localDateRaw
    );

    /**
     * Returns the total of super Cartex bought on a certain date
     * @param localDateRaw the date in LocalDate.toString() format
     * @return the total of cards
     */
    @WebMethod
    int getNumberOfSuperCartexBoughtOnDate(
        @WebParam(name="localDateRaw") String localDateRaw
    );

    /**
     * Returns the total of normal cards bought on a certain date
     * @param localDateRaw the date in LocalDate.toString() format
     * @return the total of cards
     */
    @WebMethod
    int getNumberOfCardsBoughtOnDate(
        @WebParam(name="localDateRaw") String localDateRaw
    );

    @WebMethod
    String detailedPurchaseStatisticsByDate(String localDate);
}
