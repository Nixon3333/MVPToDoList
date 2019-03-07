package com.todo.todolist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.todo.todolist.model.Model;
import com.todo.todolist.model.Task;

import java.util.Calendar;
import java.util.List;

public class BroadcastManager extends BroadcastReceiver {

    Model model;

    @Override
    public void onReceive(Context context, Intent intent) {

        model = new Model();
        List<Task> dates = model.loadTasks(context);
        StringBuilder sb = new StringBuilder();
        for (Task task : dates) {
            //Log.d("Dates", date);
            //Log.d("Dates", String.valueOf(dateToMillis(date)));
            Log.d("Millis", String.valueOf(dateToMillis(task.getDate())) + " : " + task.getDate());
            Log.d("MillisCur", String.valueOf(System.currentTimeMillis()) + " : " + Calendar.getInstance().getTime());
            if (dateToMillis(task.getDate()) - System.currentTimeMillis() < 50400000) {

                sb.append(task.getDate()).append(" ");
            }
        }
        Toast.makeText(context, sb.toString(), Toast.LENGTH_LONG).show();

    }

    private long dateToMillis(String date) {
        Calendar calendar = Calendar.getInstance();
        String[] dateArr = date.split("\\.");
        calendar.set(Integer.valueOf(dateArr[2]), Integer.valueOf(dateArr[1]) - 1, Integer.valueOf(dateArr[0]));
        Log.d("DatesCal", String.valueOf(calendar.getTime()));
        return calendar.getTimeInMillis();

    }

}
