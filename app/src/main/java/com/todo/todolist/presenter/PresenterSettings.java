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
    public boolean getSettings() {
        return model.loadSettings(context);
    }

    @Override
    public void saveSettings(boolean showDoneTasks) {
        model.saveSettings(context, showDoneTasks);
    }


}
