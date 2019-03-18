package com.navin.android.weatherup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.SupportMapFragment;
import com.here.android.mpa.search.DiscoveryLink;
import com.here.android.mpa.search.DiscoveryRequest;
import com.here.android.mpa.search.DiscoveryResultPage;
import com.here.android.mpa.search.ErrorCode;
import com.here.android.mpa.search.Place;
import com.here.android.mpa.search.PlaceLink;
import com.here.android.mpa.search.ResultListener;
import com.here.android.mpa.search.SearchRequest;
import com.navin.android.weatherup.data.WeatherInfo;
import com.navin.android.weatherup.data.WeatherRepository;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static boolean PREFERENCES_HAVE_CHANGED = false;
    private static final int AUTO_COMPLETE_REQUEST_CODE = 1;

    private RecyclerView mForecastRecyclerView;
    private ForecastAdapter mForecastAdapter;
    private ProgressBar mLoadingIndicator;
    private WeatherRepository mWeatherRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mForecastRecyclerView = findViewById(R.id.rv_weather_forecast);
        mLoadingIndicator = findViewById(R.id.pb_loading_weather);

        mForecastAdapter = new ForecastAdapter(this);
        mForecastRecyclerView.setAdapter(mForecastAdapter);
        mForecastRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mForecastRecyclerView.setHasFixedSize(true);

        mWeatherRepository = new WeatherRepository(getApplication());
        getWeatherData();

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);


    }

    private void getWeatherData() {
        LiveData<List<WeatherInfo>> weatherLiveData = mWeatherRepository.getmWeatherInfoList();

        weatherLiveData.observe(this, new Observer<List<WeatherInfo>>() {
            @Override
            public void onChanged(@Nullable List<WeatherInfo> weatherInfoList) {
                mForecastAdapter.setWeatherData(weatherInfoList);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.action_refresh){
            mLoadingIndicator.setVisibility(View.VISIBLE);
            mWeatherRepository.insertWeatherDataFromApi();
            getWeatherData();
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            return true;

        } else if(itemId == R.id.action_settings){
            Intent settingsIntent = new Intent(this, ForecastSettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        PREFERENCES_HAVE_CHANGED = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(PREFERENCES_HAVE_CHANGED){
            mWeatherRepository.insertWeatherDataFromApi();
            getWeatherData();
            PREFERENCES_HAVE_CHANGED = false;
        }
    }
}
