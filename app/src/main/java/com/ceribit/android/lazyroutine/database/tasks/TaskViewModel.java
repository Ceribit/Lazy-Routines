package com.ceribit.android.lazyroutine.database.tasks;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.widget.Toast;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private TaskRepository mRepository;
    private LiveData<List<Task>> mAllTasks;

    public TaskViewModel (Application application){
        super(application);
        mRepository = new TaskRepository(application);
        mAllTasks = mRepository.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks() { return mAllTasks; }

    public void insert(Task task) { mRepository.insert(task); }

    public void update(Task task) { mRepository.update(task); }

    public Task getTask(int id){ return mRepository.getTask(id); }

    public void delete(Task task) {mRepository.delete(task); }

}
