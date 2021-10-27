package fr.unice.polytech.isa.statistics.interfaces.purchases;

import javax.ejb.Local;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Local
public interface PurchaseStatisticsRegistration {
    void addNewDailyBuyingStatistics(LocalDate localDate);
    void addCurrentDailyBuyingStatistics();
    void addNewHourlyBuyingStatistics(LocalDateTime localDateTime) throws Exception;
    void addCurrentHourlyBuyingStatistics() throws Exception;
}
