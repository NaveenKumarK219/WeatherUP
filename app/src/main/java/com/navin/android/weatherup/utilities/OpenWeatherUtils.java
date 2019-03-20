package com.navin.android.weatherup.utilities;

import android.net.Uri;
import android.util.JsonReader;
import android.util.Log;

import com.navin.android.weatherup.data.WeatherInfo;
import com.navin.android.weatherup.data.WeatherNow;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by navinkumark on 2/6/19.
 */

public class OpenWeatherUtils {

    private static final String TAG = OpenWeatherUtils.class.getSimpleName();
    private static final String OPEN_WEATHER_FORECAST_URL = "http://api.openweathermap.org/data/2.5/forecast";
    private static final String OPEN_WEATHER_FORECAST_TODAY_URL = "http://api.openweathermap.org/data/2.5/weather";
    private static final String APPID_PARAM = "appid";
    private static final String API_KEY = "944da5c89d0e0ac2bc371808c0841db2";
    // api.openweathermap.org/data/2.5/forecast?q={city name},{country code}
    //private static final String QUERY_PARAM = "q";
    // api.openweathermap.org/data/2.5/forecast?id={city ID}
    //private static final String CITY_ID_PARAM = "id";
    // api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}
    private static final String LATITUDE_PARAM = "lat";
    private static final String LONGITUDE_PARAM = "lon";
    // api.openweathermap.org/data/2.5/forecast?zip={zip code},{country code}
    //private static final String ZIPCODE_PARAM = "zip";
    private static final String UNITS_PARAM = "units";
    //private static final String DEFAULT_UNITS = "metric";

    private static int mCityId;
    private static String mCityName;

