package fr.unice.polytech.isa.payment.components;

import fr.unice.polytech.isa.common.entities.accounts.Customer;
import fr.unice.polytech.isa.common.entities.shopping.catalog.Item;
import fr.unice.polytech.isa.common.entities.shopping.Order;
import fr.unice.polytech.isa.common.exceptions.ExternalServiceException;
import fr.unice.polytech.isa.common.exceptions.PaymentException;
import fr.unice.polytech.isa.payment.external.BankService;
import fr.unice.polytech.isa.payment.interfaces.PaymentProcessor;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Billing bean.
 * <p>
 * The billing bean is in charge of customer payments.
 * </p>
 */
@Stateless
public class BillingBean implements PaymentProcessor {
    /**
     * The component logger.
     */
    private static final Logger logger = Logger.getLogger(Logger.class.getName());

    /**
     * The entity manager.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * The external bank service.
     */
    private BankService bankService;

    /**
     * Initializes the bank service.
     */
    @PostConstruct
    private void initializeBankService() {
        // Build the bank service client.
        bankService = new BankService(
            System.getenv().getOrDefault("BANK_HOST", "localhost"),
            System.getenv().getOrDefault("BANK_PORT", "9090")
        );
    }

    /**
     * Performs the payment of a deliverable order.
     *
     * @param c The customer.
     * @param items The items of the order.
     * @param deliveryDate The delivery date.
     * @param delivered Whether the order has been delivered.
     * @return The order identifier if the payment was successful.
     * @throws PaymentException if the payment was unsuccessful.
     */
    @Override
    public int payOrder(Customer c, Set<Item> items, LocalDateTime deliveryDate, boolean delivered) throws PaymentException {
        // Update the customer.
        Customer customer = entityManager.merge(c);

        // Build the order.
        Order order = new Order(
            customer,
            items,
            LocalDateTime.now(),
            deliveryDate,
            delivered
        );

        // Perform the payment.
        boolean status;
        double amount = order.getPrice();
        try {
            status = bankService.performPayment(customer, amount);
        } catch (ExternalServiceException error) {
            throw new PaymentException("Unable to perform the payment of the order.", error);
        }

        // Check the payment.
        if (!status) {
            throw new PaymentException("The payment has been refused by the bank service.");
        }

        // Persist the order.
        customer.addOrder(order);
        entityManager.persist(order);
        return order.getId();
    }

    /**
     * Performs the payment of an order.
     *
     * @param customer The customer to use.
     * @param items The items of the order.
     * @return The order identifier if the payment was successful.
     * @throws PaymentException if the payment was unsuccessful.
     */
    @Override
    public int payOrder(Customer customer, Set<Item> items) throws PaymentException {
        return payOrder(customer, items, LocalDateTime.now(), false);
    }
}
