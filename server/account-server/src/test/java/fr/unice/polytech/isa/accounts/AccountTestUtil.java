package fr.unice.polytech.isa.accounts;

import fr.unice.polytech.isa.common.entities.shopping.catalog.ItemType;
import fr.unice.polytech.isa.common.entities.items.ItemTypeName;
import fr.unice.polytech.isa.common.entities.shopping.catalog.PassType;
import fr.unice.polytech.isa.common.entities.items.Card;

import java.time.Duration;
import java.time.YearMonth;

/** Centralization of the constants used to test the Shopping server. ******/

public class AccountTestUtil {

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

    /******************* Duration *********************/

    public static final Duration DAYS_1 = Duration.ofDays(1);
    public static final Duration DAYS_3 = Duration.ofDays(3);
    public static final Duration DAYS_5 = Duration.ofDays(5);


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

    /******************* Item *********************/

    public static final String DISCOVERY_PASS = "Discovery";
    public static final String SEASON_PASS = "Season";
    public static final String CARTEX = "Cartex";
    public static final String SUPER_CARTEX = "Super Cartex";
    public static final boolean NORMAL_CARD = false;
    public static final boolean SUPERCARTEX_CARD = true;
    public static final boolean CHILD_PASS = true;
    public static final boolean ADULT_PASS = false;
    public static final String ADULT = "adult";
    public static final String REGULAR = "regular";
    public static final String PHYSICAL_ID1 = "0123456789101121";
    public static final PassType DISCOVERY_CHILD_DAYS_3_PRICE_10 = new PassType(DISCOVERY_PASS, CHILD_PASS, DAYS_3, PRICE_10);
    public static final ItemType CARTEX_PRICE_2 = new ItemType(CARTEX, PRICE_2, ItemTypeName.CARD);
    public static final Card CARD_CARTEX = new Card(CARTEX_PRICE_2,PHYSICAL_ID1);

    /******************* Exceptions ******************/

    public static final String ITEM_ALREADY_EXIST_EXCEPTION = ": ItemAlreadyExistException";
    public static final String UNKNOWN_CATALOG_ENTRY_EXCEPTION = ": UnknownCatalogEntryException";
}
