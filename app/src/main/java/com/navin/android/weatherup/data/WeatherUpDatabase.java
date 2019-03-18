package com.navin.android.weatherup.data;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

/**
 * Created by navinkumark on 2/6/19.
 */
@Database(entities = {WeatherInfo.class}, version = 2, exportSchema = false)
public abstract class WeatherUpDatabase extends RoomDatabase {

    private static final String TAG = WeatherUpDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "weatherupdata";
    private static WeatherUpDatabase sInstance;

    public static WeatherUpDatabase getInstance(Context context){

        if(sInstance == null){
            synchronized (LOCK){
                Log.i(TAG, "New Database instance is created");
                sInstance = Room.databaseBuilder(context.getApplicationContext(), WeatherUpDatabase.class, DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }

        Log.i(TAG, "Returning Database instance");
        return sInstance;
    }

    public abstract WeatherDao weatherDao();

}
