package com.ceribit.android.lazyroutine.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ceribit.android.lazyroutine.database.weather.UpdateTemperatureAsyncTask;
import com.ceribit.android.lazyroutine.database.weather.WeatherPreferences;

import java.util.concurrent.ExecutionException;

public class WeatherNotificationReceiever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("OnReceived", "ONREECEIVEED CALLED");

        // Get and wait for Weather information to be received
        //try {
            new UpdateTemperatureAsyncTask(context).execute();
//        } catch (ExecutionException e){
//            Log.e("WeatherReceiver", "Issue when getting values " + e.toString());
//        } catch (InterruptedException e){
//            Log.e("WeatherReceiver", "Issue when getting values " + e.toString());
//        }

        // Get weather information and create a notification from it
        String title = "Weather Notification";
        String message = "Today's temperature high is " + WeatherPreferences.getHighTemperature() +
                " and Today's Low Temperature is " + WeatherPreferences.getLowTemperature();
        NotificationUtils.notifyUserWithMessage(context, title, message, 0);
    }
}
