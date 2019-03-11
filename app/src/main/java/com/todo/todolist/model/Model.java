package com.todo.todolist.model;

import android.content.Context;

import com.todo.todolist.contractApi.Contract;
import com.todo.todolist.model.db.DBHelper;

import java.util.List;

public class Model implements Contract.Model {

    private DBHelper dbHelper;

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
    public void edit(Task task, Context context, int position) {
        dbHelper = new DBHelper(context);
        dbHelper.editTask(task, position);
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

    /*@Override
    public List<String> getDates(Context context) {
        dbHelper = new DBHelper(context);
        return dbHelper.getTaskDates();
    }*/

    @Override
    public void switchDone(Context context, int position) {
        dbHelper = new DBHelper(context);
        dbHelper.switchDone(position);
    }
}
