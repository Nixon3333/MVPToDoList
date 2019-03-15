package com.todo.todolist.model;

import android.content.Context;

import com.todo.todolist.contractApi.ContractMain;
import com.todo.todolist.model.db.DBHelper;
import com.todo.todolist.model.pojo.Task;

import java.util.List;

public class ModelMain implements ContractMain.Model {

    private DBHelper dbHelper;

    @Override
    public List<Task> loadTasks(Context context) {
        dbHelper = new DBHelper(context);
        return dbHelper.loadTask();
    }

    public void saveTask(Task task, Context context) {
        dbHelper = new DBHelper(context);
        dbHelper.saveTask(task);
    }

    @Override
    public void editTask(Task task, Context context, int position, List<Task> list) {
        dbHelper = new DBHelper(context);
        dbHelper.editTask(task, position, list);
    }

    @Override
    public void deleteTask(Context context, int position, List<Task> list) {
        dbHelper = new DBHelper(context);
        dbHelper.deleteTask(position, list);
    }

    @Override
    public String[] getEdit(Context context, int position, List<Task> list) {
        dbHelper = new DBHelper(context);
        return dbHelper.getEditTask(position, list);
    }

    @Override
    public void switchDone(Context context, int position, List<Task> list) {
        dbHelper = new DBHelper(context);
        dbHelper.switchDone(position, list);
    }

    @Override
    public void switchSelectItem(Context context, int position, List<Task> list) {
        dbHelper = new DBHelper(context);
        dbHelper.switchSelectTask(position, list);
    }
}