    public static URL buildWeatherUrlWithPreferences(String lat, String lon, String units){

        Uri weatherUri = Uri.parse(OPEN_WEATHER_FORECAST_URL).buildUpon()
                .appendQueryParameter(APPID_PARAM, API_KEY)
                .appendQueryParameter(LATITUDE_PARAM, lat)
                .appendQueryParameter(LONGITUDE_PARAM, lon)
                .appendQueryParameter(UNITS_PARAM, units)
                .build();

        try {
            URL weatherUrl = new URL(weatherUri.toString());
            Log.i(TAG, "Weather URL: "+weatherUrl);
            return weatherUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URL buildTodaysWeatherUrlWithPreferences(String lat, String lon, String units){
        Uri weatherUri = Uri.parse(OPEN_WEATHER_FORECAST_TODAY_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM, API_KEY)
                        .appendQueryParameter(LATITUDE_PARAM, lat)
                        .appendQueryParameter(LONGITUDE_PARAM, lon)
                        .appendQueryParameter(UNITS_PARAM, units)
                        .build();
        try {
            URL weatherUrl = new URL(weatherUri.toString());
            Log.i(TAG, "Weather URL: "+weatherUrl.toString());
            return weatherUrl;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String getWeatherDataFromApi(URL weatherUrl) throws IOException {
        Log.i(TAG, "Connecting to API...");
        HttpURLConnection urlConnection = (HttpURLConnection) weatherUrl.openConnection();
        try {

            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            Log.i(TAG, "Has Input:"+hasInput);
            if(hasInput){
                return scanner.next();
            } else{
                return null;
            }

        } catch (Exception e) {
            Log.e(TAG, "Error Connecting To API:"+e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            urlConnection.disconnect();
        }

    }

    public static WeatherNow getWeatherNowFromJsonResponse(String responseJson){
        try(JsonReader jsonReader = new JsonReader(new StringReader(responseJson))) {
            WeatherNow weatherNow = new WeatherNow();
            jsonReader.beginObject();
            while (jsonReader.hasNext()){
                String key = jsonReader.nextName();
                if("weather".equals(key)){
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()){
                        jsonReader.beginObject();
                        while (jsonReader.hasNext()){
                            key = jsonReader.nextName();
                            if("description".equals(key))
                                weatherNow.setWeatherDesc(jsonReader.nextString());
                            else if("icon".equals(key))
                                weatherNow.setWeatherIcon(jsonReader.nextString());
                            else jsonReader.skipValue();
                        }
                        jsonReader.endObject();
                    }
                    jsonReader.endArray();
                } else if("main".equals(key)){
                    jsonReader.beginObject();
                    while (jsonReader.hasNext()){
                        key = jsonReader.nextName();
                        if("temp".equals(key))
                            weatherNow.setTemperature(jsonReader.nextDouble());
                        else if("pressure".equals(key))
                            weatherNow.setPressure(jsonReader.nextDouble());
                        else if("humidity".equals(key))
                            weatherNow.setHumidity(jsonReader.nextDouble());
                        else if("temp_min".equals(key))
                            weatherNow.setMinTemp(jsonReader.nextDouble());
                        else if("temp_max".equals(key))
                            weatherNow.setMaxTemp(jsonReader.nextDouble());
                    }
                    jsonReader.endObject();
                } else if("wind".equals(key)){
                    jsonReader.beginObject();
                    while (jsonReader.hasNext()){
                        key = jsonReader.nextName();
                        if("speed".equals(key))
                            weatherNow.setWindSpeed(jsonReader.nextDouble());
                        else if("deg".equals(key))
                            weatherNow.setWindDirection(jsonReader.nextInt());
                    }
                    jsonReader.endObject();
                } else if("dt".equals(key)){
                    weatherNow.setEpochTime(jsonReader.nextInt());
                } else if("sys".equals(key)){
                    jsonReader.beginObject();
                    while (jsonReader.hasNext()){
                        key = jsonReader.nextName();
                        if("sunrise".equals(key))
                            weatherNow.setSunrise(jsonReader.nextInt());
                        else if("sunset".equals(key))
                            weatherNow.setSunset(jsonReader.nextInt());
                        else jsonReader.skipValue();
                    }
                    jsonReader.endObject();
                } else if("name".equals(key))
                    weatherNow.setCityName(jsonReader.nextString());
                else jsonReader.skipValue();
            }
            jsonReader.endObject();
            return weatherNow;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<WeatherInfo> getWeatherListFromJsonResponse(String responseJsonString) throws JSONException, IOException {
        try (JsonReader jsonReader = new JsonReader(new StringReader(responseJsonString))) {
            List<WeatherInfo> weatherInfoList = new ArrayList<>();
            getCityDetailsFromJson(responseJsonString);

            Log.i(TAG, "Started JSON Reading...");
            jsonReader.beginObject();

            while (jsonReader.hasNext()) {
                String key = jsonReader.nextName();
                //Log.i(TAG, "Key Value: "+key);
                if ("cod".equals(key)) {
                    switch (Integer.parseInt(jsonReader.nextString())) {
                        case HttpURLConnection.HTTP_OK:
                            break;

                        default:
                            return null;
                    }
                } else if ("list".equals(key)) {
                    readForecastJsonArray(jsonReader, weatherInfoList);
                } else {
                    jsonReader.skipValue();
                }
            }

            jsonReader.endObject();

            return weatherInfoList;
        }
    }

    private static void getCityDetailsFromJson(String responseJsonString) throws JSONException {
        JSONObject weatherJsonObj = new JSONObject(responseJsonString);
        JSONObject cityJsonObj = weatherJsonObj.getJSONObject("city");
        mCityId = cityJsonObj.getInt("id");
        mCityName = cityJsonObj.getString("name");
    }

    private static void readForecastJsonArray(JsonReader jsonReader, List<WeatherInfo> weatherInfoList) throws IOException {
        jsonReader.beginArray();
        //Log.i(TAG, "JSON Array started...");
        while (jsonReader.hasNext()){
            weatherInfoList.add(readForecastJsonObject(jsonReader));
        }
        jsonReader.endArray();
        //Log.i(TAG, "JSON Array End.");
    }

    private static WeatherInfo readForecastJsonObject(JsonReader jsonReader) throws IOException {
        WeatherInfo weatherInfo = new WeatherInfo();
        weatherInfo.setCityId(mCityId);
        weatherInfo.setCityName(mCityName);
        jsonReader.beginObject();

        while (jsonReader.hasNext()){
            String key = jsonReader.nextName();
            //Log.i(TAG, "Weather Object: "+key);
            if("dt".equals(key)) weatherInfo.setEpochTime(jsonReader.nextInt());
            else if("main".equals(key)) readMainForecastObject(jsonReader, weatherInfo);
            else if("weather".equals(key)){
                jsonReader.beginArray();
                while (jsonReader.hasNext()){
                    readWeatherDescriptionObject(jsonReader, weatherInfo);
                }
                jsonReader.endArray();
            } else if("clouds".equals(key)){
                jsonReader.beginObject();

                while (jsonReader.hasNext()){
                    if("all".equals(jsonReader.nextName()))
                        weatherInfo.setCloudsAll(jsonReader.nextDouble());
                }

                jsonReader.endObject();
            } else if("wind".equals(key)){
                jsonReader.beginObject();

                while (jsonReader.hasNext()){
                    String windKey = jsonReader.nextName();
                    if("speed".equals(windKey)) weatherInfo.setWindSpeed(jsonReader.nextDouble());
                    else if("deg".equals(windKey))
                        weatherInfo.setWindDirection(jsonReader.nextDouble());
                }

                jsonReader.endObject();
            } else if("dt_txt".equals(key))
                weatherInfo.setWeatherDateAndTime(jsonReader.nextString());
            else jsonReader.skipValue();
        }

        jsonReader.endObject();

        return weatherInfo;
    }

    private static void readMainForecastObject(JsonReader jsonReader, WeatherInfo weatherInfo) throws IOException {
        jsonReader.beginObject();

        while (jsonReader.hasNext()){
            String key = jsonReader.nextName();
            //Log.i(TAG, "Main Weather: "+key);
            if("temp".equals(key)) weatherInfo.setTemperature(jsonReader.nextDouble());
            else if("temp_min".equals(key)) weatherInfo.setMinTemp(jsonReader.nextDouble());
            else if("temp_max".equals(key)) weatherInfo.setMaxTemp(jsonReader.nextDouble());
            else if("pressure".equals(key)) weatherInfo.setPressure(jsonReader.nextDouble());
            else if("sea_level".equals(key)) weatherInfo.setSeaLevel(jsonReader.nextDouble());
            else if("grnd_level".equals(key)) weatherInfo.setGroundLevel(jsonReader.nextDouble());
            else if("humidity".equals(key)) weatherInfo.setHumidity(jsonReader.nextDouble());
            else jsonReader.skipValue();
        }

        jsonReader.endObject();
    }

    private static void readWeatherDescriptionObject(JsonReader jsonReader, WeatherInfo weatherInfo) throws IOException {
        jsonReader.beginObject();

        while (jsonReader.hasNext()){
            String key = jsonReader.nextName();
            //Log.i(TAG, "Weather Description: "+key);
            if ("id".equals(key)) weatherInfo.setWeatherId(jsonReader.nextInt());
            else if("main".equals(key)) weatherInfo.setWeatherMain(jsonReader.nextString());
            else if("description".equals(key))
                weatherInfo.setWeatherDescription(jsonReader.nextString());
            else if("icon".equals(key)) weatherInfo.setWeatherIcon(jsonReader.nextString());
            else jsonReader.skipValue();
        }

        jsonReader.endObject();
    }

}
