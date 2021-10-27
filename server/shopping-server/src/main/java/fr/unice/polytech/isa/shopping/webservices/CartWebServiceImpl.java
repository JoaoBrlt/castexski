package fr.unice.polytech.isa.shopping.webservices;

import fr.unice.polytech.isa.accounts.exceptions.CustomerNotFoundException;
import fr.unice.polytech.isa.accounts.interfaces.CustomerFinder;
import fr.unice.polytech.isa.common.entities.accounts.Customer;
import fr.unice.polytech.isa.common.entities.shopping.catalog.Item;
import fr.unice.polytech.isa.common.entities.items.ItemTypeName;
import fr.unice.polytech.isa.common.entities.shopping.catalog.PassType;
import fr.unice.polytech.isa.common.exceptions.*;
import fr.unice.polytech.isa.shopping.interfaces.CartModifier;
import fr.unice.polytech.isa.shopping.interfaces.CartProcessor;
import fr.unice.polytech.isa.shopping.interfaces.CatalogExplorer;
import fr.unice.polytech.isa.shopping.interfaces.CreditCardRegistration;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.persistence.NoResultException;
import java.time.Duration;
import java.time.YearMonth;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@WebService(targetNamespace = "http://www.polytech.unice.fr/isa/castexski/shopping")
@Stateless(name = "CartWS")
public class CartWebServiceImpl implements CartWebService {
    @EJB
    private CartModifier modifier;

    @EJB
    private CustomerFinder customerFinder;

    @EJB
    private CartProcessor processor;

    @EJB
    private CreditCardRegistration creditCardReg;

    @EJB
    private CatalogExplorer explorer;

    @Override
    public void addCardToCustomerCart(String email, String name, boolean isSuperCartex, int quantity) throws NullQuantityException, NoResultException, UnknownCatalogEntryException {
        modifier.addToCart(readCustomer(email), explorer.pickCard(name, isSuperCartex, quantity));
    }

    @Override
    public void addPassToCustomerCart(String email, String name, String durationInDays, boolean isChildrenPass, int quantity) throws NullQuantityException, NoResultException, UnknownCatalogEntryException {
        modifier.addToCart(readCustomer(email), explorer.pickPass(name, Duration.parse(durationInDays), isChildrenPass, quantity));
    }

    @Override
    public void removePassFromCustomerCart(String email,  String name, String durationInDays, boolean isChildrenPass, int quantity) throws ItemNotFoundException, NullQuantityException, UnknownCatalogEntryException {
        modifier.removeFromCart(readCustomer(email), explorer.pickPass(name, Duration.parse(durationInDays), isChildrenPass, quantity));
    }


    @Override
    public void removeCardFromCustomerCart(String email, String name, boolean isSuperCartex, int quantity) throws NullQuantityException, UnknownCatalogEntryException, ItemNotFoundException {
        modifier.removeFromCart(readCustomer(email), explorer.pickCard(name, isSuperCartex, quantity));
    }



    @Override
    public void addCreditCard(String email, String name, String number, String securityCode, String expiryMonth, String expiryYear, boolean saveChoice) {
        creditCardReg.creditCardRegistry(readCustomer(email), name, number, securityCode,
            YearMonth.of(Integer.parseInt(expiryYear), Integer.parseInt(expiryMonth)), saveChoice);
    }

    @Override
    public void validateCustomerCart(String email) throws EmptyCartException, NoCreditCardException, PaymentException, NoResultException, CustomerNotFoundException {
        processor.validateCart(readCustomer(email));
    }

    @Override
    public Set<Integer> displayCustomerCart(String email) {
        return processor.displayContents(readCustomer(email)).stream().map(Item::getId).collect(Collectors.toSet());
    }

    /**
     * Information about an item
     */


    @Override
    public String getItemNameById(String email, int id) throws ItemNotFoundException {
        return readItem(email, id).getType().getName();
    }

    @Override
    public double getItemPriceById(String email, int id) throws ItemNotFoundException {
        return readItem(email, id).getType().getPrice();
    }

    @Override
    public double getTotalPriceById(String email, int id) throws ItemNotFoundException {
        return readItem(email, id).getTotalPrice();
    }

    @Override
    public String getItemTypeById(String email, int id) throws ItemNotFoundException {
        return readItem(email, id).getType().getType().name();
    }

    @Override
    public int getItemQuantityById(String email, int id) throws ItemNotFoundException {
        return readItem(email, id).getQuantity();
    }

    @Override
    public String getPassDurationById(String email, int id) throws ItemNotFoundException {
        Item item = readItem(email, id);
        if (item.getType().getType().equals(ItemTypeName.PASS)){
            return ((PassType) item.getType()).getMaxDurationRaw();
        } else {
            throw new ItemNotFoundException();
        }
    }

    @Override
    public boolean isChildrenPassById(String email, int id) throws ItemNotFoundException {
        Item item = readItem(email, id);
        if (item.getType().getType().equals(ItemTypeName.PASS)){
            return ((PassType) item.getType()).isChildPass();
        } else {
            throw new ItemNotFoundException(email);
        }    }

    @Override
    public boolean isSuperCartexById(String email, int id) throws ItemNotFoundException {
        return readItem(email, id).getType().getType().equals(ItemTypeName.SUPERCARTEX);
    }

    @Override
    public boolean isPassById(String email, int id) throws ItemNotFoundException {
        return readItem(email, id).getType().getType().equals(ItemTypeName.PASS);
    }

    @Override
    public boolean isCardById(String email, int id) throws ItemNotFoundException {
        return readItem(email, id).getType().getType().equals(ItemTypeName.CARD);
    }


    Item readItem(String email, int id) throws ItemNotFoundException {
        Optional<Item> item = processor.findItemById(readCustomer(email), id);
        if (item.isPresent()){
            return item.get();
        }
        throw new ItemNotFoundException(email);
    }
    Customer readCustomer(String email) throws NoResultException {
        try {
            return customerFinder.findByMail(email);
        } catch (CustomerNotFoundException e){
            throw new NoResultException("No result for the email \" " + email + "\"");
        }
    }
}
