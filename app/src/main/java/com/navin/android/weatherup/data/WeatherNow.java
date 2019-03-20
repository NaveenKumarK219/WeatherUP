package com.navin.android.weatherup.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "weather_now")
public class WeatherNow {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "weather_icon")
    private String weatherIcon;
    @ColumnInfo(name = "weather_desc")
    private String weatherDesc;
    @ColumnInfo(name = "temperature")
    private double temperature;
    @ColumnInfo(name = "pressure")
    private double pressure;
    @ColumnInfo(name = "humidity")
    private double humidity;
    @ColumnInfo(name = "min_temp")
    private double minTemp;
    @ColumnInfo(name = "max_temp")
    private double maxTemp;
    @ColumnInfo(name = "wind_speed")
    private double windSpeed;
    @ColumnInfo(name = "wind_direction")
    private int windDirection;
    @ColumnInfo(name = "epoch_time")
    private int epochTime;
    @ColumnInfo(name = "sunrise")
    private int sunrise;
    @ColumnInfo(name = "sunset")
    private int sunset;
    @ColumnInfo(name = "city_name")
    private String cityName;

    @Ignore
    public WeatherNow(){

    }

    public WeatherNow(int id, String weatherIcon, String weatherDesc, double temperature, double pressure, double humidity, double minTemp, double maxTemp, double windSpeed, int windDirection, int epochTime, int sunrise, int sunset, String cityName) {
        this.id = id;
        this.weatherIcon = weatherIcon;
        this.weatherDesc = weatherDesc;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.epochTime = epochTime;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.cityName = cityName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public String getWeatherDesc() {
        return weatherDesc;
    }

    public void setWeatherDesc(String weatherDesc) {
        this.weatherDesc = weatherDesc;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(int windDirection) {
        this.windDirection = windDirection;
    }

    public int getEpochTime() {
        return epochTime;
    }

    public void setEpochTime(int epochTime) {
        this.epochTime = epochTime;
    }

    public int getSunrise() {
        return sunrise;
    }

    public void setSunrise(int sunrise) {
        this.sunrise = sunrise;
    }

    public int getSunset() {
        return sunset;
    }

    public void setSunset(int sunset) {
        this.sunset = sunset;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
