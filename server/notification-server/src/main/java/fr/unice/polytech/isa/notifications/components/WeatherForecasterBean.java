package fr.unice.polytech.isa.notifications.components;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.unice.polytech.isa.common.entities.notifications.WeatherForecast;
import fr.unice.polytech.isa.notifications.exceptions.ExternalWeatherServiceException;
import fr.unice.polytech.isa.notifications.external.WeatherService;
import fr.unice.polytech.isa.notifications.interfaces.WeatherForecasting;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import java.time.LocalDate;

/**
 * Weather forecaster bean.
 * <p>
 * Sends and receives weather forecasts from the weather service.
 * </p>
 */
@Stateless
public class WeatherForecasterBean implements WeatherForecasting {
    /**
     * The weather service.
     */
    private WeatherService weatherService;

    /**
     * Initializes the weather service.
     */
    @PostConstruct
    private void initializePhoneService() {
        // Build the weather service client.
        weatherService = new WeatherService(
            System.getenv().getOrDefault("WEATHER_HOST", "localhost"),
            System.getenv().getOrDefault("WEATHER_PORT", "9494")
        );
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
    @Override
    public WeatherForecast getForecast(LocalDate date, String city) throws ExternalWeatherServiceException, JsonProcessingException {
        return weatherService.getForecast(date, city);
    }

    /**
     * Adds a weather forecast.
     *
     * @param forecast The weather forecast to be added.
     * @throws ExternalWeatherServiceException if there was an error while communicating with the weather service.
     * @throws JsonProcessingException if there was an error while serializing the forecast.
     */
    @Override
    public void addForecast(WeatherForecast forecast) throws ExternalWeatherServiceException, JsonProcessingException {
        weatherService.addForecast(forecast);
    }
}
