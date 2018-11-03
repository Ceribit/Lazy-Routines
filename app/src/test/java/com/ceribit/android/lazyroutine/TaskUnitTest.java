package com.ceribit.android.lazyroutine;

import com.ceribit.android.lazyroutine.database.tasks.Task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class TaskUnitTest {

    @Test
    public void emptyTask_HasCorrectDefaults(){
        Task task = new Task("","");
        assertEquals(task.getId(), 0);
        assertThat(task.getTitle(), not(""));
        assertEquals(task.getDescription(), "");
    }
    @Test
    public void emptyConstructor_HasCorrectDefaults(){
        Task task = new Task();
        assertEquals(task.getId(), 0);
        assertEquals(task.getTitle(), null);
        assertEquals(task.getDescription(), null);
    }
    @Test
    public void emptyTask_CorrectWeek(){
        Task task = new Task("","");
        assertEquals(task.getId(), 0);
        assertThat(task.getTitle(), not(""));
        assertEquals(task.getDescription(), "");
    }

}
