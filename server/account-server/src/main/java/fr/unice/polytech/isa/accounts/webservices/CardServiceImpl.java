package fr.unice.polytech.isa.accounts.webservices;

import fr.unice.polytech.isa.accounts.exceptions.CardNotFoundException;
import fr.unice.polytech.isa.accounts.exceptions.CustomerNotFoundException;
import fr.unice.polytech.isa.accounts.interfaces.CardFinder;
import fr.unice.polytech.isa.accounts.interfaces.CardRegistration;
import fr.unice.polytech.isa.accounts.interfaces.CustomerCardLinker;
import fr.unice.polytech.isa.common.entities.items.Card;
import fr.unice.polytech.isa.common.exceptions.PassNotFoundException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@WebService(targetNamespace = "http://www.polytech.unice.fr/isa/castexski/card")
@Stateless(name = "CardWS")
public class CardServiceImpl implements CardService{

    @EJB private CardRegistration registration;
    @EJB private CardFinder cardFinder;
    @EJB private CustomerCardLinker cardLinker;

    @Override
    public void linkPhysicalCard(String cardId, String physicalId) throws CardNotFoundException {
        Card card = cardFinder.findCardById(cardId);
        registration.updatePhysicalId(card,physicalId);
    }

    @Override
    public void linkPassToCardOnline(String email, String physicalCardId, String passId) throws CardNotFoundException, PassNotFoundException, CustomerNotFoundException {
        cardLinker.linkPassToCardOnline(email, physicalCardId, passId);
    }

    @Override
    public List<String> getCardsNotPhysicallyLinked(String email) throws CustomerNotFoundException {
        return cardFinder.findCardsNotPhysicallyLinked(email).stream().map(Card::getId).collect(Collectors.toList());
    }

    @Override
    public List<String> getCardsPhysicallyLinked(String email) throws CustomerNotFoundException {
        return cardFinder.findCardsPhysicallyLinked(email).stream().map(Card::getId).collect(Collectors.toList());
    }

    @Override
    public String getCardIdByPhysicalId(String physicalId) throws CardNotFoundException {
        return cardFinder.findCardByPhysicalId(physicalId).getId();
    }

    @Override
    public String getCardNameById(String cardId) throws CardNotFoundException {
        return cardFinder.findCardById(cardId).getType().getName();
    }

    @Override
    public String getCardTypeById(String cardId) throws CardNotFoundException {
        return cardFinder.findCardById(cardId).getType().getType().name();
    }

    @Override
    public double getCardPriceById(String cardId) throws CardNotFoundException {
        return cardFinder.findCardById(cardId).getType().getPrice();
    }

    @Override
    public boolean isCardLinkedWPassById(String cardId) throws CardNotFoundException {
        return Objects.nonNull(cardFinder.findCardById(cardId).getPass());
    }

    @Override
    public boolean isCardPhysicallyLinkedById(String cardId) throws CardNotFoundException {
        return Objects.nonNull(cardFinder.findCardById(cardId).getPhysicalId());
    }

    @Override
    public String getPhysicalCardIdById(String cardId) throws CardNotFoundException {
        return cardFinder.findCardById(cardId).getPhysicalId();
    }

    @Override
    public String getLinkedPassIdByCardId(String cardId) throws CardNotFoundException {
        return cardFinder.findCardById(cardId).getPass().getId();
    }

}
