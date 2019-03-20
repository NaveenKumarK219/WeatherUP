package com.navin.android.weatherup.data;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.navin.android.weatherup.utilities.OpenWeatherUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.preference.PreferenceManager;

/**
 * Created by navinkumark on 2/8/19.
 */

public class WeatherRepository {

    private static final String TAG = WeatherRepository.class.getSimpleName();
    private WeatherDao mWeatherDao;
    private LiveData<List<WeatherInfo>> mWeatherInfoList;
    private LiveData<WeatherNow> mTodaysWeatherInfo;
    private Context applicationContext;

    public WeatherRepository(Application application){
        WeatherUpDatabase weatherDB = WeatherUpDatabase.getInstance(application);
        mWeatherDao = weatherDB.weatherDao();
        mWeatherInfoList = mWeatherDao.loadWeatherData();
        mTodaysWeatherInfo = mWeatherDao.loadTodaysWeatherData();
        applicationContext = application.getApplicationContext();
        if(mWeatherInfoList == null || (mWeatherInfoList != null && mWeatherInfoList.getValue() != null)){
            Log.i(TAG, "No  Weather Data in DB, Inserting now!");
            insertWeatherDataFromApi();
        }
        if(mTodaysWeatherInfo == null){
            Log.i(TAG, "Inserting Todays Weather Data");
            insertWeatherDataFromApi();
        }
    }

    public LiveData<List<WeatherInfo>> getmWeatherInfoList(){
        return mWeatherInfoList;
    }
    public LiveData<WeatherNow> getmTodaysWeatherInfo(){
        return mTodaysWeatherInfo;
    }

    public void insertWeatherDataFromApi(){

        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        String locationString = defaultSharedPreferences.getString("location_key","{\"Lat\":\"17\",\"Long\":\"78\"}");
        String unitsPref = defaultSharedPreferences.getString("units_key", "metric");
        try {
            JSONObject locationJson = new JSONObject(locationString);
            URL[] weatherUrls = new URL[2];
            weatherUrls[0] = OpenWeatherUtils.buildWeatherUrlWithPreferences(locationJson.getString("Lat"), locationJson.getString("Long"), unitsPref);
            weatherUrls[1] = OpenWeatherUtils.buildTodaysWeatherUrlWithPreferences(locationJson.getString("Lat"), locationJson.getString("Long"), unitsPref);
            new InsertWeatherDataAsyncTask(mWeatherDao).execute(weatherUrls);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private static class InsertWeatherDataAsyncTask extends AsyncTask<URL[], Void, Void> {

        private WeatherDao asyncWeatherDao;

        InsertWeatherDataAsyncTask(WeatherDao mWeatherDao){
            this.asyncWeatherDao = mWeatherDao;
        }

        @Override
        protected Void doInBackground(URL[]... urls) {
            String responseJsonString = null;
            URL[] weatherUrls = urls[0];
            try {
                // Forecast Api Calling
                responseJsonString = OpenWeatherUtils.getWeatherDataFromApi(weatherUrls[0]);
                List<WeatherInfo> weatherInfoList = OpenWeatherUtils.getWeatherListFromJsonResponse(responseJsonString);
                deleteOldData();
                if(weatherInfoList != null){
                    insertWeatherList(weatherInfoList);
                }
                // Current Day Weather Api Calling
                responseJsonString = OpenWeatherUtils.getWeatherDataFromApi(weatherUrls[1]);
                WeatherNow weatherNow = OpenWeatherUtils.getWeatherNowFromJsonResponse(responseJsonString);
                asyncWeatherDao.deleteTodaysOldWeatherData();
                if(weatherNow != null){
                    asyncWeatherDao.insertTodaysWeatherData(weatherNow);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error While calling API:"+e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        private void deleteOldData() {
            Log.i(TAG, "DELETING OLD DATA...");
            asyncWeatherDao.deleteAllWeatherData();
        }

        private void insertWeatherList(List<WeatherInfo> weatherInfoList){
            Log.i(TAG, "INSERTING NEW DATA...");
            for (WeatherInfo aWeatherInfoList : weatherInfoList) {
                asyncWeatherDao.insertWeatherData(aWeatherInfoList);
            }
        }

    }

}
