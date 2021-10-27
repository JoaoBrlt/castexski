package fr.unice.polytech.isa.common.exceptions;

import java.io.Serializable;

/**
 * Payment exception.
 * <p>
 * An error that occurred while processing a payment.
 * </p>
 */
public class PaymentException extends Exception implements Serializable {
    /**
     * Constructs a payment exception.
     */
    public PaymentException() {
        super("There was an error while processing the payment.");
    }

    /**
     * Constructs a payment exception.
     *
     * @param message The detail message of the exception.
     */
    public PaymentException(String message) {
        super(message);
    }

    /**
     * Constructs a payment exception.
     *
     * @param message The detail message of the exception.
     * @param cause The cause of the exception.
     */
    public PaymentException(String message, Throwable cause) {
        super(message, cause);
    }
}
