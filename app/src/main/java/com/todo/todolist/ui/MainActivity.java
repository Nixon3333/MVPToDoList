package com.todo.todolist.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.todo.todolist.contractApi.ContractSettings;
import com.todo.todolist.presenter.PresenterSettings;
import com.todo.todolist.utils.BroadcastManager;
import com.todo.todolist.R;
import com.todo.todolist.adapter.TaskAdapter;
import com.todo.todolist.contractApi.ContractMain;
import com.todo.todolist.model.pojo.Task;
import com.todo.todolist.presenter.PresenterMain;
import com.todo.todolist.constants.CONST;
import com.todo.todolist.utils.DeleteTaskDialogFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements ContractMain.View, ContractSettings.View, DeleteTaskDialogFragment.MyDialogListener, NavigationView.OnNavigationItemSelectedListener {

    private ContractMain.Presenter presenterMain;
    private ContractSettings.Presenter presenterSettings;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private TaskAdapter taskAdapter;
    private TextView tvItemCount;
    private TextView tvToolbarInfo;
    private LinearLayout layoutSelectMode;
    private boolean doubleBackToExitPressedOnce = false;
    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setAlarm();

        initUI();

        presenterMain = new PresenterMain(this, this);
        presenterSettings = new PresenterSettings(this, this);

        if (presenterSettings.getIsFirstSetting()) {
            presenterMain.saveTask(new Task("Need to buy", "Milk", 1, "16.3.2019", 0, 0, "General"));
            presenterMain.saveTask(new Task("Watch film", "Avengers", 2, "18.3.2019", 0, 1, "Personal"));

            Snackbar.make(recyclerView, "Use long tap at task to multiple choice", 10000).show();
        }

        presenterMain.getTasks();


    }

    //Start alarm after 12 hours after open app and repeat everyday
    //Need to change trigger time
    private void setAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, BroadcastManager.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 5000 /*AlarmManager.INTERVAL_HALF_DAY*/, AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    private void initUI() {
        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white);

        tvToolbarInfo = findViewById(R.id.tvToolbarInfo);

        layoutSelectMode = findViewById(R.id.layoutSelectedMode);
    }

    @Override
    public void showTasks(List<Task> list) {
        taskAdapter = new TaskAdapter();
        taskAdapter.setTaskList(list);

        recyclerView.setAdapter(taskAdapter);

        //Init Notes count
        tvItemCount = findViewById(R.id.tvItemCount);
        tvItemCount.setText(String.format("%s %s %s %s", getString(R.string.notes_count), String.valueOf(getItemCount(taskAdapter.getCurrentList()))/*String.valueOf(getItemCount(list)*/,
                getString(R.string.notes_done_count), String.valueOf(getDoneItemCount(list))));

        tvToolbarInfo.setText(presenterMain.getCurrentDate());
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
            int doRemind = 0;
            if (bundle.getBoolean("remind"))
                doRemind = 1;
            Task task = new Task(bundle.getString("title"),
                    bundle.getString("task"),
                    bundle.getInt("priority", 2),
                    bundle.getString("date"),
                    0,
                    doRemind,
                    bundle.getString("group"));
            String position = bundle.getString("position");
            presenterMain.editTask(task, Integer.parseInt(position), taskAdapter.getCurrentList());
        }
        presenterMain.getTasks();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem mSearch = menu.findItem(R.id.action_search);

        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint(getString(R.string.searchview_hint));

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
                //Done
                presenterMain.switchDone(item.getOrder(), taskAdapter.getCurrentList());
                taskAdapter.switchTaskDone(item.getOrder());
                //presenterMain.getTasks();
                break;
            case 2:
                //Edit
                Log.d("Menu", "editTask");

                doOnMenuEditClick(item);

                Log.d("Menu", String.valueOf(item.getOrder()));
                break;
                //Delete
            case 3:
                Log.d("Menu", "ic_delete_white");
                doOnMenuDeleteClick(item);
                break;
            case 4:
                //Select
                presenterMain.switchSelectItem(item.getOrder(), taskAdapter.getCurrentList());
                taskAdapter.switchSelectMode();

                if (TaskAdapter.selectMode)
                    layoutSelectMode.setVisibility(View.VISIBLE);

                if (taskAdapter.getCountOfSelectedItems(taskAdapter.getCurrentList()) == 0)
                    layoutSelectMode.setVisibility(View.GONE);


                taskAdapter.notifyDataSetChanged();

                break;
        }
        return super.onContextItemSelected(item);
    }

    private void doOnMenuDeleteClick(MenuItem item) {
        DeleteTaskDialogFragment dialogFragment = new DeleteTaskDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", item.getOrder());
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getSupportFragmentManager(), "singleDelete");
    }

    private void doOnMenuEditClick(MenuItem item) {
        String[] task = presenterMain.getEditTask(item.getOrder(), taskAdapter.getCurrentList());
        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra("requestCode", CONST.EDIT_REQUEST_CODE);
        intent.putExtra("adapterPosition", item.getOrder());
        intent.putExtra("title", task[0]);
        intent.putExtra("task", task[1]);
        intent.putExtra("date", task[2]);
        intent.putExtra("priority", task[3]);
        intent.putExtra("group", task[4]);
        startActivityForResult(intent, CONST.EDIT_REQUEST_CODE);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialogFragment, int position) {

        if (dialogFragment.getTag().equals("singleDelete")) {
            presenterMain.deleteTask(position, taskAdapter.getCurrentList());
            presenterMain.getTasks();
        }

        if (dialogFragment.getTag().equals("multipleDelete")) {
            List<Integer> IDs = new ArrayList<>();
            IDs.clear();
            IDs.addAll(taskAdapter.getSelectedItemsID(taskAdapter.getCurrentList()));
            for (int i = 0; i < IDs.size(); i++) {
                presenterMain.deleteTask(IDs.get(i), taskAdapter.getCurrentList());
            }
            presenterMain.getTasks();
            layoutSelectMode.setVisibility(View.GONE);
        }
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
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, getResources().getString(R.string.back_pressed_text), Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        }
    }

    public void onMultipleActionClick(View view) {
        List<Integer> IDs = new ArrayList<>();
        switch (view.getId()) {
            case R.id.ibDelete:

                DeleteTaskDialogFragment dialogFragment = new DeleteTaskDialogFragment();
                dialogFragment.show(getSupportFragmentManager(), "multipleDelete");

                break;

            case R.id.ibShare:
                IDs.clear();
                StringBuilder sharedText = new StringBuilder();
                IDs.addAll(taskAdapter.getSelectedItemsID(taskAdapter.getCurrentList()));
                for (int i = 0; i < IDs.size(); i++) {

                    sharedText.append(getString(R.string.sharing_text_title)).append(taskAdapter.getCurrentList().get(i).getTitle()).append("\n");
                    sharedText.append(getString(R.string.sharing_text_task)).append(taskAdapter.getCurrentList().get(i).getTask());
                    if (!(i + 1 == IDs.size()))
                        sharedText.append("\n");
                }
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, sharedText.toString());
                sendIntent.setType("text/plain");
                taskAdapter.unselectedAll();
                layoutSelectMode.setVisibility(View.GONE);
                taskAdapter.notifyDataSetChanged();
                startActivity(Intent.createChooser(sendIntent, getResources().getString(R.string.sharing_chooser_title)));
                break;

            case R.id.ibCancel:
                taskAdapter.unselectedAll();
                layoutSelectMode.setVisibility(View.GONE);
                taskAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getTitle().toString().equals("Settings")) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else {
            drawerLayout.closeDrawer(Gravity.START);
            taskAdapter.setGroupFilter(menuItem.getTitle().toString());

            //Init Notes count
            tvItemCount.setText(String.format("%s %s %s %s", getString(R.string.notes_count), String.valueOf(getItemCount(taskAdapter.getCurrentList()))/*String.valueOf(getItemCount(list)*/,
                    getString(R.string.notes_done_count), String.valueOf(getDoneItemCount(taskAdapter.getCurrentList()))));
        }
        drawerLayout.closeDrawer(Gravity.START);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (presenterSettings.getDoneTasksSettings()) {
            taskAdapter.showAllTasks();
        } else {
            taskAdapter.dontShowDoneTasks();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenterSettings.saveIsFirstSettings();
    }
}
