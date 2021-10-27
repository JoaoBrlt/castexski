package fr.unice.polytech.isa.payment.interfaces;

import fr.unice.polytech.isa.common.entities.accounts.Customer;
import fr.unice.polytech.isa.common.entities.shopping.catalog.Item;
import fr.unice.polytech.isa.common.exceptions.PaymentException;

import javax.ejb.Local;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Payment processor.
 * <p>
 * The payment processor is in charge of customer payments.
 * </p>
 */
@Local
public interface PaymentProcessor {
    /**
     * Performs the payment of a deliverable order.
     *
     * @param customer The customer to use.
     * @param items The items of the order.
     * @param deliveryDate The delivery date.
     * @param delivered Whether the order has been delivered.
     * @return The order identifier if the payment was successful.
     * @throws PaymentException if the payment was unsuccessful.
     */
    int payOrder(Customer customer, Set<Item> items, LocalDateTime deliveryDate, boolean delivered) throws PaymentException;

    /**
     * Performs the payment of an order.
     *
     * @param customer The customer to use.
     * @param items The items of the order.
     * @return The order identifier if the payment was successful.
     * @throws PaymentException if the payment was unsuccessful.
     */
    int payOrder(Customer customer, Set<Item> items) throws PaymentException;
}
