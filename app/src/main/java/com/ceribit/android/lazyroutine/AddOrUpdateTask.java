package com.ceribit.android.lazyroutine;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;

import com.ceribit.android.lazyroutine.database.tasks.DateTime;
import com.ceribit.android.lazyroutine.database.tasks.Task;
import com.ceribit.android.lazyroutine.database.tasks.TaskViewModel;
import com.ceribit.android.lazyroutine.notifications.NotificationUtils;

import java.util.ArrayList;
import java.util.List;

public class AddOrUpdateTask extends AppCompatActivity {

    // ModelView
    private TaskViewModel mTaskViewModel;

    // Set of Views to be used
    private EditText mTitleView;
    private EditText mDescriptionView;
    private CheckBox[] mWeekCheckBoxes;
    private TimePicker mTimePicker;
    private Integer[] mCheckBoxIds = {
            R.id.cb_sunday, R.id.cb_monday, R.id.cb_tuesday, R.id.cb_wednesday,
            R.id.cb_thursday, R.id.cb_friday, R.id.cb_saturday
    };

    // Intent Identifiers
    public static String TASK_ID = "task-id";

    // Intent Default Values
    public static int NO_TASK = -1;
    public static String EMPTY_TASK = "";

    /**
     * Instantiate and get all views for this activity. Sets onClickListeners based on whether this
     * activity is supposed to add or update a task
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_update_task);

        // Get ModelView
        mTaskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);

        // Get Views, Buttons, and Widgets
        mTitleView = findViewById(R.id.et_task_title);
        mDescriptionView = findViewById(R.id.et_task_description);
        Button saveButton = findViewById(R.id.btn_add_task);
        Button deleteButton = findViewById(R.id.btn_delete_task);

        // Get notification times
        mTimePicker = findViewById(R.id.time_picker);
        mWeekCheckBoxes = new CheckBox[DateTime.DAYS_IN_WEEK];
        for(int i = 0; i < DateTime.DAYS_IN_WEEK; i++) {
            mWeekCheckBoxes[i] = findViewById(mCheckBoxIds[i]);
        }

        final int taskId = getIntent().getIntExtra(TASK_ID, NO_TASK);

        if(taskId == NO_TASK) {
            deleteButton.setVisibility(View.GONE);
            saveButton.setText(getString(R.string.btn_add));
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mTaskViewModel.insert(getTaskFromViews());
                    onBackPressed();
                }
            });
        } else {
            // Populate the fields with existing data and set button to update instead of save
            populateViews(taskId);
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Task task = getTaskFromViews();
                    task.setId(taskId);
                    mTaskViewModel.update(task);
                    onBackPressed();
                }
            });
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Task task = mTaskViewModel.getTask(taskId);
                    NotificationUtils.removeNotifier(getBaseContext(), task);
                    mTaskViewModel.delete(mTaskViewModel.getTask(taskId));
                    onBackPressed();
                }
            });
        }
     }

    /**
     * @return Task which all its values derived from the activity_add_or_update_task.xml layout
     */
     private Task getTaskFromViews(){
         Task task = new Task();

         String title = mTitleView.getText().toString();
         String description = mDescriptionView.getText().toString();

         DateTime dateTime = new DateTime(mTimePicker.getHour(), mTimePicker.getMinute());
         dateTime.setWeekDays(getCheckedDays());

         task.setTitle(title);
         task.setDescription(description);
         task.setDateTime(dateTime);

         return task;
     }

    /**
     * @param id Takes an ID of a task and populate all views for this activity using its data
     */
     private void populateViews(int id){
         Task currentTask = mTaskViewModel.getTask(id);
         if(currentTask != null) {
             mTitleView.setText(currentTask.getTitle());
             mDescriptionView.setText(currentTask.getDescription());
             mTimePicker.setHour(currentTask.getDateTime().getHour());
             mTimePicker.setMinute(currentTask.getDateTime().getMinute());
             setCheckedDays(currentTask.getDateTime().getWeekPreferences());
         }
     }

    /**
     * @return List of booleans based off what is checked on the activity
     */
     private ArrayList<Boolean> getCheckedDays(){
         ArrayList<Boolean> repeatedDays = new ArrayList<>(0);
         for(int i = 0; i < DateTime.DAYS_IN_WEEK; i++){
             repeatedDays.add(mWeekCheckBoxes[i].isChecked());
         }
         return repeatedDays;
     }

    /**
     * Sets the checkboxes based on an existing task's data
     */
     private void setCheckedDays(List<Boolean> repeatedDays){
         for(int i = 0; i < DateTime.DAYS_IN_WEEK; i++){
             mWeekCheckBoxes[i].setChecked(repeatedDays.get(i));
         }
     }
}
