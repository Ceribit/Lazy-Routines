package com.ceribit.android.lazyroutine;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ceribit.android.lazyroutine.database.tasks.Task;
import com.ceribit.android.lazyroutine.database.weather.UpdateTemperatureAsyncTask;
import com.ceribit.android.lazyroutine.database.weather.WeatherPreferences;
import com.ceribit.android.lazyroutine.notifications.NotificationUtils;

import java.util.List;

/**
 * This class helps facilitate information for the List of tasks stored in the database and
 * the WeatherUtil information set by the user
 */
public class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final LayoutInflater mInflater;
    private List<Task> mTasks;

    TaskAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    /**
     * Creates standard view holder using the task_list_item.xml layout
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch(viewType){
            case 0:
                return new WeatherViewHolder(
                        mInflater.inflate(R.layout.weather_list_item, parent, false)
                );

            default:
                return new TaskViewHolder(
                        mInflater.inflate(R.layout.task_list_item, parent, false));
        }
    }



    /**
     * Assigns data from the task List {@link List<Task>} for a given position
     * TODO: Create WeatherPreference function that returns the temperature with the correct metric (CASE 0)
     */
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()){
            case 0:
                new UpdateTemperatureAsyncTask(viewHolder.itemView.getContext()).execute();
                WeatherViewHolder weatherViewHolder = (WeatherViewHolder) viewHolder;
                weatherViewHolder.mCityTitleView.setText(WeatherPreferences.getCity());
                weatherViewHolder.mMinTemperatureView.setText(
                        WeatherPreferences.getFormattedTemperature(
                                WeatherPreferences.getLowTemperature(),"Low"));
                weatherViewHolder.mMaxTemperatureView.setText(
                        WeatherPreferences.getFormattedTemperature(
                                WeatherPreferences.getHighTemperature(), "High"));
                weatherViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), WeatherActivity.class);
                        view.getContext().startActivity(intent);
                    }
                });
                break;
            default:
                final TaskViewHolder taskViewHolder = (TaskViewHolder) viewHolder;
                if(mTasks != null) {
                    final Task currentTask = mTasks.get(position - 1);
                    taskViewHolder.mTaskTitleView.setText(currentTask.getTitle());
                    taskViewHolder.mTaskDescriptionView.setText(currentTask.getDescription());
                    taskViewHolder.mTaskTimeView.setText(currentTask.getFormattedTime());
                    taskViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(view.getContext(), AddOrUpdateTask.class);
                            intent.putExtra(AddOrUpdateTask.TASK_ID, currentTask.getId());
                            view.getContext().startActivity(intent);
                            NotificationUtils.scheduleRepeatedTask(taskViewHolder.mTaskTimeView.getContext(), currentTask);
                        }
                    });
                } else{
                    taskViewHolder.mTaskTitleView.setText("No title found.");
                }
                break;
        }
    }

    /**
     * Gets the size of the array plus 1 for the weather
     * @return mTasks array size
     */
    @Override
    public int getItemCount() {
        if(mTasks != null) {
            return mTasks.size()+1;
        }
        return 0;
    }

    /**
     * Reassigns the task list for the adapter
     * @param tasks The list of tasks to be displayed by the adapter
     */
    void setTasks(List<Task> tasks){
        mTasks = tasks;
        notifyDataSetChanged();
    }

    /**
     * Creates ViewHolder for the recycler_item
     */
    private class TaskViewHolder extends RecyclerView.ViewHolder{
        private final TextView mTaskTitleView;
        private final TextView mTaskDescriptionView;
        private final TextView mTaskTimeView;

        private TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            mTaskTitleView = itemView.findViewById(R.id.task_item_title);
            mTaskDescriptionView = itemView.findViewById(R.id.task_item_description);
            mTaskTimeView = itemView.findViewById(R.id.task_item_time);
        }
    }

    private class WeatherViewHolder extends RecyclerView.ViewHolder{
        private final TextView mCityTitleView;
        private final TextView mMinTemperatureView;
        private final TextView mMaxTemperatureView;

        private WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            mCityTitleView = itemView.findViewById(R.id.task_item_city);
            mMinTemperatureView = itemView.findViewById(R.id.task_item_min_temperature);
            mMaxTemperatureView = itemView.findViewById(R.id.weather_item_max_temperature);
        }
    }
}
