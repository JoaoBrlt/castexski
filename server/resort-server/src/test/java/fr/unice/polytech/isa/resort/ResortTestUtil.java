package fr.unice.polytech.isa.resort;

import java.time.Duration;
import java.time.YearMonth;

/** Centralization of the constants used to test the Shopping server. ******/

public class ResortTestUtil {

    public static final double DELTA = 1e-15;

    /******************* Customer *********************/

    public static final String MARCEL_FIRSTNAME = "Marcel";
    public static final String MARCEL_LASTNAME = "Eats";
    public static final String MARCEL_EMAIL = "marceldu06@gmail.com";
    public static final String JOHN_FIRSTNAME = "John";
    public static final String JOHN_LASTNAME = "Doe";
    public static final String JOHN_EMAIL = "john@doe.com";
    public static final String CREDIT_CARD_NO = "1234567891896983";
    public static final YearMonth EXPIRY_DATE = YearMonth.of(2100, 1);
    public static final String CVV = "123";
    public static final boolean SAVE = true;


    /******************* Item *********************/

    public static final String DISCOVERY_PASS = "Discovery";
    public static final String SUPER_ORIGINAL_PASS = "SUPER_ORIGINAL";
    public static final String SUPER_FREE_EIGHTH = "SUPER_FREE_EIGHTH";
    public static final String SUPER_FREE_HOUR_PASS = "SUPER_FREE_HOUR";

    public static final String SEASON_PASS = "Season";
    public static final String SUPER_CARTEX = "Super Cartex";
    public static final boolean SUPERCARTEX_CARD = true;
    public static final String REGULAR = "regular";
    public static final boolean PUBLIC_ITEM = false;
    public static final String PHYSICAL_CARD_ID = "0123456789101121";
    public static final boolean PRIVATE_ITEM = true;
    /******************* Duration *********************/

    public static final Duration HOURS_1 = Duration.ofHours(1);
    public static final Duration DAYS_1 = Duration.ofDays(1);


    /******************* Price *********************/

    public static final double PRICE_10 = 10;
    public static final double PRICE_0 = 0;

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

    /****************** Ski Trail *******************/
    public static final String SKI_TRAIL_NAME = "SkiTrailTest";
}
