package com.ceribit.android.lazyroutine;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ceribit.android.lazyroutine.database.tasks.Task;
import com.ceribit.android.lazyroutine.database.tasks.TaskViewModel;
import com.ceribit.android.lazyroutine.database.weather.UpdateTemperatureAsyncTask;
import com.ceribit.android.lazyroutine.database.weather.WeatherPreferences;
import com.ceribit.android.lazyroutine.database.weather.WeatherUtils;
import com.ceribit.android.lazyroutine.notifications.NotificationUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TaskViewModel mTaskViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WeatherPreferences.init(this);
        WeatherPreferences.setCity("Houston");

        new UpdateTemperatureAsyncTask(this).execute();

        RecyclerView recyclerView = findViewById(R.id.main_container);
        final TaskAdapter adapter = new TaskAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mTaskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);

        mTaskViewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable List<Task> tasks) {
                adapter.setTasks(tasks);
                NotificationUtils.addOrReplaceAlarms(getBaseContext(), tasks);
            }
        });
    }

    public void goToAddTask(View view){
        Intent intent = new Intent(this, AddOrUpdateTask.class);
        //NotificationUtils.notifyUserWithMessage(this, "Hello world");
        startActivity(intent);
    }
}
