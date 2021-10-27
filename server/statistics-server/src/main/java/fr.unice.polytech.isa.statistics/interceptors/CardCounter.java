package fr.unice.polytech.isa.statistics.interceptors;

import fr.unice.polytech.isa.common.entities.items.Card;
import fr.unice.polytech.isa.common.entities.resort.DoubleSkiLift;
import fr.unice.polytech.isa.common.entities.resort.SkiLift;
import fr.unice.polytech.isa.statistics.interfaces.presence.PresenceStatisticsUpdater;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.time.LocalDateTime;

public class CardCounter implements Serializable {
    @EJB
    private PresenceStatisticsUpdater updater;

    @PersistenceContext
    private EntityManager entityManager;

    @AroundInvoke
    public Object interceptAccess(InvocationContext ctx) throws Exception {
        //We retrieve the parameters useful for our presence statistics
        SkiLift skiLift = (SkiLift) ctx.getParameters()[0];
        Card card = (Card) ctx.getParameters()[1];
        //We execute the intercepted method
        boolean checkCard = (boolean) ctx.proceed();

        //If the access was granted, we can add the card to the statistics
        if (checkCard) {
            skiLift = entityManager.merge(skiLift);
            if (skiLift.isHasDoubleBadgeTerminal()) {
                // if its the first swipe on a double ski lift, we can add the card to the statistics
                if (((DoubleSkiLift) skiLift).swipeTerminal(card.getPhysicalId(), LocalDateTime.now())) {
                    updater.addCardToStatistics(skiLift, card.getId());
                }
            } else {
                updater.addCardToStatistics(skiLift, card.getId());
            }
        }
        return checkCard;
    }
}
