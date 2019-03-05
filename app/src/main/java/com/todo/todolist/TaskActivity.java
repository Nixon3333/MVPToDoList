package com.todo.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.todo.todolist.Interface.Contract;

import java.util.List;

public class TaskActivity extends AppCompatActivity implements Contract.View {

    Contract.Presenter presenter;
    Button btAddTask;
    EditText etTitle, etTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        presenter = new Presenter(this, this);

        initUI();
    }

    private void initUI() {
        btAddTask = findViewById(R.id.btAddTask);
        etTask = findViewById(R.id.etTask);
        etTitle = findViewById(R.id.etTitle);
    }

    public void onAddTaskClick(View view) {
        presenter.saveTask(etTitle.getText().toString(), etTask.getText().toString());
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void showTasks(List<Task> list) {

    }
}
