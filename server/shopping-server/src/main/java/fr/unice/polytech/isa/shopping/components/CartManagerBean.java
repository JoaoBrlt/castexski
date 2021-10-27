package fr.unice.polytech.isa.shopping.components;

import fr.unice.polytech.isa.accounts.exceptions.CardNotFoundException;
import fr.unice.polytech.isa.accounts.exceptions.CustomerNotFoundException;
import fr.unice.polytech.isa.accounts.interfaces.CardRegistration;
import fr.unice.polytech.isa.accounts.interfaces.CustomerCardLinker;
import fr.unice.polytech.isa.accounts.interfaces.CustomerPassFinder;
import fr.unice.polytech.isa.accounts.interfaces.PassRegistration;
import fr.unice.polytech.isa.common.entities.accounts.Customer;
import fr.unice.polytech.isa.common.entities.shopping.catalog.Item;
import fr.unice.polytech.isa.common.entities.items.Pass;
import fr.unice.polytech.isa.common.entities.items.SuperCartex;
import fr.unice.polytech.isa.common.entities.shopping.Cart;
import fr.unice.polytech.isa.common.entities.shopping.CreditCard;
import fr.unice.polytech.isa.common.entities.shopping.Order;
import fr.unice.polytech.isa.common.exceptions.*;
import fr.unice.polytech.isa.payment.interfaces.PaymentProcessor;
import fr.unice.polytech.isa.shopping.interfaces.*;
import fr.unice.polytech.isa.statistics.interceptors.PurchaseCounter;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.*;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class CartManagerBean implements CartModifier, CartProcessor, CreditCardRegistration, SuperCartexProcessor {

    @EJB
    protected PaymentProcessor payment;
    @EJB
    protected CardRegistration cardRegistration;
    @EJB
    protected PassRegistration passRegistration;
    @EJB
    protected CatalogExplorer catalogExplorer;
    @EJB
    protected CustomerCardLinker customerCardLinker;
    @EJB
    protected SuperCartexDiscount superCartexDiscount;
    @EJB
    protected CustomerPassFinder passFinder;
    @PersistenceContext
    private EntityManager entityManager;
    private static final Logger log = Logger.getLogger(Logger.class.getName());
    private static final double FREE = 0.0;
    @Override
    public void addToCart(Customer customer, Item item) throws NullQuantityException {
        Cart cart = getCustomerCart(customer);
        Item finalItem = item;
        Optional<Item> itemCart = cart.getItems().stream().filter(i -> i.getType().equals(finalItem.getType())).findFirst();
        if (itemCart.isPresent()){
            cart.deleteItem(itemCart.get());
            item = new Item(item.getType(), item.getQuantity() + itemCart.get().getQuantity());
        }
        if (item.getQuantity() > 0) cart.addItem(new Item(item.getType(), item.getQuantity()));
        else {
            log.log(Level.FINEST, "the item " + item.getType().getName() + " wasn't added. The quantity must be greater than 0");
            throw new NullQuantityException(item.getType().getName());
        }
        log.log(Level.FINEST, "Item " + item.getType().getName() + " added to the cart.");
    }

    @Override
    public void removeFromCart(Customer customer, Item item) throws ItemNotFoundException {
        Cart c = getCustomerCart(customer);
        Optional<Item> itemCart = c.getItems().stream().filter(i -> i.getType().equals(item.getType())).findFirst();
        if (itemCart.isPresent()) {
            if (item.getQuantity() < itemCart.get().getQuantity()) {
                itemCart.get().setQuantity(itemCart.get().getQuantity() - item.getQuantity());
            }
            else {
                c.deleteItem(itemCart.get());
            }
        } else {
            throw new ItemNotFoundException(customer.getEmail());
        }
    }

    @Override
    public void clearCart(Customer customer) {
        getCustomerCart(customer).clearCart();
    }

    @Override
    @Interceptors({PurchaseCounter.class})
    public void validateCart(Customer customer) throws EmptyCartException, NoCreditCardException, PaymentException, CustomerNotFoundException {
        Customer c = entityManager.merge(customer);
        Cart cart = c.getCart();
        if (cart.getItems().isEmpty()) {
            throw new EmptyCartException(customer.getEmail());
        }
        if (c.hasCreditCard()) {
            processOrder(payment.payOrder(c, cart.getItems()), customer); //must wait for a boolean or an exception
            if(!c.getCreditCard().isSaveChoice()) {
                creditCardDeletion(customer);
            }

            clearCart(customer);
        } else {
            throw new NoCreditCardException(c.getEmail());
        }
    }

    @Override
    public void creditCardRegistry(Customer customer, String name, String number, String securityCode, YearMonth expiryDate, boolean saveChoice) {
        Customer c = entityManager.merge(customer);
        c.setCreditCard(new CreditCard(c, name, number, securityCode, expiryDate, saveChoice));
    }

    @Override
    public void creditCardDeletion(Customer customer) throws NoCreditCardException {
        Customer c = entityManager.merge(customer);
        if (c.hasCreditCard()) {
            c.setCreditCard(null);
        }
        else {
            throw new NoCreditCardException(c.getEmail());
        }
    }

    @Override
    public Set<Item> displayContents(Customer customer) {
        return getCustomerCart(customer).getItems();
    }

    @Override
    public Optional<Item> findItemById(Customer customer, int id) {
        Cart c = getCustomerCart(customer);
        return c.getItems().stream().filter(i->i.getId() == id).findFirst();
    }

    @Override
    public Cart getCustomerCart(Customer customer) {
        Customer c = entityManager.merge(customer);
        if (c.getCart() == null) {
            c.setCart(new Cart(c));
        }
        return c.getCart();
    }

    @Override
    public double priceCart(Customer customer) {
        return getCustomerCart(customer).getAmount();
    }

    public void processOrder(int orderId, Customer customer) throws CustomerNotFoundException {
        Customer c = entityManager.merge(customer);
        Optional <Order> o = c.getOrders().stream().filter(order -> order.getId() == orderId).findFirst();
        if (o.isPresent()){
            for (Item item : o.get().getItems()) {
                int i = 0;
                while (i < item.getQuantity()) {
                    switch (item.getType().getType()) {
                        case SUPERCARTEX:
                        case CARD:
                            cardRegistration.addCard(customer.getEmail(), item.getType());
                            break;
                        case PASS:
                            passRegistration.registerPass(customer.getEmail(), item.getType());
                            break;
                        default:
                            break;
                    }
                    i++;
                }
            }
        }

    }

    /**
     * v1: Process a super cartex which does not have any Pass. Discounts are hard coded here.
     * @return true if the superCartex has a valid pass ; false if the payment was rejected.
     */
    @Override
    public boolean processSuperCartex(SuperCartex superCartex) throws NullQuantityException, UnknownCatalogEntryException, CustomerNotFoundException, CardNotFoundException, PassNotFoundException, PaymentException, EmptyCartException, NoCreditCardException {
        superCartex = entityManager.merge(superCartex);
        Item superPass = superCartexDiscount.findSuperCartexPass(superCartex);
        if (superPass.getTotalPrice() == FREE){
            String passId = passRegistration.registerPass(superCartex.getCustomer().getEmail(), superPass.getType());
            customerCardLinker.linkPassToCardOnline(superCartex.getCustomer().getEmail(), superCartex.getPhysicalId(), passId);
            passRegistration.activatePass(passFinder.findPassById(superCartex.getCustomer().getEmail(), passId).get());
            return true;
        }
        clearCart(superCartex.getCustomer());
        addToCart(superCartex.getCustomer(), superPass);
        try {
            validateCart(superCartex.getCustomer());
            Optional<Pass> newPass = passFinder.findNotLinkedPass(superCartex.getCustomer().getEmail()).stream().
                filter(p->superPass.getType().equals(p.getType())).findFirst();
            customerCardLinker.linkPassToCardOnline(superCartex.getCustomer().getEmail(), superCartex.getPhysicalId(), newPass.get().getId());
            passRegistration.activatePass(passFinder.findPassById(superCartex.getCustomer().getEmail(), newPass.get().getId()).get());
        } catch (PaymentException paymentException){
            return false;
        }
        return true;
    }
}
