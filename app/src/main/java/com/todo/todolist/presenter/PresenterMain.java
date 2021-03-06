package com.todo.todolist.presenter;

import android.content.Context;

import com.todo.todolist.contractApi.ContractMain;
import com.todo.todolist.model.ModelMain;
import com.todo.todolist.model.pojo.Task;

import java.util.List;

public class PresenterMain implements ContractMain.Presenter {


    private ContractMain.View view;
    private ContractMain.Model model;
    private Context context;


    public PresenterMain(ContractMain.View view, Context context) {
        this.view = view;
        this.context = context;
        this.model = new ModelMain();
    }

    @Override
    public void getTasks() {
        view.showTasks(model.loadTasks(context));
    }

    @Override
    public void saveTask(Task task) {
        model.saveTask(task, context);
    }

    @Override
    public void editTask(Task task, int position, List<Task> list) {
        model.editTask(task, context, position, list);
    }

    @Override
    public void deleteTask(int position, List<Task> list) {
        model.deleteTask(context, position, list);
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
        model.switchSelectItem(context, position, list);
    }
}
