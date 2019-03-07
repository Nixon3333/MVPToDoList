package com.todo.todolist.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.todo.todolist.BroadcastManager;
import com.todo.todolist.R;
import com.todo.todolist.adapter.TaskAdapter;
import com.todo.todolist.contractApi.Contract;
import com.todo.todolist.model.Task;
import com.todo.todolist.presenter.Presenter;

import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity implements Contract.View {

    private Contract.Presenter presenter;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private TaskAdapter taskAdapter;
    private int ADD_REQUEST_CODE = 1;
    private int EDIT_REQUEST_CODE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setAlarm();

        initUI();

        presenter = new Presenter(this, this);
        presenter.getTasks();

    }

    private void setAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, BroadcastManager.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis(), pendingIntent);
    }

    private void initUI() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void showTasks(List<Task> list) {
        taskAdapter = new TaskAdapter();
        taskAdapter.setTaskList(list);
        recyclerView.setAdapter(taskAdapter);
    }


    public void onFabClick(View view) {
        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra("requestCode", ADD_REQUEST_CODE);
        startActivityForResult(intent, ADD_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            presenter.getTasks();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem mSearch = menu.findItem(R.id.action_search);

        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search");

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("Search", newText);
                taskAdapter.filter(newText);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                Log.d("Menu", "edit");
                String[] task = presenter.getEditTask(item.getOrder());
                Intent intent = new Intent(this, TaskActivity.class);
                intent.putExtra("requestCode", EDIT_REQUEST_CODE);
                intent.putExtra("adapterPosition", item.getOrder());
                intent.putExtra("title", task[0]);
                intent.putExtra("task", task[1]);
                intent.putExtra("date", task[2]);
                startActivityForResult(intent, EDIT_REQUEST_CODE);

                Log.d("Menu", String.valueOf(item.getOrder()));
                break;
            case 2:
                Log.d("Menu", "delete");
                presenter.deleteTask(item.getOrder());
                presenter.getTasks();
                break;
        }
        return super.onContextItemSelected(item);
    }
}
