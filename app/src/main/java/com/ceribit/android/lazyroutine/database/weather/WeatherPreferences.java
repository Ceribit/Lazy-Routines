package com.ceribit.android.lazyroutine.database.weather;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ceribit.android.lazyroutine.database.tasks.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WeatherPreferences {

    // Shared Preference Keys
    private static final String CITY_KEY = "city-key";
    private static final String UNIT_KEY = "unit-key";
    private static final String LOW_TEMP_KEY = "low-temperature-key";
    private static final String HIGH_TEMP_KEY = "high-temperature-key";
    private static final String MONDAY_KEY = "weather-monday";
    private static final String TUESDAY_KEY = "weather-tuesday";
    private static final String WEDNESDAY_KEY = "weather-wednesday";
    private static final String THURSDAY_KEY = "weather-thursday";
    private static final String FRIDAY_KEY = "weather-friday";
    private static final String SATURDAY_KEY = "weather-saturday";
    private static final String SUNDAY_KEY = "weather-sunday";
    private static final String HOUR_KEY = "weather-hour";
    private static final String MINUTE_KEY = "weather-minute";

    private static SharedPreferences INSTANCE;

    public static void init(Context context){
        if(INSTANCE == null) {
            INSTANCE = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
        }
    }


    /**
     * Sets the city the user is located in
     */
    public static void setCity(String city){
        if(INSTANCE != null) {
            SharedPreferences.Editor editor = INSTANCE.edit();
            editor.putString(CITY_KEY, city).apply();
        }
    }

    /**
     * Gets the city the user is located in
     */
    public static String getCity(){
        if(INSTANCE != null) {
            return INSTANCE.getString(CITY_KEY, "");
        }
        return "";
    }

    /**
     * Sets the [temperature] unit type that the user wants to retrieve from the weather API
     * Can be either METRIC or IMPERIAL
     */
    public static void setUnits(String units){
        if(INSTANCE != null){
            SharedPreferences.Editor editor = INSTANCE.edit();
            editor.putString(UNIT_KEY, units).apply();
        }
    }

    /**
     * Sets the [temperature] unit type that the user wants to retrieve from the weather API
     * Can be either METRIC(Kelvin) or IMPERIAL(Fahrenheit)
     */
    public static String getUnits(){
        if(INSTANCE != null) {
            return INSTANCE.getString(UNIT_KEY, "imperial");
        }
        return "metric";
    }

    /**
     * Sets the minimum temperature that the user wants to retrieve from the weather API
     * Can be either METRIC or IMPERIAL
     */
    public static void setLowTemperature(float low){
        if(INSTANCE != null) {
            SharedPreferences.Editor editor = INSTANCE.edit();
            editor.putFloat(LOW_TEMP_KEY, low).apply();
        }
    }

    /**
     * Gets the minimum temperature that the user wants to retrieve from the weather API
     * Can be either METRIC or IMPERIAL
     */
    public static float getLowTemperature(){
        if(INSTANCE != null) {
            return INSTANCE.getFloat(LOW_TEMP_KEY,  0.0f);
        }
        return 0.0f;
    }

    /**
     * Sets the maximum temperature that the user wants to retrieve from the weather API
     * Can be either METRIC or IMPERIAL
     */
    public static void storeHighTemperature(float high){
        if(INSTANCE != null) {
            SharedPreferences.Editor editor = INSTANCE.edit();
            editor.putFloat(HIGH_TEMP_KEY, high).apply();
        }
    }

    /**
     * Gets the maximum temperature that the user wants to retrieve from the weather API
     * Can be either METRIC or IMPERIAL
     */
    public static float getHighTemperature(){
        if(INSTANCE != null) {
            return INSTANCE.getFloat(HIGH_TEMP_KEY, 0.0f);
        }
        return 0.0f;
    }

    /**
     * Returns formatted string for temperatures
     */
    public static String getFormattedTemperature(float temperature, String type){
        return type + ": " + String.format(Locale.getDefault(), "%.1f", temperature);
    }

    /**
     * Get DateTime object using preferences
     */
    public static DateTime getDateTime(){
        int hour = INSTANCE.getInt(HOUR_KEY, 0);
        int minute = INSTANCE.getInt(MINUTE_KEY, 0);
        List<Boolean> weekPreference = new ArrayList<>();
        weekPreference.add(INSTANCE.getBoolean(SUNDAY_KEY, true));
        weekPreference.add(INSTANCE.getBoolean(MONDAY_KEY, true));
        weekPreference.add(INSTANCE.getBoolean(TUESDAY_KEY, true));
        weekPreference.add(INSTANCE.getBoolean(WEDNESDAY_KEY, true));
        weekPreference.add(INSTANCE.getBoolean(THURSDAY_KEY, true));
        weekPreference.add(INSTANCE.getBoolean(FRIDAY_KEY, true));
        weekPreference.add(INSTANCE.getBoolean(SATURDAY_KEY, true));
        DateTime dateTime = new DateTime(hour,minute);
        dateTime.setWeekDays(weekPreference);
        return dateTime;
    }
}
