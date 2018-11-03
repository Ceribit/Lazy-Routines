package com.ceribit.android.lazyroutine;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.ceribit.android.lazyroutine.database.tasks.Task;
import com.ceribit.android.lazyroutine.database.tasks.TaskDao;
import com.ceribit.android.lazyroutine.database.tasks.TaskRoomDatabase;
import com.ceribit.android.lazyroutine.testUtilites.LiveDataTestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class TaskDaoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private TaskDao mTaskDao;
    private TaskRoomDatabase mDb;

    private static String TASK_MOCK_TITLE = "mock-title";
    private static String TASK_MOCK_DESCRIPTION = "mock-description";
    private static String TASK_EMPTY_TITLE = "No title specified.";

    @Before
    public void createDb(){
        Context context = InstrumentationRegistry.getTargetContext();

        mDb = Room.inMemoryDatabaseBuilder(context, TaskRoomDatabase.class)
                .allowMainThreadQueries()
                .build();
        mTaskDao = mDb.taskDao();
    }

    @After
    public void closeDb(){
        mDb.close();
    }

    @Test
    public void insert_EmptyTask() throws Exception {
        Task task = new Task();
        mTaskDao.insert(task);
        List<Task> allTasks = LiveDataTestUtil.getValue(mTaskDao.getAllTasks());
        assertEquals(allTasks.get(0).getTitle(), TASK_EMPTY_TITLE );
    }

    @Test
    public void insert_FilledTask() throws Exception {
        Task task = new Task(TASK_MOCK_TITLE, TASK_MOCK_DESCRIPTION, null);
        mTaskDao.insert(task);
        List<Task> allTasks = LiveDataTestUtil.getValue(mTaskDao.getAllTasks());
        assertEquals(allTasks.get(0).getTitle(), TASK_MOCK_TITLE );
    }

    @Test
    public void insert_CheckDateTimeRetrieval() throws Exception {
        Task task = new Task();
        mTaskDao.insert(task);
        List<Task> allTasks = LiveDataTestUtil.getValue(mTaskDao.getAllTasks());
        List<Boolean> daysOfTheWeek = allTasks.get(0).getDateTime().getWeekPreferences();
        for(int i = 0; i < daysOfTheWeek.size(); i++){
            assertEquals(daysOfTheWeek.get(i), false);
        }
    }
}
