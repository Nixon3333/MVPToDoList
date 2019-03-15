package com.todo.todolist.contractApi;

import android.content.Context;

public interface ContractSettings {

    interface View {

    }

    interface Presenter {
        boolean getDoneTasksSettings();
        boolean getIsFirstSetting();
        void saveDoneTasksSettings(boolean showDoneTasks);
        void saveIsFirstSettings();
    }

    interface Model {
        boolean loadDoneTasksSettings(Context context);
        boolean getIsFirstSetting(Context context);
        void saveDoneTasksSettings(Context context, boolean showDoneTasks);
        void saveIsFirstSettings(Context context);
    }
}
