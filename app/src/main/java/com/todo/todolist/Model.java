package com.todo.todolist;

import android.content.Context;

import com.todo.todolist.Interface.Contract;

import java.util.List;

public class Model implements Contract.Model {

    DBHelper dbHelper;

    @Override
    public List<Task> loadTasks(Context context) {
        dbHelper = new DBHelper(context);
        return dbHelper.loadTask();
    }

    public void save(String title, String task, int priority, Context context) {
        dbHelper = new DBHelper(context);
        dbHelper.saveTask(title, task, priority);
    }

    @Override
    public void edit(String title, String task, int priority, Context context, int position) {
        dbHelper = new DBHelper(context);
        dbHelper.editTask(title, task, priority, position);
    }
}
