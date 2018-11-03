package com.ceribit.android.lazyroutine;

import com.ceribit.android.lazyroutine.database.tasks.DateTime;


import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class DateTimeUnitTests {
    @Test
    public void emptyTask_HasCorrectDefaults(){
        DateTime test = new DateTime();
        List<Boolean> testArray = test.getWeekPreferences();
        for(int i = 0; i < testArray.size(); i++){
            assertEquals(testArray.get(i), false);
        }
    }
}
