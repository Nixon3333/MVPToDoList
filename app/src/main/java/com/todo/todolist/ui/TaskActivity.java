package com.todo.todolist.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.todo.todolist.R;
import com.todo.todolist.contractApi.Contract;
import com.todo.todolist.model.Task;
import com.todo.todolist.presenter.Presenter;
import com.todo.todolist.utils.CONST;

import java.util.Calendar;
import java.util.Date;

public class TaskActivity extends AppCompatActivity implements Contract.View {

    private Contract.Presenter presenter;
    private Button btAddTask, btApply;
    private EditText etTitle, etTask, etDate;
    private RadioGroup rgPriority;
    private Bundle requestCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        presenter = new Presenter(this, this);

        initUI();
    }

    private void initUI() {
        btAddTask = findViewById(R.id.btAddTask);
        btApply = findViewById(R.id.btApply);
        etTask = findViewById(R.id.etTask);
        etTitle = findViewById(R.id.etTitle);
        showKeyboard();
        etTitle.requestFocus();
        etDate = findViewById(R.id.etDate);

        rgPriority = findViewById(R.id.rgPriority);

        requestCode = getIntent().getExtras();
        if (requestCode.get("requestCode").equals(CONST.ADD_REQUEST_CODE)) {
            rgPriority.check(R.id.radio_medium);
            btApply.setVisibility(View.GONE);
            etDate.setText(getTodayDate());
        }
        if (requestCode.get("requestCode").equals(CONST.EDIT_REQUEST_CODE)) {
            btAddTask.setVisibility(View.GONE);
            etTitle.setText(getIntent().getStringExtra("title"));
            etTask.setText(getIntent().getStringExtra("task"));
            etDate.setText(getIntent().getStringExtra("date"));
            Log.d("Priority", getIntent().getStringExtra("priority"));
            switch (Integer.valueOf(getIntent().getStringExtra("priority"))) {
                case 1:
                    rgPriority.check(R.id.radio_high);
                    break;
                case 2:
                    rgPriority.check(R.id.radio_medium);
                    break;
                case 3:
                    rgPriority.check(R.id.radio_low);
                    break;
            }
        }
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public void onAddTaskClick(View view) {
        hideKeyboard();
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
        if (etTitle.getText().toString().equals("") & etTask.getText().toString().equals(""))
            Toast.makeText(this, "Task is empty", Toast.LENGTH_LONG).show();
        else {
            Task task = new Task(etTitle.getText().toString(), etTask.getText().toString(), priority, etDate.getText().toString(), 0);
            presenter.saveTask(task);
            setResult(RESULT_OK);
            finish();
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etTitle.getWindowToken(), 0);
    }

    public void onEditTaskClick(View view) {
        hideKeyboard();
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
        Log.d("PositionTask", String.valueOf(position));
        Task task = new Task(etTitle.getText().toString(), etTask.getText().toString(), priority, etDate.getText().toString(), 0);
        Intent data = new Intent();
        data.putExtra("taskBundle", taskToBundle(task, position));
        //presenter.editTask(task, position);
        setResult(RESULT_OK, data);
        finish();
    }

    private Bundle taskToBundle(Task task, int position) {
        Bundle bundle = new Bundle();
        bundle.putString("title", task.getTitle());
        bundle.putString("task", task.getTask());
        bundle.putInt("priority", task.getPriority());
        bundle.putString("date", task.getDate());
        bundle.putString("done", "0");
        bundle.putString("position", String.valueOf(position));
        return bundle;
    }

    private void callTimePicker() {
        // получаем текущее время
        final Calendar cal = Calendar.getInstance();
        int mHour = cal.get(Calendar.HOUR_OF_DAY);
        int mMinute = cal.get(Calendar.MINUTE);

        // инициализируем диалог выбора времени текущими значениями
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    String editTextTimeParam = hourOfDay + " : " + minute;
                    Log.d("Time", editTextTimeParam);
                    //editTextTime.setText(editTextTimeParam);
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void callDatePicker() {
        // получаем текущую дату
        final Calendar cal = Calendar.getInstance();
        int mYear = cal.get(Calendar.YEAR);
        int mMonth = cal.get(Calendar.MONTH);
        int mDay = cal.get(Calendar.DAY_OF_MONTH);

        // инициализируем диалог выбора даты текущими значениями
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    String editTextDateParam = dayOfMonth + "." + (monthOfYear + 1) + "." + year;
                    etDate.setText(editTextDateParam);
                    Log.d("Date", editTextDateParam);
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
        datePickerDialog.show();
    }

    public void onDateClick(View view) {
        hideKeyboard();
        callDatePicker();
    }

    private String getTodayDate() {
        final Calendar cal = Calendar.getInstance();
        int mYear = cal.get(Calendar.YEAR);
        int mMonth = cal.get(Calendar.MONTH) + 1;
        int mDay = cal.get(Calendar.DAY_OF_MONTH);

        return mDay + "." + mMonth + "." + mYear;
    }
}
