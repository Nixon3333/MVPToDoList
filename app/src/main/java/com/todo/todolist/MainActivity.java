package com.todo.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.todo.todolist.Interface.Contract;

import java.util.List;

public class MainActivity extends AppCompatActivity implements Contract.View {

    Contract.Presenter presenter;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initUI();

        presenter = new Presenter(this, this);
        presenter.getTasks();

    }

    private void initUI() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void showTasks(List<Task> list) {
        TaskAdapter taskAdapter = new TaskAdapter();
        taskAdapter.setTaskList(list);
        recyclerView.setAdapter(taskAdapter);
    }



    public void onFabClick(View view) {
        Intent intent = new Intent(this, TaskActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.getTasks();
    }
}
