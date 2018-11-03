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

@RunWith(AndroidJUnit4.class)
public class TaskDaoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private TaskDao mTaskDao;
    private TaskRoomDatabase mDb;

    private static int TASK_MOCK_ID = 1234;
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

        int id = allTasks.get(0).getId();
        Task singleTask = LiveDataTestUtil.getValue(mTaskDao.getTask(id));
        assertEquals(singleTask.getTitle(), TASK_EMPTY_TITLE);
    }

    @Test
    public void insert_FilledTask() throws Exception {
        Task task = new Task(TASK_MOCK_TITLE, TASK_MOCK_DESCRIPTION, null);
        mTaskDao.insert(task);
        List<Task> allTasks = LiveDataTestUtil.getValue(mTaskDao.getAllTasks());
        assertEquals(allTasks.get(0).getTitle(), TASK_MOCK_TITLE );

        int id = allTasks.get(0).getId();
        Task singleTask = LiveDataTestUtil.getValue(mTaskDao.getTask(id));
        assertEquals(singleTask.getTitle(), TASK_MOCK_TITLE);
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

    @Test
    public void update_TaskUpdate() throws Exception {
        Task originalTask = new Task();
        Task updatedTask = new Task(TASK_MOCK_TITLE, TASK_MOCK_DESCRIPTION, null);
        originalTask.setId(TASK_MOCK_ID);
        updatedTask.setId(TASK_MOCK_ID);

        // Verify task was inserted
        mTaskDao.insert(originalTask);
        List<Task> allTasks = LiveDataTestUtil.getValue(mTaskDao.getAllTasks());
        assertEquals(allTasks.get(0).getTitle(), TASK_EMPTY_TITLE);

        // Verify task was updated
        mTaskDao.updateTask(updatedTask);
        allTasks = LiveDataTestUtil.getValue(mTaskDao.getAllTasks());
        assertEquals(allTasks.get(0).getTitle(), TASK_MOCK_TITLE);
        assertEquals(allTasks.get(0).getDescription(), TASK_MOCK_DESCRIPTION);
    }

    @Test
    public void delete_TaskIsDeleted() throws Exception {
        Task task = new Task();
        task.setId(TASK_MOCK_ID);

        // Insert and check if it was inserted
        mTaskDao.insert(task);
        List<Task> allTasks = LiveDataTestUtil.getValue(mTaskDao.getAllTasks());
        assertEquals(allTasks.get(0).getTitle(), TASK_EMPTY_TITLE);


        // Delete and check if it was deleted
        mTaskDao.delete(task);
        allTasks = LiveDataTestUtil.getValue(mTaskDao.getAllTasks());
        assertEquals(allTasks.size(), 0);
    }
}
