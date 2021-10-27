package fr.unice.polytech.isa.common.exceptions;


import java.io.Serializable;

/**
 * External service exception.
 * <p>
 * An error that occurred while communicating with an external service.
 * </p>
 */
public class ExternalServiceException extends Exception implements Serializable {
    /**
     * The default message pattern.
     */
    private static final String MESSAGE = "An error occurred while communicating with the %s service at '%s'.";

    /**
     * Default constructor.
     */
    public ExternalServiceException() {
        super("An error occurred while communicating with an external service.");
    }

    /**
     * Constructs an external service exception.
     *
     * @param service The name of the service.
     * @param resource The URI of the resource.
     */
    public ExternalServiceException(String service, String resource) {
        super(String.format(MESSAGE, service, resource));
    }

    /**
     * Constructs an external service exception.
     *
     * @param service The name of the service.
     * @param resource The URI of the resource.
     * @param cause The cause of the exception.
     */
    public ExternalServiceException(String service, String resource, Throwable cause) {
        super(String.format(MESSAGE, service, resource), cause);
    }
}
