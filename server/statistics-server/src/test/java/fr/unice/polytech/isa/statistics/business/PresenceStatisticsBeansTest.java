package fr.unice.polytech.isa.statistics.business;

import arquillian.AbstractStatisticsTest;
import fr.unice.polytech.isa.common.entities.resort.SkiLift;
import fr.unice.polytech.isa.common.entities.statistics.DailyPresenceStatistics;
import fr.unice.polytech.isa.common.entities.statistics.HourlyPresenceStatistics;
import fr.unice.polytech.isa.statistics.exceptions.NoDailyStatisticsException;
import fr.unice.polytech.isa.statistics.interfaces.presence.PresenceStatisticsFinder;
import fr.unice.polytech.isa.statistics.interfaces.presence.PresenceStatisticsRegistration;
import fr.unice.polytech.isa.statistics.interfaces.presence.PresenceStatisticsUpdater;
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
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class PresenceStatisticsBeansTest extends AbstractStatisticsTest {

    @EJB private PresenceStatisticsRegistration registry;
    @EJB private PresenceStatisticsFinder finder;
    @EJB private PresenceStatisticsUpdater updater;

    @PersistenceContext private EntityManager entityManager;
    @Inject private UserTransaction utx;

    @Rule public ExpectedException exceptionRule = ExpectedException.none();

    private final LocalDate date = LocalDate.of(dateTime.getYear(),dateTime.getMonthValue(),dateTime.getDayOfMonth());

    private final LocalDateTime dateTime2 = LocalDateTime.of(2021,1,1,startingHour,40);
    private final LocalDate date2 = LocalDate.of(dateTime2.getYear(),dateTime2.getMonthValue(),dateTime2.getDayOfMonth());

    private final LocalDateTime dateTime3 = LocalDateTime.of(2021,1,1,startingHour+1,30);
    private final LocalDate date3 = LocalDate.of(dateTime3.getYear(),dateTime3.getMonthValue(),dateTime3.getDayOfMonth());

    @Before
    public void setUpContext() throws Exception {
        settingUpResort();
        settingUpSkiLift();
    }

    @After
    public void cleaningUp() throws Exception {
        utx.begin();
        try {
            deleteResort(getResortId());
        } catch (Exception e) {
            //There is nothing to delete
        }
        utx.commit();
    }

    @Test
    public void addDailyStatistics() throws Exception {
        SkiLift retrievedSkiLift = findSkiLiftById(getSkiLiftId(getResortId()));
        registry.addNewDailyPresenceStatistics(retrievedSkiLift,date);
        DailyPresenceStatistics dailyStatistics1 = finder.findPresenceStatisticsOfSkiLiftByDate(date,retrievedSkiLift);
        assertTrue(dailyStatistics1.getHourlyPresenceStatistics().isEmpty());
        LocalDate dateBis = LocalDate.of(date.getYear(),date.getMonthValue(),date.getDayOfMonth());
        assertEquals(dateBis,date);
        assertEquals(dailyStatistics1.getDate(), date);
        assertEquals(retrievedSkiLift.getDailyStatistics().size(),1);
    }

    @Test
    public void findDailyBySkiLiftAndResortName() throws Exception {
        SkiLift retrievedSkiLift = findSkiLiftById(getSkiLiftId(getResortId()));
        registry.addNewDailyPresenceStatistics(retrievedSkiLift, date);
        DailyPresenceStatistics daily = finder.findPresenceStatisticsBySkiLiftAndResortNameAndDate(SKI_LIFT_NAME,RESORT_NAME,date);
        assertEquals(daily.getResortName(),RESORT_NAME);
        assertEquals(daily.getSkiLiftName(),SKI_LIFT_NAME);
        assertTrue(daily.getHourlyPresenceStatistics().isEmpty());

    }

    @Test
    public void addDailyStatisticsOnceForSameDate() throws Exception {
        SkiLift retrievedSkiLift = findSkiLiftById(getSkiLiftId(getResortId()));
        registry.addNewDailyPresenceStatistics(retrievedSkiLift, date);
        registry.addNewDailyPresenceStatistics(retrievedSkiLift, date);
        assertEquals(retrievedSkiLift.getDailyStatistics().size(),1);
    }

    @Test
    public void addHourlyToDaily() throws Exception {
        SkiLift retrievedSkiLift = findSkiLiftById(getSkiLiftId(getResortId()));
        registry.addNewDailyPresenceStatistics(retrievedSkiLift, date);
        registry.addNewHourlyPresenceStatistics(retrievedSkiLift, dateTime);
        DailyPresenceStatistics dailyStatistics1 = finder.findPresenceStatisticsOfSkiLiftByDate(date,retrievedSkiLift);
        assertEquals(dailyStatistics1.getHourlyPresenceStatistics().size(), 1);
        assertEquals(retrievedSkiLift.getDailyStatistics().size(),1);
    }

    @Test(expected = NoDailyStatisticsException.class)
    public void addHourlyWhenNoDaily() throws Exception {
        SkiLift retrievedSkiLift = findSkiLiftById(getSkiLiftId(getResortId()));
        registry.addNewHourlyPresenceStatistics(retrievedSkiLift, dateTime);
        exceptionRule.expect(NoDailyStatisticsException.class);
    }

    @Test
    public void updateWithNewDaily() throws Exception {
        SkiLift retrievedSkiLift = findSkiLiftById(getSkiLiftId(getResortId()));
        updater.addCardToStatisticsWithDate(retrievedSkiLift,"card", dateTime);
        updater.addCardToStatisticsWithDate(retrievedSkiLift,"card", dateTime3);
        assertEquals(retrievedSkiLift.getDailyStatistics().size(),1);
        List<DailyPresenceStatistics> dailyStatistics = finder.findPresenceStatisticsByDate(date);
        assertEquals(dailyStatistics.size(),1);
        DailyPresenceStatistics dailyStatistics1 = finder.findPresenceStatisticsOfSkiLiftByDate(date,retrievedSkiLift);
        assertEquals(dailyStatistics1.getHourlyPresenceStatistics().size(),2);
    }

    @Test
    public void updateWithSameCardTwiceSameHour() throws Exception {
        SkiLift retrievedSkiLift = findSkiLiftById(getSkiLiftId(getResortId()));
        updater.addCardToStatisticsWithDate(retrievedSkiLift,"card", dateTime);
        updater.addCardToStatisticsWithDate(retrievedSkiLift,"card", dateTime2);
        assertEquals(retrievedSkiLift.getDailyStatistics().size(),1);
        List<DailyPresenceStatistics> dailyStatistics = finder.findPresenceStatisticsByDate(date);
        assertEquals(dailyStatistics.size(),1);
        DailyPresenceStatistics dailyStatistics1 = finder.findPresenceStatisticsOfSkiLiftByDate(date,retrievedSkiLift);
        assertEquals(dailyStatistics1.getHourlyPresenceStatistics().size(),1);
        HourlyPresenceStatistics hourlyStatistics = finder.findPresenceStatisticsByHour(dailyStatistics1,startingHour);
        assertEquals(hourlyStatistics.getBeepedCards().size(),1);
    }

    @Test
    public void updateWithDifferentCardsSameHour() throws Exception {
        SkiLift retrievedSkiLift = findSkiLiftById(getSkiLiftId(getResortId()));
        updater.addCardToStatisticsWithDate(retrievedSkiLift,"card", dateTime);
        updater.addCardToStatisticsWithDate(retrievedSkiLift,"card2", dateTime2);
        assertEquals(retrievedSkiLift.getDailyStatistics().size(),1);
        List<DailyPresenceStatistics> dailyStatistics = finder.findPresenceStatisticsByDate(date);
        assertEquals(dailyStatistics.size(),1);
        DailyPresenceStatistics dailyStatistics1 = finder.findPresenceStatisticsOfSkiLiftByDate(date,retrievedSkiLift);
        assertEquals(dailyStatistics1.getHourlyPresenceStatistics().size(),1);
        HourlyPresenceStatistics hourlyStatistics = finder.findPresenceStatisticsByHour(dailyStatistics1,startingHour);
        assertEquals(hourlyStatistics.getBeepedCards().size(),2);
    }

}
