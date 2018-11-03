package com.ceribit.android.lazyroutine;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ceribit.android.lazyroutine.database.tasks.Task;
import com.ceribit.android.lazyroutine.database.tasks.TaskViewModel;

public class AddOrUpdateTask extends AppCompatActivity {

    private TaskViewModel mTaskViewModel;
    private EditText mTitleView;
    private EditText mDescriptionView;

    public static String TASK_ID = "task-id";
    public static String TASK_TITLE = "task-title";
    public static String TASK_DESCRIPTION = "task-description";

    public static int NO_TASK = -1;
    public static String EMPTY_TASK = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_update_task);

        mTaskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);

        mTitleView = findViewById(R.id.et_task_title);
        mDescriptionView = findViewById(R.id.et_task_description);
        Button button = findViewById(R.id.btn_add_task);

        final int taskId = getIntent().getIntExtra(TASK_ID, NO_TASK);

        if(taskId == NO_TASK) {
            button.setText(getString(R.string.btn_add));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mTaskViewModel.insert(getTask());
                    onBackPressed();
                }
            });
        } else {
            // Populate the fields with existing data and set button to update instead of save
            populateViews();
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Task task = getTask();
                    task.setId(taskId);
                    mTaskViewModel.update(task);
                    onBackPressed();
                }
            });
        }
     }

     private Task getTask(){
         Task task = new Task();
         String title = mTitleView.getText().toString();
         String description = mDescriptionView.getText().toString();
         task.setTitle(title);
         task.setDescription(description);
         return task;
     }

     private void populateViews(){
         Intent intent = getIntent();
         mTitleView.setText(intent.getStringExtra(TASK_TITLE));
         mDescriptionView.setText(intent.getStringExtra(TASK_DESCRIPTION));
     }
}
