package com.todo.todolist.presenter;

import android.content.Context;

import com.todo.todolist.contractApi.Contract;
import com.todo.todolist.model.Model;
import com.todo.todolist.model.Task;

import java.util.List;

public class Presenter implements Contract.Presenter {


    private Contract.View view;
    private Contract.Model model;
    private Context context;


    public Presenter(Contract.View view, Context context) {
        this.view = view;
        this.context = context;
        this.model = new Model();
    }

    @Override
    public void getTasks() {
        view.showTasks(model.loadTasks(context));
    }

    @Override
    public void saveTask(Task task) {
        model.save(task, context);
    }

    @Override
    public void editTask(Task task, int position, List<Task> list) {
        model.edit(task, context, position, list);
    }

    @Override
    public void deleteTask(int position, List<Task> list) {
        model.delete(context, position, list);
    }

    @Override
    public String[] getEditTask(int position, List<Task> list) {
        return model.getEdit(context, position, list);
    }

    @Override
    public void switchDone(int position, List<Task> list) {
        model.switchDone(context, position, list);
    }

    @Override
    public void switchSelectItem(int position, List<Task> list) {
        model.switchSelect(context, position, list);
    }
}
