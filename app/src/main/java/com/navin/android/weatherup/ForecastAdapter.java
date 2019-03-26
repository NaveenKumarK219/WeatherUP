package com.navin.android.weatherup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.navin.android.weatherup.data.WeatherInfo;
import com.navin.android.weatherup.utilities.CommonUtils;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

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

        holder.mDateTextView.setText(CommonUtils.getReadableDate(weatherInfo.getEpochTime()));
        holder.mForecastDescView.setText(weatherInfo.getWeatherDescription());
        holder.mPressureView.setText(CommonUtils.formatWeatherInfo(weatherInfo.getPressure(), "pressure"));
        holder.mHumidity.setText(CommonUtils.formatWeatherInfo(weatherInfo.getHumidity(), "humidity"));
        holder.mHighTempView.setText(CommonUtils.formatWeatherInfo(weatherInfo.getMaxTemp(), "temperature"));
        holder.mLowTempView.setText(CommonUtils.formatWeatherInfo(weatherInfo.getMinTemp(), "temperature"));
        int iconRes = mContext.getResources().getIdentifier("ic_"+weatherInfo.getWeatherIcon(), "drawable", mContext.getPackageName());
        holder.mForecastImgView.setImageResource(iconRes);

    }

    @Override
    public int getItemCount() {
        if(mWeatherInfoList != null && mWeatherInfoList.size()>0){
            return mWeatherInfoList.size();
        } else
            return 0;
    }

    public class ForecastAdapterViewHolder extends RecyclerView.ViewHolder{

        private ImageView mForecastImgView;
        private TextView mDateTextView;
        private TextView mForecastDescView;
        private TextView mHighTempView;
        private TextView mLowTempView;
        private TextView mPressureView;
        private TextView mHumidity;

        public ForecastAdapterViewHolder(View itemView) {
            super(itemView);
            mForecastImgView = itemView.findViewById(R.id.weather_icon_iv);
            mDateTextView = itemView.findViewById(R.id.forecast_date_tv);
            mForecastDescView = itemView.findViewById(R.id.forecast_desc_tv);
            mHighTempView = itemView.findViewById(R.id.high_temp_tv);
            mLowTempView = itemView.findViewById(R.id.low_temp_tv);
            mPressureView = itemView.findViewById(R.id.pressure_tv);
            mHumidity = itemView.findViewById(R.id.humidity_tv);
        }
    }
}
