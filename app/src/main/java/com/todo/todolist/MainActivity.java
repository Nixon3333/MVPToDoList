package com.todo.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.todo.todolist.Interface.Contract;

import java.util.List;


public class MainActivity extends AppCompatActivity implements Contract.View {

    Contract.Presenter presenter;
    RecyclerView recyclerView;
    Toolbar toolbar;
    TaskAdapter taskAdapter;


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
        intent.putExtra("requestCode", 1);
        startActivityForResult(intent, 1);
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
                intent.putExtra("requestCode", 2);
                intent.putExtra("adapterPosition", item.getOrder());
                intent.putExtra("title", task[0]);
                intent.putExtra("task", task[1]);
                startActivityForResult(intent, 2);

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
