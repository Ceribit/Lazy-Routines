package com.ceribit.android.lazyroutine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ceribit.android.lazyroutine.database.tasks.DateTime;
import com.ceribit.android.lazyroutine.database.tasks.TaskViewModel;
import com.ceribit.android.lazyroutine.database.weather.WeatherPreferences;
import com.ceribit.android.lazyroutine.database.weather.WeatherUtils;
import com.ceribit.android.lazyroutine.notifications.NotificationUtils;

public class WeatherActivity extends AppCompatActivity {

    // ModelView
    private TaskViewModel mTaskViewModel;

    // Set of Views to be used
    private TextView mCityViewLabel;
    private EditText mCityView;
    private EditText mDescriptionView;
    private CheckBox[] mWeekCheckBoxes;
    private TimePicker mTimePicker;
    private Button mSaveButton;
    private Button mDeleteButton;
    private Integer[] mCheckBoxIds = {
            R.id.cb_sunday, R.id.cb_monday, R.id.cb_tuesday, R.id.cb_wednesday,
            R.id.cb_thursday, R.id.cb_friday, R.id.cb_saturday
    };

    // Intent Identifiers
    public static String WEATHER_ID = "task-id";

    // Intent Defalt Values
    public static int NO_TASK = -1;
    public static String EMPTY_TASK = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_update_task);
        WeatherPreferences.init(this);

        // Get Views, Buttons, and Widgets
        mCityViewLabel = findViewById(R.id.tv_add_task);
        mCityView = findViewById(R.id.et_task_title);
        mDescriptionView = findViewById(R.id.et_task_description);
        mSaveButton = findViewById(R.id.btn_add_task);
        mDeleteButton = findViewById(R.id.btn_delete_task);

        // Get notification times
        mTimePicker = findViewById(R.id.time_picker);
        mWeekCheckBoxes = new CheckBox[DateTime.DAYS_IN_WEEK];
        for(int i = 0; i < DateTime.DAYS_IN_WEEK; i++) {
            mWeekCheckBoxes[i] = findViewById(mCheckBoxIds[i]);
        }

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePreferences();
                NotificationUtils.setWeatherAlarm(getApplicationContext());
                finish();
            }
        });
    }

    public void savePreferences(){
        String city = mCityView.getText().toString();
        WeatherPreferences.setCity(city);
    }

    public void init(){
        mDeleteButton.setVisibility(View.GONE);
        mCityViewLabel.setText(getString(R.string.weather_activity_city_label));
    }
}
