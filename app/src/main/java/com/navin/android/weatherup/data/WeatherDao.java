package com.navin.android.weatherup.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * Created by navinkumark on 2/6/19.
 */
@Dao
public interface WeatherDao {

    @Query("SELECT * FROM weatherinfo")
    LiveData<List<WeatherInfo>> loadWeatherData();

    @Insert
    void insertWeatherData(WeatherInfo weatherInfo);

    @Delete
    void deleteOldWeatherData(WeatherInfo weatherInfo);

    @Query("DELETE FROM weatherinfo")
    void deleteAllWeatherData();

    @Query("SELECT * FROM weather_now")
    LiveData<WeatherNow> loadTodaysWeatherData();

    @Insert
    void insertTodaysWeatherData(WeatherNow weatherNow);

    @Query("DELETE FROM weather_now")
    void deleteTodaysOldWeatherData();

}
