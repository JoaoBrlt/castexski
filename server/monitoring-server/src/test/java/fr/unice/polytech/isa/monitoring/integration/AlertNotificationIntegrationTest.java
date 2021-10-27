package fr.unice.polytech.isa.monitoring.integration;

import arquillian.AbstractMonitoringTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import fr.unice.polytech.isa.common.entities.resort.AlertLevel;
import fr.unice.polytech.isa.common.entities.resort.DoubleSkiLift;
import fr.unice.polytech.isa.monitoring.components.LiveAlertBean;
import fr.unice.polytech.isa.notifications.exceptions.ExternalEmailServiceException;
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

import java.time.Duration;
import java.time.LocalDateTime;

import static fr.unice.polytech.isa.monitoring.MonitoringTestUtil.*;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class AlertNotificationIntegrationTest extends AbstractMonitoringTest {

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

    @Before
    public void setUp() throws Exception {
        setUpCompleteResort();

    }

    private void setUpCompleteResort() throws Exception {
        resortRegister.registerResort(RESORT_NAME, RESORT_EMAIL, CLOSE, RESORT_CITY_NAME);
        String resortId = resortFinder.findByName(RESORT_NAME).getId();
        skiLiftRegister.registerDoubleSkiLift(resortId, SKI_LIFT_NAME, OPEN, Duration.ofMinutes(1).toString());
    }

    void simulatingDoubleSwipe(Duration durationBetweenSwipes, String physicalCard, DoubleSkiLift doubleSkiLift){
        doubleSkiLift = entityManager.merge(doubleSkiLift);
        doubleSkiLift.swipeTerminal(physicalCard, LocalDateTime.now());
        doubleSkiLift.swipeTerminal(physicalCard, doubleSkiLift.getWaitingForSecondSwipe().get(fr.unice.polytech.isa.monitoring.MonitoringTestUtil.PHYSICAL_ID1).plus(durationBetweenSwipes));
    }

    @After
    public void cleaningUp() {
        try {
            resortRegister.deleteResort(resortFinder.findByName(RESORT_NAME).getId());
        } catch (Exception e) {
            //Shouldn't happen
        }
    }

    @Test
    public void doubleSwipeMonitoring() throws JsonProcessingException, ExternalEmailServiceException, ResortNotFoundException, SkiLiftNotFoundException {
        DoubleSkiLift doubleSkiLift = (DoubleSkiLift) skiLiftFinder.findByName(resortFinder.findByName(RESORT_NAME).getId(), SKI_LIFT_NAME);
        simulatingDoubleSwipe(Duration.ofSeconds(20), PHYSICAL_ID1, doubleSkiLift);
        liveAlertBean.doubleSwipeMonitoring();
        doubleSkiLift = entityManager.merge(doubleSkiLift);
        assertEquals(Duration.ofSeconds(20), doubleSkiLift.getAverageWait());
        simulatingDoubleSwipe(Duration.ofSeconds(120), PHYSICAL_ID1, doubleSkiLift);
        assertEquals(AlertLevel.BUSY,doubleSkiLift.getStatus());
        liveAlertBean.doubleSwipeMonitoring();
        //An alert has been sent to the resort controller, then the busy status of the ski lift has been reset to Normal level
        assertEquals(AlertLevel.NORMAL,doubleSkiLift.getStatus());
    }


}
