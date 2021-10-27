package fr.unice.polytech.isa.common.entities.notifications;

import java.util.Objects;

/**
 * Weather forecast.
 * <p>
 * Represents a forecast from the weather service.
 * </p>
 */
public class WeatherForecast {
    /**
     * The city of the forecast.
     */
    private String city;

    /**
     * The date of the forecast.
     */
    private String date;

    /**
     * The forecasted temperature (in C°).
     */
    private double temperatureCelsius;

    /**
     * The forecasted precipitation (in mm).
     */
    private double precipitationMm;

    /**
     * The forecasted wind (in km/h).
     */
    private int windKph;

    /**
     * The forecasted snow (in cm).
     */
    private int snowCm;

    /**
     * Whether powdered snow is expected.
     */
    private boolean powderAlert;

    /**
     * Default constructor.
     */
    public WeatherForecast() {}

    /**
     * Constructs a weather forecast.
     *
     * @param city The city of the forecast.
     * @param date The date of the forecast.
     * @param temperatureCelsius The forecasted temperature (in C°).
     * @param precipitationMm The forecasted precipitation (in mm).
     * @param windKph The forecasted wind (in km/h).
     * @param snowCm The forecasted snow (in cm).
     * @param powderAlert Whether powdered snow is expected.
     */
    public WeatherForecast(
        String city,
        String date,
        double temperatureCelsius,
        double precipitationMm,
        int windKph,
        int snowCm,
        boolean powderAlert
    ) {
        this.city = city;
        this.date = date;
        this.temperatureCelsius = temperatureCelsius;
        this.precipitationMm = precipitationMm;
        this.windKph = windKph;
        this.snowCm = snowCm;
        this.powderAlert = powderAlert;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTemperatureCelsius() {
        return temperatureCelsius;
    }

    public void setTemperatureCelsius(double temperatureCelsius) {
        this.temperatureCelsius = temperatureCelsius;
    }

    public double getPrecipitationMm() {
        return precipitationMm;
    }

    public void setPrecipitationMm(double precipitationMm) {
        this.precipitationMm = precipitationMm;
    }

    public int getWindKph() {
        return windKph;
    }

    public void setWindKph(int windKph) {
        this.windKph = windKph;
    }

    public int getSnowCm() {
        return snowCm;
    }

    public void setSnowCm(int snowCm) {
        this.snowCm = snowCm;
    }

    public boolean isPowderAlert() {
        return powderAlert;
    }

    public void setPowderAlert(boolean powderAlert) {
        this.powderAlert = powderAlert;
    }

    /**
     * Indicates whether another object is equal to this forecast.
     *
     * @param object The object to be compared.
     * @return <code>true</code> if this forecast is the same as the object argument; <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object) {
        // Same instance.
        if (this == object) {
            return true;
        }

        // Not same class.
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        // Same attributes.
        WeatherForecast forecast = (WeatherForecast) object;
        return (
            Double.compare(forecast.getTemperatureCelsius(), getTemperatureCelsius()) == 0 &&
            Double.compare(forecast.getPrecipitationMm(), getPrecipitationMm()) == 0 &&
            getWindKph() == forecast.getWindKph() &&
            getSnowCm() == forecast.getSnowCm() &&
            isPowderAlert() == forecast.isPowderAlert() &&
            Objects.equals(getCity(), forecast.getCity()) &&
            Objects.equals(getDate(), forecast.getDate())
        );
    }

    /**
     * Returns a hash code value for the forecast.
     *
     * @return A hash code value for the forecast.
     */
    @Override
    public int hashCode() {
        return Objects.hash(
            getCity(),
            getDate(),
            getTemperatureCelsius(),
            getPrecipitationMm(),
            getWindKph(),
            getSnowCm(),
            isPowderAlert()
        );
    }

    /**
     * Returns a string representation of the forecast.
     *
     * @return A string representation of the forecast.
     */
    @Override
    public String toString() {
        return "WeatherForecast{" +
            "city='" + city + '\'' +
            ", date='" + date + '\'' +
            ", temperatureCelsius=" + temperatureCelsius +
            ", precipitationMm=" + precipitationMm +
            ", windKph=" + windKph +
            ", snowCm=" + snowCm +
            ", powderAlert=" + powderAlert +
            '}';
    }
}
