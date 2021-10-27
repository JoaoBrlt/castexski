package fr.unice.polytech.isa.payment.external;

import fr.unice.polytech.isa.common.entities.shopping.CreditCard;
import fr.unice.polytech.isa.common.entities.accounts.Customer;
import fr.unice.polytech.isa.common.exceptions.ExternalServiceException;
import org.apache.cxf.jaxrs.client.WebClient;
import org.json.JSONObject;

import javax.ws.rs.core.MediaType;


/**
 * Bank service.
 * <p>
 * Allows the components to interact with the bank service.
 * </p>
 */
public class BankService {
    /**
     * The bank service root endpoint.
     */
    private final String url;

    /**
     * Constructs a bank service.
     *
     * @param host The bank service host name.
     * @param port The bank service port.
     */
    public BankService(String host, String port) {
        this.url = "http://" + host + ":" + port;
    }

    /**
     * Formats a credit card.
     *
     * @param creditCard The credit card to format.
     * @return The credit card as a JSON object.
     */
    private JSONObject formatCreditCard(CreditCard creditCard) {
        return new JSONObject()
            .put("CreditCardNumber", creditCard.getNumber())
            .put("ExpirationDate", creditCard.getFormattedExpiryDate())
            .put("CVV", creditCard.getSecurityCode());
    }

    /**
     * Builds a payment request.
     *
     * @param creditCard The credit card to use.
     * @param amount The amount to pay.
     * @return The payment request.
     */
    private JSONObject buildPaymentRequest(CreditCard creditCard, double amount) {
        return new JSONObject()
            .put("creditCard", formatCreditCard(creditCard))
            .put("Amount", amount);
    }

    /**
     * Performs a payment.
     *
     * @param customer The customer who wants to pay.
     * @param amount The amount to pay.
     * @return <code>true</code> if the payment was successful; <code>false</code> otherwise.
     * @throws ExternalServiceException if there was an error while contacting the bank service.
     */
    public boolean performPayment(Customer customer, double amount) throws ExternalServiceException {
        // Build the payment request.
        JSONObject request = buildPaymentRequest(customer.getCreditCard(), amount);

        int id;
        try {
            // Send the payment request.
            String response = WebClient
                .create(url)
                .path("/pay")
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .post(request.toString(), String.class);

            // Save the payment identifier.
            id = Integer.parseInt(response);
        } catch (Exception error) {
            throw new ExternalServiceException("bank", url + "/pay", error);
        }


        JSONObject payment;
        try {
            // Retrieve the payment status.
            String response = WebClient
                .create(url)
                .path("/payments/" + id)
                .get(String.class);

            // Save the payment response.
            payment = new JSONObject(response);
        } catch (Exception error) {
            throw new ExternalServiceException("bank", url + "/payments/" + id, error);
        }

        // Check the payment status.
        return payment.getInt("Status") == 0;
    }
}
