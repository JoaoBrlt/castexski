package fr.unice.polytech.isa.monitoring.components;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.unice.polytech.isa.common.entities.resort.AlertLevel;
import fr.unice.polytech.isa.common.entities.resort.DoubleSkiLift;
import fr.unice.polytech.isa.common.entities.resort.Resort;
import fr.unice.polytech.isa.common.entities.resort.SkiLift;
import fr.unice.polytech.isa.common.entities.notifications.Email;
import fr.unice.polytech.isa.notifications.exceptions.ExternalEmailServiceException;
import fr.unice.polytech.isa.notifications.interfaces.EmailNotification;
import fr.unice.polytech.isa.resort.interfaces.ResortFinder;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@Startup
@Singleton
public class LiveAlertBean implements Serializable {
    @EJB
    ResortFinder resortFinder;
    @EJB
    EmailNotification emailNotification;
    public static final String emailService = "noreply@castexski.org";
    public static final String subjectAlert = "LIVE ALERT: ";
    private int TEN_SECONDS = 10;
    private static final Logger logger = Logger.getLogger(Logger.class.getName());
    @PersistenceContext
    private EntityManager entityManager;


    @Schedule(second="*/20", minute = "*", hour = "*")
    public void doubleSwipeMonitoring() throws JsonProcessingException, ExternalEmailServiceException {
        for (Resort r : resortFinder.findAllResorts()) {
            r = entityManager.merge(r);
            for (SkiLift skiLift : r.getLifts()) {
                skiLift = entityManager.merge(skiLift);
                if (skiLift.isHasDoubleBadgeTerminal()) {
                    if (((DoubleSkiLift) skiLift).getStatus().equals(AlertLevel.BUSY)){
                        logger.log(Level.INFO, "SKI LIFT " + skiLift.getName() + " IS BUSY.");
                        emailNotification.sendEmail(generateBusyDoubleSkiLiftEmail((DoubleSkiLift) skiLift, r));
                        ((DoubleSkiLift) skiLift).resetWaiting();
                    }
                }
            }
        }
    }

    public Email generateBusyDoubleSkiLiftEmail(DoubleSkiLift doubleSkiLift, Resort resort){
        String body = "************* THIS MESSAGE WAS AUTOMATICALLY GENERATED *************\n" +
            "Ski Lift " + doubleSkiLift.getName() + " IS CURRENTLY " + doubleSkiLift.getStatus().name() + ".\n" +
            "The time passage is currently " + (int) ((100 * doubleSkiLift.getAverageWait().toMillis() / (doubleSkiLift.getAverageLimit().toMillis() == 0 ? TEN_SECONDS : doubleSkiLift.getAverageLimit().toMillis())) - 100) + "% higher than the limit.\n" +
            "LIMIT: " + formatDuration(doubleSkiLift.getAverageLimit().toMillis())+ "\n" +
            "CURRENT WAITING TIME: " + formatDuration(doubleSkiLift.getAverageWait().toMillis());
        return new Email(emailService, resort.getResortEmail(), subjectAlert + doubleSkiLift.getName() + " IS BUSY", body);
    }

    public String formatDuration(long milliseconds){
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(milliseconds), TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }

}
