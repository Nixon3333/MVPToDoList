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
    public void saveTask(String title, String task, int priority) {
        model.save(title, task, priority, context);
    }
}
