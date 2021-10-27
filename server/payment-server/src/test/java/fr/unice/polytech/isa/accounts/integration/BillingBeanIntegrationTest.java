package fr.unice.polytech.isa.accounts.integration;

import arquillian.AbstractPaymentTest;
import fr.unice.polytech.isa.common.entities.accounts.Customer;
import fr.unice.polytech.isa.common.entities.shopping.catalog.Item;
import fr.unice.polytech.isa.common.entities.shopping.catalog.ItemType;
import fr.unice.polytech.isa.common.entities.items.ItemTypeName;
import fr.unice.polytech.isa.common.entities.shopping.CreditCard;
import fr.unice.polytech.isa.common.exceptions.PaymentException;
import fr.unice.polytech.isa.payment.interfaces.PaymentProcessor;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.*;

import java.time.YearMonth;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class BillingBeanIntegrationTest extends AbstractPaymentTest {

    @EJB private PaymentProcessor paymentProcessor;

    @PersistenceContext private EntityManager entityManager;
    @Inject private UserTransaction utx;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    private Customer customer;
    private CreditCard creditCard;
    private static String MagicKey = "896983";
    private Set<Item> itemSet;

    @Before
    public void setUpContext(){
        customer = new Customer();
        customer.setEmail("marcel@mail.mail");
        customer.setLastName("Eats");
        customer.setFirstName("Marcel");

        YearMonth yearMonth = YearMonth.of(2100,1);
        creditCard = new CreditCard(customer, "Marcel Eats",  "1234567891"+MagicKey, "000", yearMonth, false);

        itemSet = new HashSet<>();

        Item item1 = new Item(new ItemType("CARTEX", 10.0, ItemTypeName.CARD), 1);
        Item item2 = new Item(new ItemType("DISCOVERY", 10.0, ItemTypeName.PASS), 1);
        itemSet.add(item1); itemSet.add(item2);

        customer.setCreditCard(creditCard);
        entityManager.persist(customer);
    }

   @Test
    public void payOrder() throws PaymentException {
        assertTrue(customer.getOrders().isEmpty());
        paymentProcessor.payOrder(customer,itemSet);
        assertEquals(1,customer.getOrders().size());
    }

    @Test(expected = PaymentException.class)
    public void payOrderMagicKeyException() throws PaymentException {
        creditCard.setNumber("1234567891011121");
        assertTrue(customer.getOrders().isEmpty());
        paymentProcessor.payOrder(customer,itemSet);
        exceptionRule.expect(PaymentException.class);
        exceptionRule.expectMessage("The payment has been refused by the bank service.");
    }

   @After
    public void cleaningUp() throws Exception {
        utx.begin();
        Customer c = entityManager.merge(customer);
        entityManager.remove(c);
        CreditCard cc = entityManager.merge(creditCard);
        entityManager.remove(cc);
        for(Item i : itemSet){
            Item item = entityManager.merge(i);
            entityManager.remove(item);
        }
        utx.commit();

    }

}
