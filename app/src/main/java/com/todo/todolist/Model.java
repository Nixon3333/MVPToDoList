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

    public void save(Task task, Context context) {
        dbHelper = new DBHelper(context);
        dbHelper.saveTask(task);
    }

    @Override
    public void edit(String title, String task, int priority, Context context, int position) {
        dbHelper = new DBHelper(context);
        dbHelper.editTask(title, task, priority, position);
    }

    @Override
    public void delete(Context context, int position) {
        dbHelper = new DBHelper(context);
        dbHelper.deleteTask(position);
    }

    @Override
    public String[] getEdit(Context context, int position) {
        dbHelper = new DBHelper(context);
        return dbHelper.getEditTask(position);
    }
}
