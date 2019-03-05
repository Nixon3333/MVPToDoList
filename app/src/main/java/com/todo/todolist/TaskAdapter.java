package com.todo.todolist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    List<Task> taskList = new ArrayList<>();
    List<Task> copyTaskList;

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_task, viewGroup, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder taskViewHolder, int i) {
        taskViewHolder.tvTitle.setText(taskList.get(i).getTitle());
        taskViewHolder.tvTask.setText(taskList.get(i).getTask());
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void setTaskList(List<Task> list) {
        taskList.addAll(list);
        copyTaskList = new ArrayList<>();
        copyTaskList.addAll(taskList);
        notifyDataSetChanged();
    }

    public void filter(String string) {
        taskList.clear();
        if(string.isEmpty()){
            taskList.addAll(copyTaskList);
        } else{
            string = string.toLowerCase();
            for(Task task: copyTaskList){
                if(task.getTitle().toLowerCase().contains(string) || task.getTask().toLowerCase().contains(string)){
                    taskList.add(task);
                }
            }
        }
        notifyDataSetChanged();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvTask;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvTask = itemView.findViewById(R.id.tvTask);
        }
    }
}
