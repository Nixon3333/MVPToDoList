package com.todo.todolist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.todo.todolist.Interface.Contract;

import java.util.List;

public class TaskActivity extends AppCompatActivity implements Contract.View {

    Contract.Presenter presenter;
    Button btAddTask, btEditTask;
    EditText etTitle, etTask;
    RadioGroup rgPriority;
    Bundle requestCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        presenter = new Presenter(this, this);

        initUI();
    }

    private void initUI() {
        btAddTask = findViewById(R.id.btAddTask);
        btEditTask = findViewById(R.id.btEditTask);
        etTask = findViewById(R.id.etTask);
        etTitle = findViewById(R.id.etTitle);
        rgPriority = findViewById(R.id.rgPriority);
        rgPriority.check(R.id.radio_medium);
        requestCode = getIntent().getExtras();
        if (requestCode.get("requestCode").equals(1))
            btAddTask.setVisibility(View.GONE);
        if (requestCode.get("requestCode").equals(2))
            btAddTask.setVisibility(View.GONE);
    }

    public void onAddTaskClick(View view) {
        int priority = 0;
        switch (rgPriority.getCheckedRadioButtonId()) {
            case R.id.radio_high:
                priority = 1;
                break;
            case R.id.radio_medium:
                priority = 2;
                break;
            case R.id.radio_low:
                priority = 3;
                break;
        }
        presenter.saveTask(etTitle.getText().toString(), etTask.getText().toString(), priority);
        setResult(RESULT_OK);
        finish();
    }

    public void onEditTaskClick(View view) {
        int priority = 0;
        switch (rgPriority.getCheckedRadioButtonId()) {
            case R.id.radio_high:
                priority = 1;
                break;
            case R.id.radio_medium:
                priority = 2;
                break;
            case R.id.radio_low:
                priority = 3;
                break;
        }
        requestCode = getIntent().getExtras();
        int position = (Integer) requestCode.get("adapterPosition");
        presenter.editTask(etTitle.getText().toString(), etTask.getText().toString(), priority, position);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void showTasks(List<Task> list) {

    }
}
