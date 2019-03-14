package com.todo.todolist.contractApi;

import android.content.Context;

public interface ContractSettings {

    interface View {

    }

    interface Presenter {
        boolean getSettings();
        void saveSettings(boolean showDoneTasks);
    }

    interface Model {
        boolean loadSettings(Context context);
        void saveSettings(Context context, boolean showDoneTasks);
    }
}
