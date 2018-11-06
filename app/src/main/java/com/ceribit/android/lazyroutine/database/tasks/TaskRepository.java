package com.ceribit.android.lazyroutine.database.tasks;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;
import java.util.concurrent.ExecutionException;

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
    public Task getTask(int id) {
        try {
            return new singleQueryAsyncTask(mTaskDao).execute(id).get();
        } catch (Exception e) {
            return null;
        }
    }

    public void insert(Task task){
        new insertAsyncTask(mTaskDao).execute(task);
    }
    public void update(Task task){
        new updateAsyncTask(mTaskDao).execute(task);
    }
    public void delete(Task task){
        new deleteAsyncTask(mTaskDao).execute(task);
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
            if(!tasks[0].getTitle().isEmpty()) {
                mAsyncTaskDao.insert(tasks[0]);
            }
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
            if(!tasks[0].getTitle().isEmpty()) {
                mAsyncTaskDao.updateTask(tasks[0]);
            }
            return null;
        }
    }

    /**
     * Deletes task on another thread
     */
    private static class deleteAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao mAsyncTaskDao;
        deleteAsyncTask(TaskDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            mAsyncTaskDao.delete(tasks[0]);
            return null;
        }
    }

    /**
     * Retrieves a task given its id
     * // WARNING : Requires the main thread to wait to get the returned task
     */
    private static class singleQueryAsyncTask extends AsyncTask<Integer, Void, Task> {
        private TaskDao mAsyncTaskDao;
        singleQueryAsyncTask(TaskDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Task doInBackground(Integer... integers) {
            return mAsyncTaskDao.getTask(integers[0]);
        }
    }
}
