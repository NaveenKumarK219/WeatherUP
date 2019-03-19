package com.navin.android.weatherup.data;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.navin.android.weatherup.utilities.OpenWeatherUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.Iterator;
import java.util.List;

/**
 * Created by navinkumark on 2/8/19.
 */

public class WeatherRepository {

    private static final String TAG = WeatherRepository.class.getSimpleName();
    private WeatherDao mWeatherDao;
    private LiveData<List<WeatherInfo>> mWeatherInfoList;
    private Context applicationContext;

    public WeatherRepository(Application application){
        WeatherUpDatabase weatherDB = WeatherUpDatabase.getInstance(application);
        mWeatherDao = weatherDB.weatherDao();
        mWeatherInfoList = mWeatherDao.loadWeatherData();
        applicationContext = application.getApplicationContext();
        if(mWeatherInfoList == null || (mWeatherInfoList != null && mWeatherInfoList.getValue() != null)){
            Log.i(TAG, "No  Weather Data in DB, Inserting now!");
            insertWeatherDataFromApi();
        }
    }

    public LiveData<List<WeatherInfo>> getmWeatherInfoList(){
        return mWeatherInfoList;
    }

    public void insertWeatherDataFromApi(){

        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        String locationString = defaultSharedPreferences.getString("location_key","{\"Lat\":\"17\",\"Long\":\"78\"}");
        String unitsPref = defaultSharedPreferences.getString("units_key", "metric");
        try {
            JSONObject locationJson = new JSONObject(locationString);

            URL weatherUrl = OpenWeatherUtils.buildWeatherUrlWithPreferences(locationJson.getString("Lat"), locationJson.getString("Long"), unitsPref);
            new InsertWeatherDataAsyncTask(mWeatherDao).execute(weatherUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private static class InsertWeatherDataAsyncTask extends AsyncTask<URL, Void, Void> {

        private WeatherDao asyncWeatherDao;

        public InsertWeatherDataAsyncTask(WeatherDao mWeatherDao){
            this.asyncWeatherDao = mWeatherDao;
        }

        @Override
        protected Void doInBackground(URL... urls) {
            String responseJsonString = null;
            try {
                responseJsonString = OpenWeatherUtils.getWeatherDataFromApi(urls[0]);
                List<WeatherInfo> weatherInfoList = OpenWeatherUtils.getWeatherListFromJsonResponse(responseJsonString);
                deleteOldData();
                insertWeatherList(weatherInfoList);
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
            Iterator<WeatherInfo> iterator = weatherInfoList.iterator();
            while (iterator.hasNext()){
                asyncWeatherDao.insertWeatherData(iterator.next());
            }
        }

    }

}
