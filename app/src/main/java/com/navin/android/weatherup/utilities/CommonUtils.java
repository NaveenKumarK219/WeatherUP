package com.navin.android.weatherup.utilities;

import android.content.Context;
import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by navinkumark on 2/12/19.
 */

public class CommonUtils {


    public static String formatDateString(String dateString){
        Date inDate = new Date(dateString);
        return DateFormat.format("E d M", inDate).toString();
    }

    public static String getReadableDate(long unixTimestamp){
        Date unixDate = new Date(unixTimestamp * 1000L);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM HH:mm");
        return dateFormat.format(unixDate);
    }

    public static String formatTemperature(double temp){
        return String.valueOf((int)temp).concat("\u00B0");
    }
}
