package com.todo.todolist.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.todo.todolist.constants.CONST;
import com.todo.todolist.contractApi.ContractSettings;

import java.util.Locale;

public class ModelSettings implements ContractSettings.Model {

    private SharedPreferences sharedPreferences;
    private static boolean isFirstLaunch = true;

    @Override
    public boolean loadDoneTasksSettings(Context context) {
        sharedPreferences = context.getSharedPreferences(CONST.APP_PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("showDoneTasks", true);
    }

    @Override
    public boolean getIsFirstSetting(Context context) {
        sharedPreferences = context.getSharedPreferences(CONST.APP_PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isFirstLaunch", isFirstLaunch);
    }



    @Override
    public void saveDoneTasksSettings(Context context, boolean showDoneTasks) {
        sharedPreferences = context.getSharedPreferences(CONST.APP_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("showDoneTasks", showDoneTasks);
        editor.putBoolean("isFirstLaunch", false);
        editor.apply();
    }

    @Override
    public void saveIsFirstSettings(Context context) {
        sharedPreferences = context.getSharedPreferences(CONST.APP_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isFirstLaunch", false);
        editor.apply();
        ModelSettings.isFirstLaunch = false;
    }
}
