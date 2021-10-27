package fr.unice.polytech.isa.statistics.business;

import arquillian.AbstractStatisticsTest;
import fr.unice.polytech.isa.common.entities.resort.AlertLevel;
import fr.unice.polytech.isa.common.entities.resort.DoubleSkiLift;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.transaction.UserTransaction;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;


@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class DoubleSkiLiftTest extends AbstractStatisticsTest {


    @Inject private UserTransaction utx;
    DoubleSkiLift doubleSkiLift;
    int ONE_MINUTE = 1;
    int TWO_MINUTE = 2;
    int THREE_MINUTES = 3;
    int FORTY_FIVE_SECONDS = 45;
    int THIRTY_SECONDS = 30;
    int TWENTY_SECONDS = 20;
    boolean LIFT_OPEN = true;
    LocalDateTime DATE_01012022_10AM = LocalDateTime.of(2022, 1, 1, 10, 0);
    LocalDateTime DATE_01012022_10_01AM = LocalDateTime.of(2022, 1, 1, 10, 1);
    LocalDateTime DATE_01012022_10_02AM = LocalDateTime.of(2022, 1, 1, 10, 2);
    LocalDateTime DATE_01012022_10_05AM = LocalDateTime.of(2022, 1, 1, 10, 5);
    @Before
    public void setUpContext() throws Exception {
        settingUpResort();
        String resortId = getResortId();
        settingUpSkiLift(resortId,SKI_LIFT_NAME);
        settingUpSkiLift(resortId,SKI_LIFT_NAME_2);
        doubleSkiLift = new DoubleSkiLift(findResortById(resortId), Duration.ofMinutes(TWO_MINUTE), LIFT_OPEN, DOUBLE_SKI_LIFT_NAME);
    }

    @Test
    public void swipeTerminal(){
        assertEquals(AlertLevel.NORMAL, doubleSkiLift.getStatus());
        assertEquals(Duration.ZERO, doubleSkiLift.getAverageWait());
        doubleSkiLift.swipeTerminal(PHYSICAL_CARD_ID, DATE_01012022_10AM);
        assertEquals(Duration.ZERO, doubleSkiLift.getAverageWait());
        doubleSkiLift.swipeTerminal(PHYSICAL_CARD_ID, DATE_01012022_10_01AM);
        assertEquals(Duration.ofMinutes(ONE_MINUTE), doubleSkiLift.getAverageWait());
        assertEquals(AlertLevel.NORMAL, doubleSkiLift.getStatus());
        doubleSkiLift.swipeTerminal(PHYSICAL_CARD_ID, DATE_01012022_10AM);
        assertEquals(Duration.ofMinutes(ONE_MINUTE), doubleSkiLift.getAverageWait());
        assertEquals(AlertLevel.NORMAL, doubleSkiLift.getStatus());
        doubleSkiLift.swipeTerminal(PHYSICAL_CARD_ID, DATE_01012022_10_02AM);
        assertEquals(Duration.ofMinutes(ONE_MINUTE).plusSeconds(THIRTY_SECONDS), doubleSkiLift.getAverageWait());
        assertEquals(AlertLevel.NORMAL, doubleSkiLift.getStatus());
        doubleSkiLift.swipeTerminal(PHYSICAL_CARD_ID, DATE_01012022_10_01AM);
        doubleSkiLift.swipeTerminal(PHYSICAL_CARD_ID, DATE_01012022_10_05AM);
        assertEquals(Duration.ofMinutes(TWO_MINUTE).plusSeconds(TWENTY_SECONDS), doubleSkiLift.getAverageWait());
        assertEquals(AlertLevel.BUSY, doubleSkiLift.getStatus());
        doubleSkiLift.swipeTerminal(PHYSICAL_CARD_ID, DATE_01012022_10_05AM);
        doubleSkiLift.swipeTerminal(PHYSICAL_CARD_ID, DATE_01012022_10_05AM);
        assertEquals(Duration.ofMinutes(ONE_MINUTE).plusSeconds(FORTY_FIVE_SECONDS), doubleSkiLift.getAverageWait());
        assertEquals(AlertLevel.NORMAL, doubleSkiLift.getStatus());
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


}
