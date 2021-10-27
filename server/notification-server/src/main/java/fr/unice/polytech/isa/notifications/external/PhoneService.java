package fr.unice.polytech.isa.notifications.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.isa.common.entities.notifications.SMS;
import fr.unice.polytech.isa.notifications.exceptions.ExternalPhoneServiceException;
import org.apache.cxf.jaxrs.client.WebClient;

import javax.ws.rs.core.MediaType;

/**
 * Phone service client.
 * <p>
 * Allows the components to interact with the phone service.
 * </p>
 */
public class PhoneService {
    /**
     * The service root endpoint.
     */
    private final String url;

    /**
     * The object mapper.
     */
    private final ObjectMapper mapper;

    /**
     * Constructs a phone service client.
     *
     * @param host The phone service host name.
     * @param port The phone service port.
     */
    public PhoneService(String host, String port) {
        this.url = "http://" + host + ":" + port;
        this.mapper = new ObjectMapper();
    }

    /**
     * Sends a text message.
     *
     * @param message The text message to be sent.
     * @return The text message identifier.
     * @throws ExternalPhoneServiceException if there was an error while communicating with the phone service.
     * @throws JsonProcessingException if there was an error while serializing the text message.
     */
    public int sendMessage(SMS message) throws ExternalPhoneServiceException, JsonProcessingException {
        // Initialize the response.
        String response;

        // Serialize the text message.
        String request = mapper.writeValueAsString(message);

        // Send the text message.
        try {
            response = WebClient
                .create(url)
                .path("/messages")
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .post(request, String.class);
        } catch (Exception error) {
            throw new ExternalPhoneServiceException("POST", url + "/messages", error);
        }

        // Deserialize the identifier.
        return Integer.parseInt(response);
    }

    /**
     * Returns a text message by identifier.
     *
     * @param identifier The text message identifier.
     * @return The requested text message.
     * @throws ExternalPhoneServiceException if there was an error while communicating with the phone service.
     * @throws JsonProcessingException if there was an error while deserializing the text message.
     */
    public SMS getMessage(int identifier) throws ExternalPhoneServiceException, JsonProcessingException {
        // Initialize the response.
        String response;

        // Get the text message.
        try {
            response = WebClient
                .create(url)
                .path("/messages/" + identifier)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get(String.class);
        } catch (Exception error) {
            throw new ExternalPhoneServiceException("GET", url + "/messages/" + identifier, error);
        }

        // Deserialize the text message.
        return mapper.readValue(response, SMS.class);
    }

    /**
     * Returns the text messages sent from a specific phone number.
     *
     * @param from The phone number of the sender.
     * @return The list of text message identifiers sent from the specified phone number.
     * @throws ExternalPhoneServiceException if there was an error while communicating with the phone service.
     * @throws JsonProcessingException if there was an error while deserializing the text messages.
     */
    public int[] getMessagesFrom(String from) throws ExternalPhoneServiceException, JsonProcessingException {
        // Initialize the response.
        String response;

        // Get the text messages.
        try {
            response = WebClient
                .create(url)
                .path("/messages/from/" + from)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get(String.class);
        } catch (Exception error) {
            throw new ExternalPhoneServiceException("GET", url + "/messages/from/" + from, error);
        }

        // Deserialize the identifiers.
        return mapper.readValue(response, int[].class);
    }

    /**
     * Returns the text messages sent to a specific phone number.
     *
     * @param to The phone number of the recipient.
     * @return The list of text message identifiers sent to the specified phone number.
     * @throws ExternalPhoneServiceException if there was an error while communicating with the phone service.
     * @throws JsonProcessingException if there was an error while deserializing the text messages.
     */
    public int[] getMessagesTo(String to) throws ExternalPhoneServiceException, JsonProcessingException {
        // Initialize the response.
        String response;

        // Get the text messages.
        try {
            response = WebClient
                .create(url)
                .path("/messages/to/" + to)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get(String.class);
        } catch (Exception error) {
            throw new ExternalPhoneServiceException("GET", url + "/messages/to/" + to, error);
        }

        // Deserialize the identifiers.
        return mapper.readValue(response, int[].class);
    }

    /**
     * Deletes all the text messages (FOR TESTING PURPOSES ONLY).
     *
     * @throws ExternalPhoneServiceException if there was an error while communicating with the phone service.
     */
    public void deleteMessages() throws ExternalPhoneServiceException {
        // Delete all the text messages.
        try {
            WebClient
                .create(url)
                .path("/messages")
                .delete();
        } catch (Exception error) {
            throw new ExternalPhoneServiceException("DELETE", url + "/messages", error);
        }
    }
}
