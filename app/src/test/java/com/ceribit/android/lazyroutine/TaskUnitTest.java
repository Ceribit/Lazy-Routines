package com.ceribit.android.lazyroutine;

import com.ceribit.android.lazyroutine.database.tasks.Task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

public class TaskUnitTest {

    @Test
    public void emptyTask_HasCorrectDefaults(){
        Task task = new Task();
        assertEquals(task.getId(), 0);
    }
}
