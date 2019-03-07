package com.todo.todolist.Interface;

import android.content.Context;

import com.todo.todolist.Task;

import java.util.List;

public interface Contract {

    interface View {
        default void showTasks(List<Task> list) {
        }
    }

    interface Presenter {
        void getTasks();

        void saveTask(Task task);

        void editTask(String title, String task, int priority, int position);

        void deleteTask(int position);

        String[] getEditTask(int position);
    }

    interface Model {

        List<Task> loadTasks(Context context);

        void save(Task task, Context context);

        void edit(String title, String task, int priority, Context context, int position);

        void delete(Context context, int position);

        String[] getEdit(Context context, int position);
    }
}
