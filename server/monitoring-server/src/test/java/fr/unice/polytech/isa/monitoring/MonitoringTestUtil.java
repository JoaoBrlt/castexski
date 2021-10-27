package fr.unice.polytech.isa.monitoring;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

/** Centralization of the constants used to test the Monitoring server. ******/

public class MonitoringTestUtil {

    public static final double DELTA = 1e-15;

    /******************* Customer *********************/

    public static final String MARCEL_FIRSTNAME = "Marcel";
    public static final String MARCEL_LASTNAME = "Eats";
    public static final String MARCEL_EMAIL = "marceldu06@gmail.com";
    public static final String CREDIT_CARD_NO = "1234567891896983";
    public static final YearMonth EXPIRY_DATE = YearMonth.of(2100, 1);
    public static final String CVV = "123";
    public static final boolean SAVE = true;
    public static final boolean DONT_SAVE = false;

    /******************* Item *********************/

    public static final String DISCOVERY_PASS = "Discovery";
    public static final String SUPER_ORIGINAL_PASS = "SUPER_ORIGINAL";
    public static final String SUPER_FREE_EIGHTH = "SUPER_FREE_EIGHTH";
    public static final String SUPER_FREE_HOUR_PASS = "SUPER_FREE_HOUR";
    public static final String SUPER_DISCOUNT_THIS_MONTH = "SUPER_DISCOUNT_"+ LocalDate.now().format(DateTimeFormatter.ofPattern("MM-yyyy"));
    public static final String SUPER_DISCOUNT_TODAY = "SUPER_DISCOUNT_"+ LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

    public static final String SEASON_PASS = "Season";
    public static final String CARTEX = "Cartex";
    public static final String SUPER_CARTEX = "Super Cartex";
    public static final boolean NORMAL_CARD = false;
    public static final boolean SUPERCARTEX_CARD = true;
    public static final boolean CHILD_PASS = true;
    public static final boolean ADULT_PASS = false;
    public static final String ADULT = "adult";
    public static final String REGULAR = "regular";
    public static final boolean PUBLIC_ITEM = false;
    public static final boolean PRIVATE_ITEM = true;
    /******************* Duration *********************/

    public static final Duration HOURS_1 = Duration.ofHours(1);
    public static final Duration DAYS_1 = Duration.ofDays(1);
    public static final Duration DAYS_3 = Duration.ofDays(3);
    public static final Duration DAYS_5 = Duration.ofDays(5);
    public static final LocalDateTime DATE_010121_9PM = LocalDateTime.of(2021, 1, 1, 9, 0);
    public static final LocalDateTime DATE_NOW = LocalDateTime.now();
    public static final LocalDateTime DATE_NOW_MINUS_7_DAYS = LocalDateTime.now().minusDays(7);

    /******************* Price *********************/

    public static final double PRICE_25 = 25;
    public static final double PRICE_20 = 20;
    public static final double PRICE_15 = 15;
    public static final double PRICE_10 = 10;
    public static final double PRICE_2 = 2;
    public static final double PRICE_0 = 0;

    /******************* Quantity *********************/

    public static final int QTY_2 = 2;
    public static final int QTY_1 = 1;
    public static final int QTY_0 = 0;

    /******************* Exceptions ******************/

    public static final String ITEM_ALREADY_EXIST_EXCEPTION = ": ItemAlreadyExistException";
    public static final String UNKNOWN_CATALOG_ENTRY_EXCEPTION = ": UnknownCatalogEntryException";

    /****************** Ski lift *******************/
    public static final String SKI_LIFT_NAME = "skiLiftTest";
    public static final String SKI_LIFT_NAME_2 = "ski lift test";
    public static final String BAD_SKI_LIFT_ID = "thisWontWork";

    /****************** Resort *******************/
    public static final String RESORT_NAME = "resortTest";
    public static final String BAD_RESORT_ID = "thisWontWork";
    public static final String RESORT_EMAIL = "resort@castex2000.fr";
    public static final String RESORT_CITY_NAME = "Isola2000";
    public static final boolean OPEN = true;
    public static final boolean CLOSE = false;
    public static final String PHYSICAL_ID1 = "0123456789101121";

    /****************** Ski Trail *******************/
    public static final String SKI_TRAIL_NAME = "SkiTrailTest";
}
