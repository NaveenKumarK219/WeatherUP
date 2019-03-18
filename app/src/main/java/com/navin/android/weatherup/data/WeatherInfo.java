package com.navin.android.weatherup.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Created by navinkumark on 2/6/19.
 */
@Entity(tableName = "weatherinfo")
public class WeatherInfo {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "epoch_time")
    private int epochTime;
    @ColumnInfo(name = "date_time")
    private String weatherDateAndTime;
    @ColumnInfo(name = "temp")
    private double temperature;
    @ColumnInfo(name = "min_temp")
    private double minTemp;
    @ColumnInfo(name = "max_temp")
    private double maxTemp;
    @ColumnInfo(name = "pressure")
    private double pressure;
    @ColumnInfo(name = "sea_level")
    private double seaLevel;
    @ColumnInfo(name = "ground_level")
    private double groundLevel;
    @ColumnInfo(name = "humidity")
    private double humidity;

    @ColumnInfo(name = "weather_id")
    private int weatherId;
    @ColumnInfo(name = "weather_main")
    private String weatherMain;
    @ColumnInfo(name = "weather_desc")
    private String weatherDescription;
    @ColumnInfo(name = "weather_icon")
    private String weatherIcon;

    @ColumnInfo(name = "clouds")
    private double cloudsAll;
    @ColumnInfo(name = "wind_speed")
    private double windSpeed;
    @ColumnInfo(name = "wind_dir")
    private double windDirection;
    @ColumnInfo(name = "city_id")
    private int cityId;
    @ColumnInfo(name = "city_name")
    private String cityName;

    @Ignore
    public WeatherInfo(){

    }

    public WeatherInfo(int id, int epochTime, String weatherDateAndTime, double temperature, double minTemp,
                       double maxTemp, double pressure, double seaLevel, double groundLevel, double humidity,
                       int weatherId, String weatherMain, String weatherDescription, String weatherIcon,
                       double cloudsAll, double windSpeed, double windDirection, int cityId, String cityName) {
        this.id = id;
        this.epochTime = epochTime;
        this.weatherDateAndTime = weatherDateAndTime;
        this.temperature = temperature;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.pressure = pressure;
        this.seaLevel = seaLevel;
        this.groundLevel = groundLevel;
        this.humidity = humidity;
        this.weatherId = weatherId;
        this.weatherMain = weatherMain;
        this.weatherDescription = weatherDescription;
        this.weatherIcon = weatherIcon;
        this.cloudsAll = cloudsAll;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.cityId = cityId;
        this.cityName = cityName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEpochTime() {
        return epochTime;
    }

    public void setEpochTime(int epochTime) {
        this.epochTime = epochTime;
    }

    public String getWeatherDateAndTime() {
        return weatherDateAndTime;
    }

    public void setWeatherDateAndTime(String weatherDateAndTime) {
        this.weatherDateAndTime = weatherDateAndTime;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
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

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getSeaLevel() {
        return seaLevel;
    }

    public void setSeaLevel(double seaLevel) {
        this.seaLevel = seaLevel;
    }

    public double getGroundLevel() {
        return groundLevel;
    }

    public void setGroundLevel(double groundLevel) {
        this.groundLevel = groundLevel;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public int getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }

    public String getWeatherMain() {
        return weatherMain;
    }

    public void setWeatherMain(String weatherMain) {
        this.weatherMain = weatherMain;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public double getCloudsAll() {
        return cloudsAll;
    }

    public void setCloudsAll(double cloudsAll) {
        this.cloudsAll = cloudsAll;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(double windDirection) {
        this.windDirection = windDirection;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
