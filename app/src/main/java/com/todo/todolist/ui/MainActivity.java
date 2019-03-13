package com.todo.todolist.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.todo.todolist.BroadcastManager;
import com.todo.todolist.R;
import com.todo.todolist.adapter.TaskAdapter;
import com.todo.todolist.contractApi.Contract;
import com.todo.todolist.model.Task;
import com.todo.todolist.presenter.Presenter;
import com.todo.todolist.utils.CONST;
import com.todo.todolist.utils.DeleteTaskDialogFragment;

import java.util.List;


public class MainActivity extends AppCompatActivity implements Contract.View, DeleteTaskDialogFragment.MyDialogListener {

    private Contract.Presenter presenter;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private TaskAdapter taskAdapter;
    private TextView tvItemCount;
    private TextView tvToolbarDate;
    private LinearLayout layoutSelectMode;
    private boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setAlarm();

        initUI();

        presenter = new Presenter(this, this);
        presenter.getTasks();

    }

    //Start alarm after 6 hours after open app and repeat everyday
    private void setAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, BroadcastManager.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC, 21600000, 86400000, pendingIntent);
        }
    }

    private void initUI() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvToolbarDate = findViewById(R.id.tvToolbarDate);

        layoutSelectMode = findViewById(R.id.layoutSelectedMode);
    }

    @Override
    public void showTasks(List<Task> list) {
        taskAdapter = new TaskAdapter();
        taskAdapter.setTaskList(list);

        recyclerView.setAdapter(taskAdapter);

        tvItemCount = findViewById(R.id.tvItemCount);
        tvItemCount.setText(String.format("%s %s %s %s", getString(R.string.notes_count), String.valueOf(getItemCount(list)),
                getString(R.string.notes_done_count), String.valueOf(getDoneItemCount(list))));

        tvToolbarDate.setText(presenter.getCurrentDate());
    }


    public void onFabClick(View view) {
        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra("requestCode", CONST.ADD_REQUEST_CODE);
        startActivityForResult(intent, CONST.ADD_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CONST.EDIT_REQUEST_CODE & resultCode == RESULT_OK) {

            Bundle bundle = data.getBundleExtra("taskBundle");
            Task task = new Task(bundle.getString("title"),
                    bundle.getString("task"),
                    bundle.getInt("priority", 2),
                    bundle.getString("date"),
                    0);
            String position = bundle.getString("position");
            presenter.editTask(task, Integer.parseInt(position), taskAdapter.getCurrentList());
        }
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
                presenter.switchDone(item.getOrder(), taskAdapter.getCurrentList());
                presenter.getTasks();
                break;
            case 2:
                Log.d("Menu", "edit");

                doOnMenuEditClick(item);

                Log.d("Menu", String.valueOf(item.getOrder()));
                break;
            case 3:
                Log.d("Menu", "ic_delete_white");
                doOnMenuDeleteClick(item);
                break;
            case 4:
                presenter.switchSelectItem(item.getOrder(), taskAdapter.getCurrentList());
                taskAdapter.switchSelectMode();

                if (TaskAdapter.selectMode)
                    layoutSelectMode.setVisibility(View.VISIBLE);

                taskAdapter.notifyDataSetChanged();

                if (taskAdapter.getCountOfSelectedItems(taskAdapter.getCurrentList()) == 0)
                    layoutSelectMode.setVisibility(View.GONE);

                break;
        }
        return super.onContextItemSelected(item);
    }

    private void doOnMenuDeleteClick(MenuItem item) {
        DeleteTaskDialogFragment dialogFragment = new DeleteTaskDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", item.getOrder());
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getSupportFragmentManager(), "tag");
    }

    private void doOnMenuEditClick(MenuItem item) {
        String[] task = presenter.getEditTask(item.getOrder(), taskAdapter.getCurrentList());
        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra("requestCode", CONST.EDIT_REQUEST_CODE);
        intent.putExtra("adapterPosition", item.getOrder());
        intent.putExtra("title", task[0]);
        intent.putExtra("task", task[1]);
        intent.putExtra("date", task[2]);
        intent.putExtra("priority", task[3]);
        startActivityForResult(intent, CONST.EDIT_REQUEST_CODE);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialogFragment, int position) {
        presenter.deleteTask(position, taskAdapter.getCurrentList());
        presenter.getTasks();
    }

    private int getDoneItemCount(List<Task> list) {
        int count = 0;
        for (Task task : list) {
            if (task.isDone() == 1)
                count++;
        }
        return count;
    }

    private int getItemCount(List<Task> list) {
        int count = 0;
        for (Task task : list) {
            if (task.isDone() == 0)
                count++;
        }
        return count;
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }
}
