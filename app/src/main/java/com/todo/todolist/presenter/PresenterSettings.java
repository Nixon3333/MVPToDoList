package com.todo.todolist.presenter;

import android.content.Context;

import com.todo.todolist.contractApi.ContractSettings;
import com.todo.todolist.model.ModelSettings;

public class PresenterSettings implements ContractSettings.Presenter {

    private ContractSettings.View view;
    private ContractSettings.Model model;
    private Context context;


    public PresenterSettings(ContractSettings.View view, Context context) {
        this.view = view;
        this.context = context;
        this.model = new ModelSettings();
    }

    @Override
    public boolean getDoneTasksSettings() {
        return model.loadDoneTasksSettings(context);
    }

    @Override
    public boolean getIsFirstSetting() {
        return model.getIsFirstSetting(context);
    }

    @Override
    public void saveDoneTasksSettings(boolean showDoneTasks) {
        model.saveDoneTasksSettings(context, showDoneTasks);
    }

    @Override
    public void saveIsFirstSettings() {
        model.saveIsFirstSettings(context);
    }


}
