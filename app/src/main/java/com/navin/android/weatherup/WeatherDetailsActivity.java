package com.navin.android.weatherup;

import android.os.Bundle;

import com.navin.android.weatherup.data.WeatherNow;
import com.navin.android.weatherup.data.WeatherRepository;
import com.navin.android.weatherup.databinding.ActivityWeatherDetailsBinding;
import com.navin.android.weatherup.utilities.CommonUtils;

import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

public class WeatherDetailsActivity extends AppCompatActivity {

    private ActivityWeatherDetailsBinding dataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_details);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_weather_details);
        getTodaysWeatherDetails();
    }

    private void getTodaysWeatherDetails(){
        WeatherRepository weatherRepository = new WeatherRepository(getApplication());
        LiveData<WeatherNow> weatherNow = weatherRepository.getmTodaysWeatherInfo();

        weatherNow.observe(this, new Observer<WeatherNow>() {
            @Override
            public void onChanged(WeatherNow weatherNow) {
                if(weatherNow != null){
                    Map<String,String> formatedWeatherData = CommonUtils.formatWeatherNowData(weatherNow);
                    dataBinding.tvCityname.setText(weatherNow.getCityName());
                    dataBinding.tvWeatherDate.setText(CommonUtils.getReadableDate(weatherNow.getEpochTime()));
                    int iconRes = getResources().getIdentifier("ic_"+weatherNow.getWeatherIcon(), "drawable", getPackageName());
                    dataBinding.ivWeatherIcon.setImageResource(iconRes);
                    dataBinding.tvWeatherDesc.setText(weatherNow.getWeatherDesc());
                    dataBinding.tvMaxTemp.setText(formatedWeatherData.get("maxtemp"));
                    dataBinding.tvMinTemp.setText(formatedWeatherData.get("mintemp"));
                    dataBinding.tvPressure.setText(formatedWeatherData.get("pressure"));
                    dataBinding.tvHumidity.setText(formatedWeatherData.get("humidity"));
                    dataBinding.tvWindSpeed.setText(formatedWeatherData.get("windspeed"));
                    dataBinding.tvSunrise.setText(formatedWeatherData.get("sunrise"));
                    dataBinding.tvSunset.setText(formatedWeatherData.get("sunset"));
                }
            }
        });
    }
}
