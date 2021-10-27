package fr.unice.polytech.isa.resort.webservices;

import fr.unice.polytech.isa.accounts.exceptions.CardNotFoundException;
import fr.unice.polytech.isa.accounts.interfaces.CardFinder;
import fr.unice.polytech.isa.common.entities.items.Card;
import fr.unice.polytech.isa.common.entities.resort.SkiLift;
import fr.unice.polytech.isa.resort.exceptions.SkiLiftNotFoundException;
import fr.unice.polytech.isa.resort.interfaces.CardChecker;
import fr.unice.polytech.isa.resort.interfaces.SkiLiftFinder;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;

@WebService(targetNamespace = "http://www.polytech.unice.fr/isa/castexski/accesschecking")
@Stateless(name = "AccessCheckingWS")
public class AccessCheckingServiceImpl implements AccessCheckingService {
    @EJB
    private CardChecker controller;

    @EJB
    private SkiLiftFinder finder;

    @EJB
    private CardFinder cardFinder;

    @Override
    public boolean checkCard(String liftId, String physicalCardId) throws SkiLiftNotFoundException, CardNotFoundException {
        SkiLift lift = finder.findById(liftId);
        Card card = cardFinder.findCardByPhysicalId(physicalCardId);
        return controller.checkCard(lift, card);
    }
}
