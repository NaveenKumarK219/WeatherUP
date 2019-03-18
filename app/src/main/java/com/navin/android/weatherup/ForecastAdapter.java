package com.navin.android.weatherup;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.navin.android.weatherup.data.WeatherInfo;
import com.navin.android.weatherup.utilities.CommonUtils;

import java.util.List;

/**
 * Created by navinkumark on 2/6/19.
 */

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {

    private static final String TAG = ForecastAdapter.class.getSimpleName();
    private Context mContext;
    private List<WeatherInfo> mWeatherInfoList;

    public ForecastAdapter(Context context){
        this.mContext = context;
    }

    public void setWeatherData(List<WeatherInfo> weatherData){
        this.mWeatherInfoList = weatherData;
        notifyDataSetChanged();
    }

    public List<WeatherInfo> getWeatherInfoList(){
        return mWeatherInfoList;
    }

    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View forecastView = LayoutInflater.from(mContext).inflate(R.layout.forecast_list, parent, false);
        forecastView.setFocusable(true);

        return new ForecastAdapterViewHolder(forecastView);
    }

    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder holder, int position) {

        WeatherInfo weatherInfo = mWeatherInfoList.get(position);
        String weatherDate = CommonUtils.getReadableDate(mContext, weatherInfo.getEpochTime());
        String tempInfo = String.valueOf(weatherInfo.getTemperature()) + "\u00B0C";
        String weatherDesc = weatherInfo.getWeatherDescription();
        String weatherSummary = weatherDate +" - "+ tempInfo +" - "+ weatherDesc;

        holder.mForecastDataView.setText(weatherSummary);

    }

    @Override
    public int getItemCount() {
        if(mWeatherInfoList != null && mWeatherInfoList.size()>0){
            return mWeatherInfoList.size();
        } else
            return 0;
    }

    public class ForecastAdapterViewHolder extends RecyclerView.ViewHolder{

        private TextView mForecastDataView;

        public ForecastAdapterViewHolder(View itemView) {
            super(itemView);
            mForecastDataView = itemView.findViewById(R.id.tv_forecast_data);
        }
    }
}
