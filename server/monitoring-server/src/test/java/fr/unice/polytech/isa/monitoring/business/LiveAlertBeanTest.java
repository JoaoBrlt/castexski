package fr.unice.polytech.isa.monitoring.business;

import arquillian.AbstractMonitoringTest;
import fr.unice.polytech.isa.common.entities.resort.DoubleSkiLift;
import fr.unice.polytech.isa.common.entities.resort.Resort;
import fr.unice.polytech.isa.common.entities.notifications.Email;
import fr.unice.polytech.isa.monitoring.components.LiveAlertBean;
import fr.unice.polytech.isa.resort.exceptions.ResortNotFoundException;
import fr.unice.polytech.isa.resort.exceptions.SkiLiftNotFoundException;
import fr.unice.polytech.isa.resort.interfaces.ResortFinder;
import fr.unice.polytech.isa.resort.interfaces.ResortRegister;
import fr.unice.polytech.isa.resort.interfaces.SkiLiftFinder;
import fr.unice.polytech.isa.resort.interfaces.SkiLiftRegister;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import java.time.Duration;
import java.time.LocalDateTime;

import static fr.unice.polytech.isa.monitoring.MonitoringTestUtil.*;
import static fr.unice.polytech.isa.monitoring.components.LiveAlertBean.subjectAlert;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class LiveAlertBeanTest extends AbstractMonitoringTest {

    @EJB
    private ResortRegister resortRegister;
    @EJB
    private ResortFinder resortFinder;

    @EJB
    private SkiLiftRegister skiLiftRegister;
    @EJB
    private SkiLiftFinder skiLiftFinder;

    @PersistenceContext
    private EntityManager entityManager;
    @Inject
    private LiveAlertBean liveAlertBean;

    @Inject
    private UserTransaction utx;

    @Before
    public void setUp() throws Exception {
        setUpCompleteResort();
    }

    private void setUpCompleteResort() throws Exception {
        resortRegister.registerResort(RESORT_NAME, RESORT_EMAIL, CLOSE, RESORT_CITY_NAME);
        String resortId = resortFinder.findByName(RESORT_NAME).getId();
        skiLiftRegister.registerDoubleSkiLift(resortId, SKI_LIFT_NAME, OPEN, Duration.ofMinutes(1).toString());
    }

    @After
    public void cleaningUp() {
        try {
            utx.begin();
            resortRegister.deleteResort(resortFinder.findByName(RESORT_NAME).getId());
            utx.commit();
        } catch (Exception e) {
            //Shouldn't happen
        }
    }


    @Test
    public void generateBusyDoubleSkiLiftEmail() throws ResortNotFoundException, SkiLiftNotFoundException {
        Resort resort = resortFinder.findByName(RESORT_NAME);
        DoubleSkiLift skiLift = (DoubleSkiLift) skiLiftFinder.findByName(resort.getId(), SKI_LIFT_NAME);
        simulatingDoubleSwipe(Duration.ofMinutes(2), PHYSICAL_ID1, skiLift);
        String expectedFrom = LiveAlertBean.emailService;
        String expectedTo = resort.getResortEmail();
        String expectedSubject = subjectAlert + skiLift.getName() + " IS BUSY";
        String expectedBody = "************* THIS MESSAGE WAS AUTOMATICALLY GENERATED *************\n" +
            "Ski Lift " + skiLift.getName() + " IS CURRENTLY " + skiLift.getStatus().name() + ".\n" +
            "The time passage is currently " + 100 + "% higher than the limit.\n" +
            "LIMIT: " + liveAlertBean.formatDuration(skiLift.getAverageLimit().toMillis())+ "\n" +
            "CURRENT WAITING TIME: " + liveAlertBean.formatDuration(skiLift.getAverageWait().toMillis());
        Email expectedEmail = new Email(expectedFrom, expectedTo, expectedSubject, expectedBody);
        assertEquals(expectedEmail, liveAlertBean.generateBusyDoubleSkiLiftEmail(skiLift, resort));
    }

    void simulatingDoubleSwipe(Duration durationBetweenSwipes, String physicalCard, DoubleSkiLift doubleSkiLift){
        doubleSkiLift = entityManager.merge(doubleSkiLift);
        doubleSkiLift.swipeTerminal(physicalCard, LocalDateTime.now());
        doubleSkiLift.swipeTerminal(physicalCard, doubleSkiLift.getWaitingForSecondSwipe().get(fr.unice.polytech.isa.monitoring.MonitoringTestUtil.PHYSICAL_ID1).plus(durationBetweenSwipes));
    }
    @Test
    public void formatDuration() {
        assertEquals("01:20", liveAlertBean.formatDuration(Duration.ofSeconds(80).toMillis()));
        assertEquals("00:00", liveAlertBean.formatDuration(Duration.ofSeconds(0).toMillis()));
    }
}
