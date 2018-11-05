package com.ceribit.android.lazyroutine.database.weather;

import android.app.Activity;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

public class UpdateTemperatureAsyncTask extends AsyncTask<Void, Void, Void> {

    private WeakReference<Activity> preferenceActivity;

    public UpdateTemperatureAsyncTask(Activity activity) {
        super();
        preferenceActivity = new WeakReference<>(activity);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        WeatherUtils.updateTemperature(preferenceActivity.get());
        return null;
    }
}
