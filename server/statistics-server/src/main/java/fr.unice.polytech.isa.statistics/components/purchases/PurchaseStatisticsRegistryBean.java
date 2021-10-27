package fr.unice.polytech.isa.statistics.components.purchases;

import fr.unice.polytech.isa.common.entities.statistics.DailyBuyingStatistics;
import fr.unice.polytech.isa.common.entities.statistics.HourlyBuyingStatistics;
import fr.unice.polytech.isa.statistics.exceptions.NoDailyStatisticsException;
import fr.unice.polytech.isa.statistics.exceptions.NoHourlyStatisticsException;
import fr.unice.polytech.isa.statistics.interfaces.purchases.PurchaseStatisticsFinder;
import fr.unice.polytech.isa.statistics.interfaces.purchases.PurchaseStatisticsRegistration;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.Logger;

@Stateless
public class PurchaseStatisticsRegistryBean implements PurchaseStatisticsRegistration {
    private static final Logger log = Logger.getLogger(Logger.class.getName());

    @PersistenceContext
    private EntityManager manager;

    @EJB
    private PurchaseStatisticsFinder purchaseStatisticsFinder;

    @Override
    public void addNewDailyBuyingStatistics(LocalDate localDate) {
        try {
            purchaseStatisticsFinder.findPurchaseStatisticsByDate(localDate);
        } catch (NoDailyStatisticsException e) {
            DailyBuyingStatistics dailyStatistics = new DailyBuyingStatistics(localDate);
            manager.persist(dailyStatistics);
        }
    }

    @Override
    public void addCurrentDailyBuyingStatistics() {
        LocalDate date = LocalDate.now();
        addNewDailyBuyingStatistics(date);
    }

    @Override
    public void addNewHourlyBuyingStatistics(LocalDateTime localDateTime) throws Exception {
        LocalDate date = LocalDate.of(localDateTime.getYear(),localDateTime.getMonthValue(),localDateTime.getDayOfMonth());
        DailyBuyingStatistics dailyStatistics = purchaseStatisticsFinder.findPurchaseStatisticsByDate(date);
        try {
            purchaseStatisticsFinder.findPurchaseStatisticsByDateAndHour(localDateTime, localDateTime.getHour());
        } catch (NoHourlyStatisticsException e){
            HourlyBuyingStatistics hourlyStatistics = new HourlyBuyingStatistics(localDateTime.getHour());
            dailyStatistics.addHourlyBuyingStatistic(hourlyStatistics);
            manager.persist(hourlyStatistics);
            manager.persist(dailyStatistics);
        }
    }

    @Override
    public void addCurrentHourlyBuyingStatistics() throws Exception {
        LocalDateTime dateTime = LocalDateTime.now();
        addNewHourlyBuyingStatistics(dateTime);
    }
}
