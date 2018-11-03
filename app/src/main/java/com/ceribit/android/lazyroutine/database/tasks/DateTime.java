package com.ceribit.android.lazyroutine.database.tasks;

import java.util.ArrayList;
import java.util.List;

public class DateTime {
    private int hour;
    private int minute;

    private boolean onSunday;
    private boolean onMonday;
    private boolean onTuesday;
    private boolean onWednesday;
    private boolean onThursday;
    private boolean onFriday;
    private boolean onSaturday;

    public DateTime() {
        hour = 0;
        minute = 0;
        initializeWeekDays();
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
}


