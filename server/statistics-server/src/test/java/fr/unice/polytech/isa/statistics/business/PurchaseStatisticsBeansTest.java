package fr.unice.polytech.isa.statistics.business;

import arquillian.AbstractStatisticsTest;
import fr.unice.polytech.isa.common.entities.statistics.DailyBuyingStatistics;
import fr.unice.polytech.isa.common.entities.statistics.HourlyBuyingStatistics;
import fr.unice.polytech.isa.statistics.exceptions.NoDailyStatisticsException;
import fr.unice.polytech.isa.statistics.interfaces.purchases.PurchaseStatisticsFinder;
import fr.unice.polytech.isa.statistics.interfaces.purchases.PurchaseStatisticsRegistration;
import fr.unice.polytech.isa.statistics.interfaces.purchases.PurchaseStatisticsUpdater;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class PurchaseStatisticsBeansTest extends AbstractStatisticsTest {

    @EJB
    private PurchaseStatisticsRegistration registry;
    @EJB private PurchaseStatisticsFinder finder;
    @EJB private PurchaseStatisticsUpdater updater;

    @PersistenceContext
    private EntityManager entityManager;
    @Inject
    private UserTransaction utx;

    private final LocalDate date = LocalDate.of(2021,1,1);
    private final LocalDateTime dateTime = LocalDateTime.of(date.getYear(),date.getMonthValue(),date.getDayOfMonth(),12,30);
    private final LocalDateTime dateTime2 = LocalDateTime.of(date.getYear(),date.getMonthValue(),date.getDayOfMonth(),13,30);

    private final LocalDate date2 = LocalDate.of(2021,1,1);
    private final LocalDate date3 = LocalDate.of(2021,2,1);

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void setUpContext() {

    }

    @After
    public void cleanUp() throws Exception {
        utx.begin();
        try {
            DailyBuyingStatistics d1 = finder.findPurchaseStatisticsByDate(date);
            DailyBuyingStatistics dbis = entityManager.merge(d1);
            entityManager.remove(dbis);
        } catch (NoDailyStatisticsException e){
            //Nothing to remove
        }
        utx.commit();
    }

    @Test
    public void addingNewDaily() throws Exception {
        registry.addNewDailyBuyingStatistics(date);
        DailyBuyingStatistics dailyBuyingStatistics = finder.findPurchaseStatisticsByDate(date);
        assertTrue(dailyBuyingStatistics.getHourlyBuyingStatistics().isEmpty());
    }

    @Test(expected = NoDailyStatisticsException.class)
    public void addingNewDailyGettingAnotherOneThrowsException() throws NoDailyStatisticsException {
        registry.addNewDailyBuyingStatistics(date);
        finder.findPurchaseStatisticsByDate(date3);
        exceptionRule.expect(NoDailyStatisticsException.class);
    }

    @Test
    public void addingNewHourlyWithDaily() throws Exception {
        registry.addNewDailyBuyingStatistics(date);
        registry.addNewHourlyBuyingStatistics(dateTime);
        DailyBuyingStatistics dailyBuyingStatistics = finder.findPurchaseStatisticsByDate(date);
        assertEquals(dailyBuyingStatistics.getHourlyBuyingStatistics().size(),1);
    }

    @Test(expected = NoDailyStatisticsException.class)
    public void addingNewHourlyWithoutDailyThrowsException() throws Exception {
        registry.addNewHourlyBuyingStatistics(dateTime);
        exceptionRule.expect(NoDailyStatisticsException.class);
    }

    @Test
    public void updatingCard() throws Exception {
        updater.addCardWithDate(1,dateTime);
        HourlyBuyingStatistics hourlyBuyingStatistics = finder.findPurchaseStatisticsByDateAndHour(dateTime,dateTime.getHour());
        assertEquals(hourlyBuyingStatistics.getNbOfCards(),1);
    }

    @Test
    public void updatingSuperCartex() throws Exception {
        updater.addSuperCartexWithDate(2,dateTime);
        HourlyBuyingStatistics hourlyBuyingStatistics = finder.findPurchaseStatisticsByDateAndHour(dateTime,dateTime.getHour());
        assertEquals(hourlyBuyingStatistics.getNbOfCartex(),2);
        assertEquals(hourlyBuyingStatistics.getNbOfCards(),0);
    }

    @Test
    public void updatingPassNotChild() throws Exception {
        String name = "PassName";
        updater.addPassWithDate(name,2,false,dateTime);
        HourlyBuyingStatistics hourlyBuyingStatistics = finder.findPurchaseStatisticsByDateAndHour(dateTime,dateTime.getHour());
        assertEquals(hourlyBuyingStatistics.getNbOfPasses().size(),1);
        int passQuantity = hourlyBuyingStatistics.getNbOfPasses().get(name);
        assertEquals(passQuantity,2);
        assertEquals(hourlyBuyingStatistics.getNbOfChildPasses(),0);
    }

    @Test
    public void updatingPassChild() throws Exception {
        String name = "PassName";
        updater.addPassWithDate(name,2,true,dateTime);
        HourlyBuyingStatistics hourlyBuyingStatistics = finder.findPurchaseStatisticsByDateAndHour(dateTime,dateTime.getHour());
        assertEquals(hourlyBuyingStatistics.getNbOfPasses().size(),1);
        int passQuantity = hourlyBuyingStatistics.getNbOfPasses().get(name);
        assertEquals(passQuantity,2);
        assertEquals(hourlyBuyingStatistics.getNbOfChildPasses(),2);
    }

    @Test
    public void addingPassesSameDayDifferentHour() throws Exception {
        String name = "PassName";
        updater.addPassWithDate(name,2,true,dateTime);
        updater.addPassWithDate(name,1,false,dateTime2);
        HourlyBuyingStatistics h1 = finder.findPurchaseStatisticsByDateAndHour(dateTime, dateTime.getHour());
        HourlyBuyingStatistics h2 = finder.findPurchaseStatisticsByDateAndHour(dateTime2, dateTime2.getHour());
        DailyBuyingStatistics d1 = finder.findPurchaseStatisticsByDate(date);
        assertEquals(d1.getHourlyBuyingStatistics().size(),2);
        assertEquals(h1.getNbOfChildPasses(),2);
        assertEquals(h1.getNbOfPasses().size(),1);
        assertEquals(h2.getNbOfChildPasses(),0);
        assertEquals(h2.getNbOfPasses().size(),1);
    }

}
