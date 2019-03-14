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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.todo.todolist.R;
import com.todo.todolist.contractApi.Contract;
import com.todo.todolist.model.Task;
import com.todo.todolist.presenter.Presenter;
import com.todo.todolist.utils.CONST;
import com.todo.todolist.utils.Groups;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class TaskActivity extends AppCompatActivity implements Contract.View {

    private Contract.Presenter presenter;
    private Button btAddTask, btApply;
    private EditText etTitle, etTask, etDate;
    private RadioGroup rgPriority;
    private Bundle requestCode;
    private TextView tvPriority;
    private CheckBox cbRemind;
    private Spinner spinGroup;

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

        spinGroup = findViewById(R.id.spinGroup);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Groups.getGroupList());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinGroup.setAdapter(adapter);

        rgPriority = findViewById(R.id.rgPriority);

        tvPriority = findViewById(R.id.tvPriority);
        tvPriority.setText(R.string.priority);

        cbRemind = findViewById(R.id.cbRemind);

        //Show keyboard and request focus to EditText
        showKeyboard();
        etTitle.requestFocus();


        requestCode = getIntent().getExtras();

        if (requestCode != null && Objects.equals(requestCode.get("requestCode"), CONST.ADD_REQUEST_CODE)) {
            rgPriority.check(R.id.radio_medium);
            btApply.setVisibility(View.GONE);
            etDate.setText(presenter.getCurrentDate());
        }
        if (requestCode != null && Objects.requireNonNull(requestCode.get("requestCode")).equals(CONST.EDIT_REQUEST_CODE)) {

            btAddTask.setVisibility(View.GONE);

            etTitle.setText(getIntent().getStringExtra("title"));
            etTask.setText(getIntent().getStringExtra("task"));
            etDate.setText(getIntent().getStringExtra("date"));

            int selection = 0;
            switch (getIntent().getStringExtra("group")) {
                case "General":
                    selection = 0;
                    break;
                case "Person":
                    selection = 1;
                    break;
                case "Work":
                    selection = 2;
                    break;
                case "Other":
                    selection = 3;
                    break;
            }
            spinGroup.setSelection(selection);

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
        int doRemind = 1;
        if (cbRemind.isChecked())
            doRemind = 0;
        if (etTitle.getText().toString().equals("") & etTask.getText().toString().equals(""))
            Toast.makeText(this, R.string.toast_text_task_is_empty, Toast.LENGTH_LONG).show();
        else {
            Task task = new Task(etTitle.getText().toString(), etTask.getText().toString(),
                    priority, etDate.getText().toString(), 0, doRemind, spinGroup.getSelectedItem().toString());
            presenter.saveTask(task);
            setResult(RESULT_OK);
            finish();
        }
    }

    public void onEditTaskClick(View view) {
        hideKeyboard();
        int priority = 0;
        int position = 0;
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
        if (requestCode != null) {
            position = requestCode.getInt("adapterPosition");
        }
        int doRemind = 1;
        if (cbRemind.isChecked())
            doRemind = 0;
        Task task = new Task(etTitle.getText().toString(), etTask.getText().toString(),
                priority, etDate.getText().toString(), 0, doRemind, spinGroup.getSelectedItem().toString());
        Intent data = new Intent();
        data.putExtra("taskBundle", taskToBundle(task, position));

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
        bundle.putString("group", task.getGroup());
        boolean doRemind = true;
        if (task.getDoRemind() == 0)
            doRemind = false;
        bundle.putBoolean("remind", doRemind);
        return bundle;
    }

    /*private void callTimePicker() {
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
    }*/

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

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(etTitle.getWindowToken(), 0);
        }
    }
}
