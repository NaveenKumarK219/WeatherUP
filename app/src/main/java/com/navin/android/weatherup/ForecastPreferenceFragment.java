package com.navin.android.weatherup;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.libraries.places.compat.Place;
import com.google.android.libraries.places.compat.ui.PlaceAutocomplete;

import androidx.annotation.Nullable;
import androidx.preference.CheckBoxPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

public class ForecastPreferenceFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = ForecastPreferenceFragment.class.getSimpleName();
    private static final int AUTO_COMPLETE_REQUEST_CODE = 1;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.forecast_pref_fragment);

        PreferenceScreen prefScreen = getPreferenceScreen();
        SharedPreferences sharedPref = prefScreen.getSharedPreferences();
        int prefCount = prefScreen.getPreferenceCount();

        for (int i=0; i<prefCount; i++){
            Preference pref = prefScreen.getPreference(i);
            if(!(pref instanceof CheckBoxPreference)){
                String value = sharedPref.getString(pref.getKey(), "");
                setPreferenceSummary(pref, value);
            }
        }

        setUpPlaceAutoCompleteIntent();

    }

    private void setUpPlaceAutoCompleteIntent(){
        Preference locationPreference = findPreference("location_key");
        locationPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            boolean prefOnClickFlag = false;
            @Override
            public boolean onPreferenceClick(Preference preference) {
                try {
                    Log.i(TAG, "Initiating PlaceAutoComplete Intent!");
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(getActivity());
                    startActivityForResult(intent, AUTO_COMPLETE_REQUEST_CODE);
                    prefOnClickFlag = true;
                } catch (GooglePlayServicesRepairableException e) {
                    prefOnClickFlag = false;
                    Log.e(TAG, e.getMessage());
                    e.printStackTrace();

                } catch (GooglePlayServicesNotAvailableException e) {
                    prefOnClickFlag = false;
                    Log.e(TAG, e.getMessage());
                    e.printStackTrace();
                }

                return prefOnClickFlag;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        SharedPreferences.Editor preferences = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        if(requestCode == AUTO_COMPLETE_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                Place placeInfo = PlaceAutocomplete.getPlace(getContext(), data);
                Log.i(TAG, "Place Details: "+ placeInfo.getId() +"::"+ placeInfo.getLatLng() + "::" + placeInfo.getName());
                preferences.putString("location_key", placeInfo.getLatLng().toString());
                preferences.apply();
            } else if(resultCode == PlaceAutocomplete.RESULT_ERROR){
                Log.e(TAG, "Error Occured");
            } else if(resultCode == Activity.RESULT_CANCELED){
                Log.i(TAG, "Result Canceled");
            }
        }
    }

    private void setPreferenceSummary(Preference preference, Object value){
        String prefValue = value.toString();
        if(preference instanceof ListPreference){
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(prefValue);
            if(prefIndex >= 0){
                preference.setSummary(prefValue);
            }
        } else {
            preference.setSummary(prefValue);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        Preference preference = findPreference(key);
        if(preference != null){
            if(!(preference instanceof CheckBoxPreference)){
                setPreferenceSummary(preference, sharedPreferences.getString(key, ""));
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
