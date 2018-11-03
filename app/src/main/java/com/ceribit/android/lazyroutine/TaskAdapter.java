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

import java.util.List;

/**
 * This class helps facilitate information for the List of tasks stored in the database and
 * the WeatherUtil information set by the user
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final LayoutInflater mInflater;
    private List<Task> mTasks;

    TaskAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    /**
     * Creates standard view holder using the task_list_item.xml layout
     */
    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.task_list_item, parent, false);
        return new TaskViewHolder(itemView);
    }



    /**
     * Assigns data from the task List {@link List<Task>} for a given position
     */
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder viewHolder, int position) {
        if(position == 0) {
            viewHolder.mTaskTitleView.setText("Weather");
            viewHolder.mTaskDescriptionView.setText("");
            viewHolder.itemView.setOnClickListener(null);
        } else {
            if (mTasks != null) {
                final Task current = mTasks.get(position-1);
                viewHolder.mTaskTitleView.setText(current.getTitle());
                viewHolder.mTaskDescriptionView.setText(current.getDescription());
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext() , AddOrUpdateTask.class);
                        intent.putExtra(AddOrUpdateTask.TASK_ID, current.getId());
                        intent.putExtra(AddOrUpdateTask.TASK_TITLE, current.getTitle());
                        intent.putExtra(AddOrUpdateTask.TASK_DESCRIPTION, current.getDescription());
                        view.getContext().startActivity(intent);
                    }
                });
            } else {
                viewHolder.mTaskTitleView.setText("No title found.");
            }
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
    class TaskViewHolder extends RecyclerView.ViewHolder{
        private final TextView mTaskTitleView;
        private final TextView mTaskDescriptionView;

        private TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            mTaskTitleView = itemView.findViewById(R.id.task_item_title);
            mTaskDescriptionView = itemView.findViewById(R.id.task_item_description);
        }
    }
}
