package fr.unice.polytech.isa.notifications.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.isa.common.entities.notifications.Email;
import fr.unice.polytech.isa.notifications.exceptions.ExternalEmailServiceException;
import org.apache.cxf.jaxrs.client.WebClient;

import javax.ws.rs.core.MediaType;

/**
 * Email service client.
 * <p>
 * Allows the components to interact with the email service.
 * </p>
 */
public class EmailService {
    /**
     * The service root endpoint.
     */
    private final String url;

    /**
     * The object mapper.
     */
    private final ObjectMapper mapper;

    /**
     * Constructs an email service client.
     *
     * @param host The email service host name.
     * @param port The email service port.
     */
    public EmailService(String host, String port) {
        this.url = "http://" + host + ":" + port;
        this.mapper = new ObjectMapper();
    }

    /**
     * Sends an email.
     *
     * @param email The email to be sent.
     * @return The email identifier.
     * @throws ExternalEmailServiceException if there was an error while communicating with the email service.
     * @throws JsonProcessingException if there was an error while serializing the email.
     */
    public int sendEmail(Email email) throws ExternalEmailServiceException, JsonProcessingException {
        // Initialize the response.
        String response;

        // Serialize the email.
        String request = mapper.writeValueAsString(email);

        // Send the email.
        try {
            response = WebClient
                .create(url)
                .path("/emails")
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .post(request, String.class);
        } catch (Exception error) {
            throw new ExternalEmailServiceException("POST", "/emails", error);
        }

        // Deserialize the identifier.
        return Integer.parseInt(response);
    }

    /**
     * Returns an email by identifier.
     *
     * @param identifier The email identifier.
     * @return The requested email.
     * @throws ExternalEmailServiceException if there was an error while communicating with the email service.
     * @throws JsonProcessingException if there was an error while deserializing the email.
     */
    public Email getEmail(int identifier) throws ExternalEmailServiceException, JsonProcessingException {
        // Initialize the response.
        String response;

        // Get the email.
        try {
            response = WebClient
                .create(url)
                .path("/emails/" + identifier)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get(String.class);
        } catch (Exception error) {
            throw new ExternalEmailServiceException("GET", url + "/emails/" + identifier, error);
        }

        // Deserialize the email.
        return mapper.readValue(response, Email.class);
    }

    /**
     * Returns the emails sent from a specific email address.
     *
     * @param from The email address of the sender.
     * @return The list of email identifiers sent from the specified email address.
     * @throws ExternalEmailServiceException if there was an error while communicating with the email service.
     * @throws JsonProcessingException if there was an error while deserializing the emails.
     */
    public int[] getEmailsFrom(String from) throws ExternalEmailServiceException, JsonProcessingException {
        // Initialize the response.
        String response;

        // Get the emails.
        try {
             response = WebClient
                .create(url)
                .path("/emails/from/" + from)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get(String.class);
        } catch (Exception error) {
            throw new ExternalEmailServiceException("GET", url + "/emails/from/" + from, error);
        }

        // Deserialize the identifiers.
        return mapper.readValue(response, int[].class);
    }

    /**
     * Returns the emails sent to a specific email address.
     *
     * @param to The email address of the recipient.
     * @return The list of email identifiers sent to the specified email address.
     * @throws ExternalEmailServiceException if there was an error while communicating with the email service.
     * @throws JsonProcessingException if there was an error while deserializing the emails.
     */
    public int[] getEmailsTo(String to) throws ExternalEmailServiceException, JsonProcessingException {
        // Initialize the response.
        String response;

        // Get the emails.
        try {
            response = WebClient
                .create(url)
                .path("/emails/to/" + to)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get(String.class);
        } catch (Exception error) {
            throw new ExternalEmailServiceException("GET", url + "/emails/to/" + to, error);
        }

        // Deserialize the identifiers.
        return mapper.readValue(response, int[].class);
    }

    /**
     * Deletes all the emails (FOR TESTING PURPOSES ONLY).
     *
     * @throws ExternalEmailServiceException if there was an error while communicating with the email service.
     */
    public void deleteEmails() throws ExternalEmailServiceException {
        // Delete all the emails.
        try {
            WebClient
                .create(url)
                .path("/emails")
                .delete();
        } catch (Exception error) {
            throw new ExternalEmailServiceException("DELETE", url + "/emails", error);
        }
    }
}
