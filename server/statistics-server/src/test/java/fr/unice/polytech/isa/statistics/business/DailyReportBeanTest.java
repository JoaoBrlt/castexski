package fr.unice.polytech.isa.statistics.business;

import arquillian.AbstractStatisticsTest;
import fr.unice.polytech.isa.common.entities.resort.SkiLift;
import fr.unice.polytech.isa.statistics.interfaces.DailyReportCreator;
import fr.unice.polytech.isa.statistics.interfaces.presence.PresenceStatisticsUpdater;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.transaction.UserTransaction;
import java.time.LocalDate;


@RunWith(Arquillian.class)
public class DailyReportBeanTest extends AbstractStatisticsTest {

    @EJB private DailyReportCreator dailyReportBean;
    @EJB private PresenceStatisticsUpdater presenceStatisticsUpdater;

    @Inject private UserTransaction utx;
    private final LocalDate date = LocalDate.of(dateTime.getYear(),dateTime.getMonthValue(),dateTime.getDayOfMonth());


    @Before
    public void setUpContext() throws Exception {
        settingUpResort();
        String resortId = getResortId();
        settingUpSkiLift(resortId,SKI_LIFT_NAME);
        settingUpSkiLift(resortId,SKI_LIFT_NAME_2);
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
    public void tryingEmptyDaily(){
        System.out.println(dailyReportBean.constructPresenceReportOfDate(RESORT_NAME,LocalDate.now()));
    }

    @Test
    public void tryingDailyReport() throws Exception {
        SkiLift retrievedSkiLift = findSkiLiftById(getSkiLiftId(getResortId(),SKI_LIFT_NAME));
        SkiLift retrievedSkiLift2 = findSkiLiftById(getSkiLiftId(getResortId(),SKI_LIFT_NAME_2));

        presenceStatisticsUpdater.addCardToStatisticsWithDate(retrievedSkiLift,PHYSICAL_CARD_ID,dateTime);
        presenceStatisticsUpdater.addCardToStatisticsWithDate(retrievedSkiLift2,PHYSICAL_CARD_ID_2,dateTime);

        System.out.println(dailyReportBean.constructPresenceReportOfDate(RESORT_NAME,date));

    }

}
