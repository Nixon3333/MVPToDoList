package com.todo.todolist.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.TextView;

import com.todo.todolist.R;
import com.todo.todolist.contractApi.ContractSettings;
import com.todo.todolist.presenter.PresenterSettings;

public class SettingsActivity extends AppCompatActivity implements ContractSettings.View{

    private ContractSettings.Presenter presenter;
    private CheckBox cbShowDoneTasks;
    private TextView tvToolbarInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        presenter = new PresenterSettings(this, this);

        initUI();

    }

    private void initUI() {
        tvToolbarInfo = findViewById(R.id.tvToolbarInfo);
        tvToolbarInfo.setText(R.string.toolbar_settings);

        cbShowDoneTasks = findViewById(R.id.cbShowDoneTasks);
        cbShowDoneTasks.setChecked(presenter.getSettings());
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.saveSettings(cbShowDoneTasks.isChecked());
    }
}
