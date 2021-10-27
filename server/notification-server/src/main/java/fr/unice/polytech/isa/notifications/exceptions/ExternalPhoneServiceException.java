package fr.unice.polytech.isa.notifications.exceptions;

import java.io.Serializable;

/**
 * External phone service exception.
 * <p>
 * Indicates that there was error while communicating with the phone service.
 * </p>
 */
public class ExternalPhoneServiceException extends Exception implements Serializable {
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
    public ExternalPhoneServiceException() {
        super("There was an error while communicating with the phone service.");
    }

    /**
     * Constructs an external phone service exception.
     *
     * @param method The method of the request.
     * @param resource The identifier of the notification.
     * @param cause The cause of the exception.
     */
    public ExternalPhoneServiceException(String method, String resource, Throwable cause) {
        super("There was an error while sending a " + method + " request for " + resource + " to the phone service.", cause);
    }

    /**
     * Constructs an external phone service exception.
     *
     * @param method The method of the request.
     * @param resource The identifier of the notification.
     */
    public ExternalPhoneServiceException(String method, String resource) {
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
