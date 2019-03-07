package com.todo.todolist;

import android.content.Context;

import com.todo.todolist.Interface.Contract;

public class Presenter implements Contract.Presenter {


    Contract.View view;
    Contract.Model model;
    Context context;


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
    public void editTask(String title, String task, int priority, int position) {
        model.edit(title, task, priority, context, position);
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
