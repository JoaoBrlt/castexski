package fr.unice.polytech.isa.resort.components;

import fr.unice.polytech.isa.accounts.interfaces.PassRegistration;
import fr.unice.polytech.isa.common.entities.items.ItemTypeName;
import fr.unice.polytech.isa.common.entities.items.Card;
import fr.unice.polytech.isa.common.entities.items.Pass;
import fr.unice.polytech.isa.common.entities.items.SuperCartex;
import fr.unice.polytech.isa.common.entities.resort.LiftAccess;
import fr.unice.polytech.isa.common.entities.resort.SkiLift;
import fr.unice.polytech.isa.resort.interfaces.CardChecker;
import fr.unice.polytech.isa.shopping.interfaces.SuperCartexProcessor;
import fr.unice.polytech.isa.statistics.interceptors.CardCounter;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Stateless
public class AccessControllerBean implements CardChecker {
    @PersistenceContext
    private EntityManager entityManager;

    @EJB
    SuperCartexProcessor superCartexProcessor;

    @EJB
    PassRegistration passRegistration;

    @Override
    @Interceptors({CardCounter.class})
    public boolean checkCard(SkiLift lift, Card card) {
        card = entityManager.merge(card);
        if (!lift.isOpen() || !lift.getResort().isOpen()) {
            return false;
        }
        if (card.getType().getType().equals(ItemTypeName.SUPERCARTEX)) {
            return checkSuperCartex((SuperCartex) card);
            }
        Set<LiftAccess> accesses = lift.getResort().getAccesses();
        Card finalCard = card;
        Optional<LiftAccess> access = accesses.stream().filter(a ->
            a.getPassName().equals(finalCard.getPass().getType().getName())
        ).findFirst();
        if (access.isPresent()) {
            Optional<String> allowedLiftName = access.get().getAllowedLiftsIds().stream().filter(l ->
                l.equals(lift.getId())
            ).findFirst();
            if (allowedLiftName.isPresent()) {
                if(!card.getPass().isActivated()) {
                    passRegistration.activatePass(card.getPass());
                }
                return true;
            }
        }
        return false;
    }

    private boolean isValidSuperPass(Pass pass){
        return pass != null && Date.from(Instant.now()).before(pass.getEndDate());
    }

    private boolean checkSuperCartex(SuperCartex superCartex) {
        superCartex = entityManager.merge(superCartex);
        if (isValidSuperPass(superCartex.getPass())){
            superCartex.setLastSwipe(LocalDateTime.now());
            return true;
        } else {
            if (superCartex.getLastSwipe() == null || superCartex.getLastSwipe().isBefore(superCartex.getLastSwipe().minusDays(1))) {
                superCartex.setFirstSwipe(LocalDateTime.now());
            }
            boolean access = false;
            try {
                 access = superCartexProcessor.processSuperCartex(superCartex);
                 superCartex.setLastSwipe(LocalDateTime.now());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return access;
        }
    }
}
