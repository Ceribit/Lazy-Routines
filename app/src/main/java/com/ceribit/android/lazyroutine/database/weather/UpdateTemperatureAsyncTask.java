package com.ceribit.android.lazyroutine.database.weather;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.ceribit.android.lazyroutine.database.tasks.DateTime;

import java.lang.ref.WeakReference;

public class UpdateTemperatureAsyncTask extends AsyncTask<Void, Void, Void> {

    private WeakReference<Context> mContext;

    public UpdateTemperatureAsyncTask(Context context) {
        super();
        mContext = new WeakReference<>(context);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        WeatherUtils.updateTemperature(mContext.get().getApplicationContext());
        return null;
    }
}
