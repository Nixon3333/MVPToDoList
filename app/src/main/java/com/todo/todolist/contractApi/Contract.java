package com.todo.todolist.contractApi;

import android.content.Context;

import com.todo.todolist.model.Task;

import java.util.List;

public interface Contract {

    interface View {
        default void showTasks(List<Task> list) {
        }
    }

    interface Presenter {
        void getTasks();

        void saveTask(Task task);

        void editTask(Task task, int position);

        void deleteTask(int position);

        String[] getEditTask(int position);
    }

    interface Model {

        List<Task> loadTasks(Context context);

        void save(Task task, Context context);

        void edit(Task task, Context context, int position);

        void delete(Context context, int position);

        String[] getEdit(Context context, int position);
    }
}
