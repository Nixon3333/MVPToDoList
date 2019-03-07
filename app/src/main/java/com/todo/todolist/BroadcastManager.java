package com.todo.todolist;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.todo.todolist.model.Model;
import com.todo.todolist.model.Task;

import java.util.Calendar;
import java.util.List;

public class BroadcastManager extends BroadcastReceiver {

    Model model;
    NotificationCompat.Builder notifBuilder;
    boolean showNotification = false;


    @Override
    public void onReceive(Context context, Intent intent) {

        model = new Model();
        List<Task> dates = model.loadTasks(context);
        StringBuilder sb = new StringBuilder();

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        for (Task task : dates) {
            Log.d("Millis", String.valueOf(dateToMillis(task.getDate())) + " : " + task.getDate());
            Log.d("MillisCur", String.valueOf(System.currentTimeMillis()) + " : " + Calendar.getInstance().getTime());
            Log.d("MillisDif", String.valueOf(dateToMillis(task.getDate()) - System.currentTimeMillis()));
            if (dateToMillis(task.getDate()) - System.currentTimeMillis() <= 86400000 & dateToMillis(task.getDate()) - System.currentTimeMillis() >= -10000) {

                sb.append(task.getTask()).append(", ");
                showNotification = true;
            }
        }
        notifBuilder = new NotificationCompat.Builder(context)

                .setSmallIcon(R.drawable.ic_add_white)
                .setContentTitle("Coming tasks")
                .setContentText(sb)
                .setChannelId("Channel");

        NotificationChannel mChannel = null;

        int importance = NotificationManager.IMPORTANCE_HIGH;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel("Channel", "MyChannel", importance);
            mNotificationManager.createNotificationChannel(mChannel);
        }

        if (showNotification)
            mNotificationManager.notify(1, notifBuilder.build());

    }

    private long dateToMillis(String date) {
        Calendar calendar = Calendar.getInstance();
        String[] dateArr = date.split("\\.");
        calendar.set(Integer.valueOf(dateArr[2]), Integer.valueOf(dateArr[1]) - 1, Integer.valueOf(dateArr[0]));
        Log.d("DatesCal", String.valueOf(calendar.getTime()));
        return calendar.getTimeInMillis();

    }

}
