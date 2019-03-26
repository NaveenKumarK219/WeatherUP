package com.navin.android.weatherup;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.navin.android.weatherup.data.WeatherNow;
import com.navin.android.weatherup.data.WeatherRepository;
import com.navin.android.weatherup.databinding.ActivityWeatherDetailsBinding;
import com.navin.android.weatherup.utilities.CommonUtils;

import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
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

    private Intent shareForecastIntent(){

        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText("WeatherUP : \n" + dataBinding.tvCityname.getText() + "\n"
                        + dataBinding.tvWeatherDate.getText() + "\n"
                        + "Max Temp: " + dataBinding.tvMaxTemp.getText() + "\n"
                        + "Min Temp: " + dataBinding.tvMinTemp.getText() + "\n"
                        + dataBinding.tvPressure.getText() + "\n"
                        + dataBinding.tvHumidity.getText())
                .setChooserTitle("Share Weather Data To...")
                .createChooserIntent();

        return shareIntent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        MenuItem item = menu.findItem(R.id.action_share);
        item.setIntent(shareForecastIntent());
        return true;
    }


}
