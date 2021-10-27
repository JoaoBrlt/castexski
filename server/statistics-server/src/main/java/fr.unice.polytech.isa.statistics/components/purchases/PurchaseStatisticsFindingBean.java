package fr.unice.polytech.isa.statistics.components.purchases;

import fr.unice.polytech.isa.common.entities.statistics.DailyBuyingStatistics;
import fr.unice.polytech.isa.common.entities.statistics.HourlyBuyingStatistics;
import fr.unice.polytech.isa.statistics.exceptions.NoDailyStatisticsException;
import fr.unice.polytech.isa.statistics.exceptions.NoHourlyStatisticsException;
import fr.unice.polytech.isa.statistics.interfaces.purchases.PurchaseStatisticsFinder;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class PurchaseStatisticsFindingBean implements PurchaseStatisticsFinder {
    private static final Logger log = Logger.getLogger(Logger.class.getName());

    @PersistenceContext
    private EntityManager manager;

    @Override
    public DailyBuyingStatistics findPurchaseStatisticsByDate(LocalDate date) throws NoDailyStatisticsException {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<DailyBuyingStatistics> criteria = builder.createQuery(DailyBuyingStatistics.class);
        Root<DailyBuyingStatistics> root =  criteria.from(DailyBuyingStatistics.class);
        criteria.select(root).where(builder.equal(root.get("dateRaw"), date.toString()));
        TypedQuery<DailyBuyingStatistics> query = manager.createQuery(criteria);
        try {
            return query.getSingleResult();
        } catch (NoResultException nre){
            log.log(Level.FINEST, "No result for ["+date+"]", nre);
            throw new NoDailyStatisticsException(date);
        }
    }

    @Override
    public List<HourlyBuyingStatistics> findStatisticsOfDaily(DailyBuyingStatistics dailyBuyingStatistics) {
        DailyBuyingStatistics s = manager.merge(dailyBuyingStatistics);
        if(s.getHourlyBuyingStatistics()==null){
            s.setHourlyBuyingStatistics(new ArrayList<>());
        }
        return s.getHourlyBuyingStatistics();
    }

    @Override
    public HourlyBuyingStatistics findPurchaseStatisticsByHour(DailyBuyingStatistics dailyStatistics, int hour) throws NoHourlyStatisticsException, NoDailyStatisticsException {
        LocalDate date = dailyStatistics.getDate();
        LocalDateTime dateTime = LocalDateTime.of(date.getYear(),date.getMonthValue(),date.getDayOfMonth(),hour,0);
        return findPurchaseStatisticsByDateAndHour(dateTime, hour);
    }

    @Override
    public HourlyBuyingStatistics findPurchaseStatisticsByDateAndHour(LocalDateTime localDateTime, int hour) throws NoHourlyStatisticsException, NoDailyStatisticsException {
        LocalDate date = LocalDate.of(localDateTime.getYear(),localDateTime.getMonthValue(),localDateTime.getDayOfMonth());
        DailyBuyingStatistics dailyBuyingStatistics = findPurchaseStatisticsByDate(date);
        List<HourlyBuyingStatistics> hourlyBuyingStatisticsList = dailyBuyingStatistics.getHourlyBuyingStatistics();
        for(HourlyBuyingStatistics hourlyBuyingStatistics : hourlyBuyingStatisticsList){
            if(hourlyBuyingStatistics.getStartingHour() == hour){
                return hourlyBuyingStatistics;
            }
        }
        throw new NoHourlyStatisticsException(hour);
    }
}
