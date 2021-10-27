package fr.unice.polytech.isa.statistics.interfaces.purchases;

import fr.unice.polytech.isa.common.entities.statistics.DailyBuyingStatistics;
import fr.unice.polytech.isa.common.entities.statistics.HourlyBuyingStatistics;
import fr.unice.polytech.isa.statistics.exceptions.NoDailyStatisticsException;
import fr.unice.polytech.isa.statistics.exceptions.NoHourlyStatisticsException;

import javax.ejb.Local;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Local
public interface PurchaseStatisticsFinder {
    DailyBuyingStatistics findPurchaseStatisticsByDate(LocalDate date) throws NoDailyStatisticsException;
    List<HourlyBuyingStatistics> findStatisticsOfDaily(DailyBuyingStatistics dailyBuyingStatistics);
    HourlyBuyingStatistics findPurchaseStatisticsByDateAndHour(LocalDateTime localDateTime, int hour) throws NoHourlyStatisticsException, NoDailyStatisticsException;
    HourlyBuyingStatistics findPurchaseStatisticsByHour(DailyBuyingStatistics dailyStatistics, int hour) throws NoHourlyStatisticsException, NoDailyStatisticsException;
}
