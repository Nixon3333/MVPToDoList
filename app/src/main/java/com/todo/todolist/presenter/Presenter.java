package com.todo.todolist.presenter;

import android.content.Context;

import com.todo.todolist.contractApi.Contract;
import com.todo.todolist.model.Model;
import com.todo.todolist.model.Task;

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
    public void editTask(Task task, int position) {
        model.edit(task, context, position);
    }

    @Override
    public void deleteTask(int position) {
        model.delete(context, position);
    }

    @Override
    public String[] getEditTask(int position) {
        return model.getEdit(context, position);
    }
}
