package com.todo.todolist.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.todo.todolist.constants.CONST;
import com.todo.todolist.contractApi.ContractSettings;

import java.util.Locale;

public class ModelSettings implements ContractSettings.Model {

    private SharedPreferences sharedPreferences;

    @Override
    public boolean loadSettings(Context context) {
        sharedPreferences = context.getSharedPreferences(CONST.APP_PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("showDoneTasks", true);
    }

    @Override
    public void saveSettings(Context context, boolean showDoneTasks) {
        sharedPreferences = context.getSharedPreferences(CONST.APP_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("showDoneTasks", showDoneTasks);
        editor.apply();
    }
}
