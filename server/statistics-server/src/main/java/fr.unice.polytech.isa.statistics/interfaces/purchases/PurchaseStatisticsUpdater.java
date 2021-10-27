package fr.unice.polytech.isa.statistics.interfaces.purchases;

import javax.ejb.Local;
import java.time.LocalDateTime;

@Local
public interface PurchaseStatisticsUpdater {
    void addPassWithDate(String passName, int quantity, boolean isChildPass, LocalDateTime dateTime) throws Exception;
    void addPass(String passName, int quantity, boolean isChildPass) throws Exception;
    void addCardWithDate(int quantity, LocalDateTime dateTime) throws Exception;
    void addCard(int quantity) throws Exception;
    void addSuperCartexWithDate(int quantity,LocalDateTime dateTime) throws Exception;
    void addSuperCartex(int quantity) throws Exception;
}
