package com.ceribit.android.lazyroutine.notifications;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ceribit.android.lazyroutine.MainActivity;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Relaunch app when user clicks on notification
        Intent intentToRepeat = new Intent(context, MainActivity.class);
        int taskId = intent.getIntExtra(NotificationUtils.TASK_ID, 0);
        String taskTitle = intent.getStringExtra(NotificationUtils.TASK_TITLE);
        String taskDescription = intent.getStringExtra(NotificationUtils.TASK_DESCRIPTION);

        Log.e("NotificationReceiever", "Task receieved: "
                + "\nID: " + taskId
                + "\nTitle: " + taskTitle
                + "\nDesc: " + taskDescription
                + "\n---------------------------------------");
        // Set flag to restart/relaunch app
        intentToRepeat.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Pending intent to handle launch of activity
        NotificationUtils.notifyUserWithMessage(context, taskTitle, taskDescription, taskId) ;
    }


}
