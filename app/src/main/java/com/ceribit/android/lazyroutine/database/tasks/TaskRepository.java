package com.ceribit.android.lazyroutine.database.tasks;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

public class TaskRepository {
    private TaskDao  mTaskDao;
    private LiveData<List<Task>> mAllTasks;

    TaskRepository(Application application) {
        TaskRoomDatabase db = TaskRoomDatabase.getDatabase(application);
        mTaskDao = db.taskDao();
        mAllTasks = mTaskDao.getAllTasks();
    }

    LiveData<List<Task>> getAllTasks(){
        return mTaskDao.getAllTasks();
    }

    public void insert(Task task){
        new insertAsyncTask(mTaskDao).execute(task);
    }
    public void update(Task task){
        new updateAsyncTask(mTaskDao).execute(task);
    }


    /**
     * Inserts task on another thread
     */
    private static class insertAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao mAsyncTaskDao;
        insertAsyncTask(TaskDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            Log.e("NewTaskAdded","Done");
            mAsyncTaskDao.insert(tasks[0]);
            return null;
        }
    }

    /**
     * Updates task on another thread
     */
    private static class updateAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao mAsyncTaskDao;
        updateAsyncTask(TaskDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            Log.e("TaskUpdated","Done");
            mAsyncTaskDao.updateTask(tasks[0]);
            return null;
        }
    }
}
