package com.todo.todolist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.todo.todolist.Interface.Contract;

import java.util.Calendar;
import java.util.List;

public class TaskActivity extends AppCompatActivity implements Contract.View {

    Contract.Presenter presenter;
    Button btAddTask, btApply;
    EditText etTitle, etTask, etDate;
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
        btApply = findViewById(R.id.btApply);
        etTask = findViewById(R.id.etTask);
        etTitle = findViewById(R.id.etTitle);
        etDate = findViewById(R.id.etDate);

        rgPriority = findViewById(R.id.rgPriority);
        rgPriority.check(R.id.radio_medium);
        requestCode = getIntent().getExtras();
        if (requestCode.get("requestCode").equals(1)) {
            btApply.setVisibility(View.GONE);
            etDate.setText(getTodayDate());
        }
        if (requestCode.get("requestCode").equals(2)) {
            btAddTask.setVisibility(View.GONE);
            etTitle.setText(getIntent().getStringExtra("title"));
            etTask.setText(getIntent().getStringExtra("task"));
            etDate.setText(getIntent().getStringExtra("date"));
        }
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
        if (etTitle.getText().toString().equals("") & etTask.getText().toString().equals(""))
            Toast.makeText(this, "Task is empty", Toast.LENGTH_LONG).show();
        else {
            Task task = new Task(etTitle.getText().toString(), etTask.getText().toString(), priority, etDate.getText().toString());
            presenter.saveTask(task);
            setResult(RESULT_OK);
            finish();
        }
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
        datePickerDialog.show();
    }

    public void onDateClick(View view) {
        callDatePicker();
    }

    public String getTodayDate() {
        final Calendar cal = Calendar.getInstance();
        int mYear = cal.get(Calendar.YEAR);
        int mMonth = cal.get(Calendar.MONTH) + 1;
        int mDay = cal.get(Calendar.DAY_OF_MONTH);

        return mDay + "." + mMonth + "." + mYear;
    }
}
