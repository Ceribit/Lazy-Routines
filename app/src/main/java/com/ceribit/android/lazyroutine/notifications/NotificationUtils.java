package com.ceribit.android.lazyroutine.notifications;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.ceribit.android.lazyroutine.R;
import com.ceribit.android.lazyroutine.database.tasks.DateTime;
import com.ceribit.android.lazyroutine.database.tasks.Task;

import java.util.Calendar;
import java.util.List;

public class NotificationUtils {

    /*
     * This notification ID is used to access the notification after displaying it
     */
    private static final int LAZY_ROUTINE_NOTIFICATION_ID = 1204;

    /**
     * Pending intent ID used to uniquely reference the pending intent
     */
    private static final int LAZY_ROUTINE_PENDING_INTENT_ID = 5112;

    /**
     * Notification channel id used to link notifications to this channel
     */
    private static final String LAZY_ROUTINE_NOTIFICATION_CHANNEL_ID = "routine-notification-channel";

    public static final String TASK_ID = "task-id";
    public static final String TASK_TITLE = "task-title";
    public static final String TASK_DESCRIPTION = "task-description";

    /* Debugging */
    private static final String LOG_TAG = NotificationUtils.class.getSimpleName();

    /**
     * Cancels all scheduled notification and recreates them
     */
    public static void addOrReplaceAlarms(Context context, List<Task> tasks){
        Log.e(LOG_TAG, "addOrReplaceAlarms called;");
        cancelAllRepeatedNotifications(context);
        for(int i = 0; i < tasks.size(); i++){
            scheduleRepeatedTask(context, tasks.get(i));
            Log.e(LOG_TAG, tasks.get(i).getTitle());
            Log.e(LOG_TAG, tasks.get(i).getDescription());
        }
    }

    /**
     * Cancels all notifications
     */
    public static void clearAllNotifications(Context context){
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    /**
     * Creates an instant notification given a title, message, and an offset
     * @param idOffset This is an offset given to a notification to make it unique
     */
    public static void notifyUserWithMessage(Context context, String title, String message,
                                             int idOffset){
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel mChannel = new NotificationChannel(
                    LAZY_ROUTINE_NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(mChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,
                LAZY_ROUTINE_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(android.R.drawable.arrow_up_float)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(false);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }
        notificationManager.notify(LAZY_ROUTINE_NOTIFICATION_ID+idOffset, notificationBuilder.build());
    }

    /**
     * Sets a notification schedule for a given task
     */
    public static void scheduleRepeatedTask(Context context, Task task){

        // Set time ( hour and minute) that the user wishes for the notification to be given
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, task.getDateTime().getHour());
        calendar.set(Calendar.MINUTE, task.getDateTime().getMinute());

        // Set intent to the broadcast receiver that will take the task
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra(TASK_TITLE, task.getTitle());
        intent.putExtra(TASK_DESCRIPTION, task.getDescription());
        intent.putExtra(TASK_ID, task.getId());



        // Get alarm manager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Creates intent for each of the task
        if(alarmManager != null) {
            List<Boolean> dayIsRepeated = task.getDateTime().getWeekPreferences();
            for (int i = 0; i < DateTime.DAYS_IN_WEEK; i++) {

                // Create Intent for each selected day
                if (dayIsRepeated.get(i)) {

                    // Set Day
                    int calendarDay = getDayFromCalendar(i);
                    calendar.set(Calendar.DAY_OF_WEEK, calendarDay);

                    // Set alarm pending intent
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            context, LAZY_ROUTINE_NOTIFICATION_ID+task.getId()+calendarDay,
                            intent, PendingIntent.FLAG_ONE_SHOT);

                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY*7, pendingIntent);
                    Log.e(LOG_TAG, "Task: " + task.getTitle());
                    Log.e(LOG_TAG, "Time until alarm turns on " + (calendar.getTimeInMillis() - Calendar.getInstance().getTimeInMillis()));
                }
            }
        }
    }

    private static void cancelAllRepeatedNotifications(Context context){
        // Set pending intent that is used for the particular task
        Intent intent = new Intent(context, NotificationReceiver.class);

        // Create notification pending intent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, AlarmManager.RTC, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        // Get alarm manager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        try {
            alarmManager.cancel(pendingIntent);
        } catch (NullPointerException e){
            Log.e(LOG_TAG, "Notification Receiver was not canceled. " + e.toString());
        }
    }

    /**
     * Cancels the alarm intent corresponding to the given task
     */
    public static void removeNotifier(Context context, Task task){
        // Set pending intent that is used for the particular task
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra(TASK_TITLE, task.getTitle());
        intent.putExtra(TASK_DESCRIPTION, task.getDescription());
        intent.putExtra(TASK_ID, task.getId());

        // Create notification pending intent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, AlarmManager.RTC, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get alarm manager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);


        // Delete pending intent
        alarmManager.cancel(pendingIntent);
    }

    /**
     * Sets a repeated notification given it's day in a week
     * Position 0 = Sunday, 1 = Monday, 2 = Tuesday ... 6 = Saturday
     */
    private static int getDayFromCalendar(int position){
        int calendarDay;
        switch(position){
            case 0:
                calendarDay = Calendar.SUNDAY;
                break;
            case 1:
                calendarDay = Calendar.MONDAY;
                break;
            case 2:
                calendarDay = Calendar.TUESDAY;
                break;
            case 3:
                calendarDay = Calendar.WEDNESDAY;
                break;
            case 4:
                calendarDay = Calendar.THURSDAY;
                break;
            case 5:
                calendarDay = Calendar.FRIDAY;
                break;
            case 6:
                calendarDay = Calendar.SATURDAY;
                break;
            default:
                calendarDay = Calendar.SUNDAY;
        }
        return calendarDay;
    }
}
