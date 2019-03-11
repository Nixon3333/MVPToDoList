package com.todo.todolist.adapter;

import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.todo.todolist.R;
import com.todo.todolist.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList = new ArrayList<>();
    private List<Task> copyTaskList;

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_task, viewGroup, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder taskViewHolder, int i) {
        if (taskList.get(i).isDone() == 1) {
            taskViewHolder.tvTitle.setPaintFlags(taskViewHolder.tvTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            taskViewHolder.tvTask.setPaintFlags(taskViewHolder.tvTask.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            taskViewHolder.tvDate.setVisibility(View.GONE);
            taskViewHolder.imagePriority.setVisibility(View.GONE);
        }
        else {
            taskViewHolder.tvTitle.setPaintFlags(0);
            taskViewHolder.tvTask.setPaintFlags(0);
        }
        taskViewHolder.tvTitle.setText(taskList.get(i).getTitle());
        taskViewHolder.tvTask.setText(taskList.get(i).getTask());
        taskViewHolder.tvDate.setText(taskList.get(i).getDate());
        switch (taskList.get(i).getPriority()) {
            case 1:
                taskViewHolder.imagePriority.setImageResource(R.drawable.shape_circle_red);
                break;
            case 2:
                taskViewHolder.imagePriority.setImageResource(R.drawable.shape_circle_yellow);
                break;
            case 3:
                taskViewHolder.imagePriority.setImageResource(R.drawable.shape_circle_green);
                break;
        }
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
        if (string.isEmpty()) {
            taskList.addAll(copyTaskList);
        } else {
            string = string.toLowerCase();
            for (Task task : copyTaskList) {
                if (task.getTitle().toLowerCase().contains(string) || task.getTask().toLowerCase().contains(string)) {
                    taskList.add(task);
                }
            }
        }
        notifyDataSetChanged();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        TextView tvTitle;
        TextView tvTask;
        TextView tvDate;
        ImageView imagePriority;

        TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvTask = itemView.findViewById(R.id.tvTask);
            tvDate = itemView.findViewById(R.id.tvDate);
            imagePriority = itemView.findViewById(R.id.imagePriority);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            if (taskList.get(getAdapterPosition()).isDone() == 1)
                contextMenu.add(Menu.NONE, 1, getAdapterPosition(), "Undone");
            else
                contextMenu.add(Menu.NONE, 1, getAdapterPosition(), "Done");
            contextMenu.add(Menu.NONE, 2, getAdapterPosition(), "Edit");
            contextMenu.add(Menu.NONE, 3, getAdapterPosition(), "Delete");
        }

    }
}
