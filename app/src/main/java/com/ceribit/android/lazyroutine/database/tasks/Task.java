package com.ceribit.android.lazyroutine.database.tasks;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "task_table")
public class Task {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;
    public String title;
    public String description;

    @Embedded
    private DateTime dateTime;

    @Ignore
    public Task() {
        super();
        dateTime = new DateTime();
    }

    public Task(String title, String description, DateTime dateTime) {
        // Check Title
        if(title == null || title.isEmpty()){ this.title = "No title specified."; }
        else { this.title = title; }

        // Check DateTime
        if(dateTime == null) {this.dateTime = new DateTime();}
        else { this.dateTime = dateTime; }

        // Check description (this can be empty)
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }


}
