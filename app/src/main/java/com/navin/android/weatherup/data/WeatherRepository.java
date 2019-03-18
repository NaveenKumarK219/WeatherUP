package com.navin.android.weatherup.data;

import android.app.Application;
import androidx.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.navin.android.weatherup.utilities.OpenWeatherUtils;

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

    public WeatherRepository(Application application){
        WeatherUpDatabase weatherDB = WeatherUpDatabase.getInstance(application);
        mWeatherDao = weatherDB.weatherDao();
        mWeatherInfoList = mWeatherDao.loadWeatherData();
        if(mWeatherInfoList == null || (mWeatherInfoList != null && mWeatherInfoList.getValue() != null)){
            Log.i(TAG, "No  Weather Data in DB, Inserting now!");
            insertWeatherDataFromApi();
        }
    }

    public LiveData<List<WeatherInfo>> getmWeatherInfoList(){
        return mWeatherInfoList;
    }

    public void insertWeatherDataFromApi(){
        URL weatherUrl = OpenWeatherUtils.buildWeatherUrlWithLatLon("17","74");
        new InsertWeatherDataAsyncTask(mWeatherDao).execute(weatherUrl);

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
