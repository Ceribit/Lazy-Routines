package com.ceribit.android.lazyroutine.database.tasks;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    void insert(Task task);

    @Query("DELETE FROM task_table")
    void deleteAll();

    @Update
    void updateTask(Task task);

    @Query("SELECT * from task_table")
    LiveData<List<Task>> getAllTasks();

    @Query("SELECT * from task_table WHERE id = :id")
    LiveData<Task> getTask(int id);
}
