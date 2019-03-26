package com.navin.android.weatherup.utilities;

import android.text.format.DateFormat;

import com.navin.android.weatherup.data.WeatherNow;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by navinkumark on 2/12/19.
 */

public class CommonUtils {

    private static final String PRESSURE = "pressure";
    private static final String HUMIDITY = "humidity";
    private static final String TEMPERATURE = "temperature";
    private static final String WINDSPEED = "windspeed";
    private static final String SUNRISE = "sunrise";
    private static final String SUNSET = "sunset";
    private static final String MAXTEMP = "maxtemp";
    private static final String MINTEMP = "mintemp";

    public static String formatDateString(String dateString){
        Date inDate = new Date(dateString);
        return DateFormat.format("E d M", inDate).toString();
    }

    public static String getReadableDate(long unixTimestamp){
        Date unixDate = new Date(unixTimestamp * 1000L);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM HH:mm");
        return dateFormat.format(unixDate);
    }

    public static String formatTemperature(double temp){
        return String.valueOf((int)temp).concat("\u00B0");
    }

    public static String formatWeatherInfo(Object weatherInput, String weatherParam){
        String formattedWeatherInfo = null;
        if(PRESSURE.equals(weatherParam)){
            formattedWeatherInfo = "P:" + String.valueOf(weatherInput).concat("hPa");
        } else if(HUMIDITY.equals(weatherParam)){
            formattedWeatherInfo = "H:" + String.valueOf(weatherInput).concat("%");
        } else if(TEMPERATURE.equals(weatherParam)){
            Double doubleValue = (Double) weatherInput;
            formattedWeatherInfo = String.valueOf(doubleValue.intValue()).concat("\u00B0");
        }

        return formattedWeatherInfo;
    }

    public static Map<String,String> formatWeatherNowData(WeatherNow weatherNow){
        Map<String,String> formatedWeatherData = new HashMap<>();
        formatedWeatherData.put(PRESSURE, "Pressure : " + String.valueOf(weatherNow.getPressure()).concat("hPa"));
        formatedWeatherData.put(HUMIDITY, "Humidity : " + String.valueOf(weatherNow.getHumidity()).concat("%"));
        formatedWeatherData.put(WINDSPEED, "Wind Speed : " + String.valueOf(weatherNow.getWindSpeed()).concat("mph"));
        formatedWeatherData.put(SUNRISE, "Sunrise : " + getReadableDate(weatherNow.getSunrise()));
        formatedWeatherData.put(SUNSET, "Sunset : " + getReadableDate(weatherNow.getSunset()));
        formatedWeatherData.put(MAXTEMP, formatTemperature(weatherNow.getMaxTemp()));
        formatedWeatherData.put(MINTEMP, formatTemperature(weatherNow.getMinTemp()));
        return formatedWeatherData;
    }
}
