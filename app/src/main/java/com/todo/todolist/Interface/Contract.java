package com.todo.todolist.Interface;

import android.content.Context;

import com.todo.todolist.Task;

import java.util.List;

public interface Contract {

    interface View {
        void showTasks(List<Task> list);

    }

    interface Presenter {
        void getTasks();

        void saveTask(String title, String task, int priority);

        void editTask(String title, String task, int priority, int position);
    }

    interface Model {

        List<Task> loadTasks(Context context);

        void save(String title, String task, int priority, Context context);

        void edit(String title, String task, int priority, Context context, int position);
    }
}
