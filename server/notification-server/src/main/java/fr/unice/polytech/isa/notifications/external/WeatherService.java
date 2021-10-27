package fr.unice.polytech.isa.notifications.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.isa.common.entities.notifications.WeatherForecast;
import fr.unice.polytech.isa.notifications.exceptions.ExternalWeatherServiceException;
import org.apache.cxf.jaxrs.client.WebClient;

import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Weather service client.
 * <p>
 * Allows the components to interact with the weather service.
 * </p>
 */
public class WeatherService {
    /**
     * The service root endpoint.
     */
    private final String url;

    /**
     * The object mapper.
     */
    private final ObjectMapper mapper;

    /**
     * Constructs a weather service client.
     *
     * @param host The weather service host name.
     * @param port The weather service port.
     */
    public WeatherService(String host, String port) {
        this.url = "http://" + host + ":" + port;
        this.mapper = new ObjectMapper();
    }

    /**
     * Returns the weather forecast.
     *
     * @param date The date of the forecast.
     * @param city The city of the forecast.
     * @return The request weather forecast, if found.
     * @throws ExternalWeatherServiceException if there was an error while communicating with the weather service.
     * @throws JsonProcessingException if there was an error while serializing the forecast.
     */
    public WeatherForecast getForecast(LocalDate date, String city) throws ExternalWeatherServiceException, JsonProcessingException {
        // Initialize the response.
        String response;

        // Format the date.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        String formattedDate = date.format(formatter);

        // Get the forecast.
        try {
            response = WebClient
                .create(url)
                .path("/forecasts/" + formattedDate + "/" + city)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get(String.class);
        } catch (Exception error) {
            throw new ExternalWeatherServiceException("GET", url + "/forecasts", error);
        }

        // Deserialize the forecast.
        return mapper.readValue(response, WeatherForecast.class);
    }

    /**
     * Adds a weather forecast.
     *
     * @param forecast The weather forecast to be added.
     * @throws ExternalWeatherServiceException if there was an error while communicating with the weather service.
     * @throws JsonProcessingException if there was an error while serializing the forecast.
     */
    public void addForecast(WeatherForecast forecast) throws ExternalWeatherServiceException, JsonProcessingException {
        // Serialize the forecast.
        String request = mapper.writeValueAsString(forecast);

        // Send the forecast.
        try {
            WebClient
                .create(url)
                .path("/forecasts")
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .post(request, String.class);
        } catch (Exception error) {
            throw new ExternalWeatherServiceException("POST", url + "/forecasts", error);
        }
    }
}
