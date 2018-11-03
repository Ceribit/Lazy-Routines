package com.ceribit.android.lazyroutine.database.tasks;

import java.util.ArrayList;
import java.util.List;

public class DateTime {

    /*
     * Standard Hour (24H) and minute variables
     */
    private int hour;
    private int minute;

    /*
     * Booleans which specify the days of the week want to be repeated
     *  (This is not an array since this will be used for databases)
     */
    private boolean onSunday;
    private boolean onMonday;
    private boolean onTuesday;
    private boolean onWednesday;
    private boolean onThursday;
    private boolean onFriday;
    private boolean onSaturday;

    /*
     * Constants representing the position of the day in an array
     */
    public static int SUNDAY = 0;
    public static int MONDAY = 1;
    public static int TUESDAY = 2;
    public static int WEDNESDAY = 3;
    public static int THURSDAY = 4;
    public static int FRIDAY = 5;
    public static int SATURDAY = 6;

    /*
     * Constant to iterate through an array of a week
     */
    public static int DAYS_IN_WEEK = 7;

    public DateTime() {
        hour = 0;
        minute = 0;
        initializeWeekDays();
    }

    public DateTime(int hour, int minute){
        this.hour = hour;
        this.minute = minute;
        initializeWeekDays();
    }

    /*
     * Constructs an array of booleans representing the week
     */
    public List<Boolean> getWeekPreferences() {
        List<Boolean> weekPreference = new ArrayList<>();
        weekPreference.add(onSunday);
        weekPreference.add(onMonday);
        weekPreference.add(onTuesday);
        weekPreference.add(onWednesday);
        weekPreference.add(onThursday);
        weekPreference.add(onFriday);
        weekPreference.add(onSaturday);
        return weekPreference;
    }

    /*
     * Sets the repeated days of the week given a boolean of values
     */
    public void setWeekDays(List<Boolean> weekDays){
        if(weekDays.size() == 7) {
            onSunday = weekDays.get(SUNDAY);
            onMonday = weekDays.get(MONDAY);
            onTuesday = weekDays.get(TUESDAY);
            onWednesday = weekDays.get(WEDNESDAY);
            onThursday = weekDays.get(THURSDAY);
            onFriday = weekDays.get(FRIDAY);
            onSaturday = weekDays.get(SATURDAY);
        }
    }

    private void initializeWeekDays() {
        onSunday = false;
        onMonday = false;
        onTuesday = false;
        onWednesday = false;
        onThursday = false;
        onFriday = false;
        onSaturday = false;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public boolean isOnSunday() {
        return onSunday;
    }

    public void setOnSunday(boolean onSunday) {
        this.onSunday = onSunday;
    }

    public boolean isOnMonday() {
        return onMonday;
    }

    public void setOnMonday(boolean onMonday) {
        this.onMonday = onMonday;
    }

    public boolean isOnTuesday() {
        return onTuesday;
    }

    public void setOnTuesday(boolean onTuesday) {
        this.onTuesday = onTuesday;
    }

    public boolean isOnWednesday() {
        return onWednesday;
    }

    public void setOnWednesday(boolean onWednesday) {
        this.onWednesday = onWednesday;
    }

    public boolean isOnThursday() {
        return onThursday;
    }

    public void setOnThursday(boolean onThursday) {
        this.onThursday = onThursday;
    }

    public boolean isOnFriday() {
        return onFriday;
    }

    public void setOnFriday(boolean onFriday) {
        this.onFriday = onFriday;
    }

    public boolean isOnSaturday() {
        return onSaturday;
    }

    public void setOnSaturday(boolean onSaturday) {
        this.onSaturday = onSaturday;
    }
}


