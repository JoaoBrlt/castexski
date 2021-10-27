package fr.unice.polytech.isa.statistics.components.purchases;

import fr.unice.polytech.isa.common.entities.statistics.DailyBuyingStatistics;
import fr.unice.polytech.isa.common.entities.statistics.HourlyBuyingStatistics;
import fr.unice.polytech.isa.statistics.exceptions.NoHourlyStatisticsException;
import fr.unice.polytech.isa.statistics.interfaces.purchases.PurchaseStatisticsFinder;
import fr.unice.polytech.isa.statistics.interfaces.purchases.PurchaseStatisticsRegistration;
import fr.unice.polytech.isa.statistics.interfaces.purchases.PurchaseStatisticsUpdater;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.Logger;

@Stateless
public class PurchaseStatisticsUpdateBean implements PurchaseStatisticsUpdater {
    private static final Logger log = Logger.getLogger(Logger.class.getName());

    @PersistenceContext
    private EntityManager manager;

    @EJB
    private PurchaseStatisticsFinder purchaseStatisticsFinder;

    @EJB
    private PurchaseStatisticsRegistration purchaseStatisticsRegistration;

    @Override
    public void addPassWithDate(String passName, int quantity, boolean isChildPass, LocalDateTime dateTime) throws Exception {
        LocalDate localDate = LocalDate.of(
            dateTime.getYear(),
            dateTime.getMonthValue(),
            dateTime.getDayOfMonth()
        );
        int hour = dateTime.getHour();

        purchaseStatisticsRegistration.addNewDailyBuyingStatistics(localDate);
        purchaseStatisticsRegistration.addNewHourlyBuyingStatistics(dateTime);

        DailyBuyingStatistics dailyBuyingStatistics = purchaseStatisticsFinder.findPurchaseStatisticsByDate(localDate);
        dailyBuyingStatistics = manager.merge(dailyBuyingStatistics);
        HourlyBuyingStatistics hourlyBuyingStatistics = new HourlyBuyingStatistics(hour);

        try {
            hourlyBuyingStatistics = purchaseStatisticsFinder.findPurchaseStatisticsByDateAndHour(dateTime,hour);
            hourlyBuyingStatistics = manager.merge(hourlyBuyingStatistics);
        } catch (NoHourlyStatisticsException e){
            dailyBuyingStatistics.addHourlyBuyingStatistic(hourlyBuyingStatistics);
        }

        hourlyBuyingStatistics.addPass(passName, quantity);
        if (isChildPass){
            hourlyBuyingStatistics.addNbOfChildPasses(quantity);
        }

        manager.persist(hourlyBuyingStatistics);
        manager.persist(dailyBuyingStatistics);
    }

    @Override
    public void addPass(String passName, int quantity, boolean isChildPass) throws Exception {
        LocalDateTime dateTime = LocalDateTime.now();
        addPassWithDate(passName,quantity,isChildPass,dateTime);
    }


    @Override
    public void addCardWithDate(int quantity, LocalDateTime dateTime) throws Exception {
        LocalDate localDate = LocalDate.of(
            dateTime.getYear(),
            dateTime.getMonthValue(),
            dateTime.getDayOfMonth()
        );
        int hour = dateTime.getHour();

        purchaseStatisticsRegistration.addNewDailyBuyingStatistics(localDate);
        purchaseStatisticsRegistration.addNewHourlyBuyingStatistics(dateTime);

        DailyBuyingStatistics dailyBuyingStatistics = purchaseStatisticsFinder.findPurchaseStatisticsByDate(localDate);
        dailyBuyingStatistics = manager.merge(dailyBuyingStatistics);
        HourlyBuyingStatistics hourlyBuyingStatistics = new HourlyBuyingStatistics(hour);

        try {
            hourlyBuyingStatistics = purchaseStatisticsFinder.findPurchaseStatisticsByDateAndHour(dateTime,hour);
            hourlyBuyingStatistics = manager.merge(hourlyBuyingStatistics);
        } catch (NoHourlyStatisticsException e){
            dailyBuyingStatistics.addHourlyBuyingStatistic(hourlyBuyingStatistics);
        }

        hourlyBuyingStatistics.addNbOfCards(quantity);

        manager.persist(hourlyBuyingStatistics);
        manager.persist(dailyBuyingStatistics);
    }

    @Override
    public void addCard(int quantity) throws Exception {
        LocalDateTime dateTime = LocalDateTime.now();
        addCardWithDate(quantity,dateTime);
    }

    @Override
    public void addSuperCartexWithDate(int quantity, LocalDateTime dateTime) throws Exception {
        LocalDate localDate = LocalDate.of(
            dateTime.getYear(),
            dateTime.getMonthValue(),
            dateTime.getDayOfMonth()
        );
        int hour = dateTime.getHour();

        purchaseStatisticsRegistration.addNewDailyBuyingStatistics(localDate);
        purchaseStatisticsRegistration.addNewHourlyBuyingStatistics(dateTime);

        DailyBuyingStatistics dailyBuyingStatistics = purchaseStatisticsFinder.findPurchaseStatisticsByDate(localDate);
        dailyBuyingStatistics = manager.merge(dailyBuyingStatistics);
        HourlyBuyingStatistics hourlyBuyingStatistics = new HourlyBuyingStatistics(hour);

        try {
            hourlyBuyingStatistics = purchaseStatisticsFinder.findPurchaseStatisticsByDateAndHour(dateTime,hour);
            hourlyBuyingStatistics = manager.merge(hourlyBuyingStatistics);
        } catch (NoHourlyStatisticsException e){
            dailyBuyingStatistics.addHourlyBuyingStatistic(hourlyBuyingStatistics);
        }

        hourlyBuyingStatistics.addNbOfCartex(quantity);

        manager.persist(hourlyBuyingStatistics);
        manager.persist(dailyBuyingStatistics);
    }

    @Override
    public void addSuperCartex(int quantity) throws Exception {
        LocalDateTime dateTime = LocalDateTime.now();
        addSuperCartexWithDate(quantity,dateTime);
    }
}
