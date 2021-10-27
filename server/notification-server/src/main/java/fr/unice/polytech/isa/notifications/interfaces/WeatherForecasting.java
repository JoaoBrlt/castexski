package fr.unice.polytech.isa.notifications.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.unice.polytech.isa.common.entities.notifications.WeatherForecast;
import fr.unice.polytech.isa.notifications.exceptions.ExternalWeatherServiceException;

import javax.ejb.Local;
import java.time.LocalDate;

/**
 * Weather forecasting.
 * <p>
 * Sends and receives weather forecasts from the weather service.
 * </p>
 */
@Local
public interface WeatherForecasting {
    /**
     * Returns the weather forecast.
     *
     * @param date The date of the forecast.
     * @param city The city of the forecast.
     * @return The request weather forecast, if found.
     * @throws ExternalWeatherServiceException if there was an error while communicating with the weather service.
     * @throws JsonProcessingException if there was an error while serializing the forecast.
     */
    WeatherForecast getForecast(LocalDate date, String city) throws ExternalWeatherServiceException, JsonProcessingException;

    /**
     * Adds a weather forecast.
     *
     * @param forecast The weather forecast to be added.
     * @throws ExternalWeatherServiceException if there was an error while communicating with the weather service.
     * @throws JsonProcessingException if there was an error while serializing the forecast.
     */
    void addForecast(WeatherForecast forecast) throws ExternalWeatherServiceException, JsonProcessingException;
}
