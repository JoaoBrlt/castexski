package fr.unice.polytech.isa.notifications.exceptions;

import java.io.Serializable;

/**
 * External email service exception.
 * <p>
 * Indicates that there was error while communicating with the phone service.
 * </p>
 */
public class ExternalEmailServiceException extends Exception implements Serializable {
    /**
     * The default message pattern.
     */
    private static final String message = "An error occurred while communicating with the %s service at '%s'.";

    /**
     * The method of the request.
     */
    private String method;

    /**
     * The resource of the request.
     */
    private String resource;

    /**
     * Default constructor.
     */
    public ExternalEmailServiceException() {
        super("There was an error while communicating with the email service.");
    }

    /**
     * Constructs an external email service exception.
     *
     * @param method The method of the request.
     * @param resource The identifier of the notification.
     * @param cause The cause of the exception.
     */
    public ExternalEmailServiceException(String method, String resource, Throwable cause) {
        super("There was an error while sending a " + method + " request for " + resource + " to the email service.", cause);
    }

    /**
     * Constructs an external email service exception.
     *
     * @param method The method of the request.
     * @param resource The identifier of the notification.
     */
    public ExternalEmailServiceException(String method, String resource) {
        this(method, resource, null);
    }

    /**
     * Returns the method of the request.
     *
     * @return The method of the request.
     */
    public String getMethod() {
        return method;
    }

    /**
     * Sets the method of the request.
     *
     * @param method The new method of the request.
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * Returns the resource of the request.
     *
     * @return The resource of the request.
     */
    public String getResource() {
        return resource;
    }

    /**
     * Sets the resource of the request.
     *
     * @param resource The new resource of the request.
     */
    public void setResource(String resource) {
        this.resource = resource;
    }
}
