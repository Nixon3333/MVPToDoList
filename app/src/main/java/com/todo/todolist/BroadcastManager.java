package com.todo.todolist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.todo.todolist.model.Model;

import java.util.Calendar;
import java.util.List;

public class BroadcastManager extends BroadcastReceiver {

    Model model;

    @Override
    public void onReceive(Context context, Intent intent) {

        model = new Model();
        List<String> dates = model.getDates(context);
        for (String date : dates) {
            Log.d("Dates", date);
            Log.d("Dates", String.valueOf(dateToMillis(date)));
            if (dateToMillis(date) > System.currentTimeMillis()) {
                Toast.makeText(context, "Hello " + date, Toast.LENGTH_LONG).show();
            }
        }

    }

    private long dateToMillis(String date) {
        Calendar calendar = Calendar.getInstance();
        String[] dateArr = date.split("\\.");
        calendar.set(Integer.valueOf(dateArr[2]), Integer.valueOf(dateArr[1]) - 1, Integer.valueOf(dateArr[0]));
        Log.d("DatesCal", String.valueOf(calendar.getTime()));
        return calendar.getTimeInMillis();

    }

}
