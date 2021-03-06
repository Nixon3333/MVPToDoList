package com.todo.todolist.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.todo.todolist.R;
import com.todo.todolist.model.ModelMain;
import com.todo.todolist.model.pojo.Task;
import com.todo.todolist.ui.MainActivity;

import java.util.Calendar;
import java.util.List;

public class BroadcastManager extends BroadcastReceiver {

    ModelMain modelMain;
    NotificationCompat.Builder notifBuilder;
    boolean showNotification = false;


    @Override
    public void onReceive(Context context, Intent intent) {

        modelMain = new ModelMain();
        List<Task> dates = modelMain.loadTasks(context);
        StringBuilder sb = new StringBuilder();

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        for (Task task : dates) {
            Log.d("Millis", String.valueOf(dateToMillis(task.getDate())) + " : " + task.getDate());
            Log.d("MillisCur", String.valueOf(System.currentTimeMillis()) + " : " + Calendar.getInstance().getTime());
            Log.d("MillisDif", String.valueOf(dateToMillis(task.getDate()) - System.currentTimeMillis()));
            if (task.isDone() != 1 & task.getDoRemind() == 1) {
                if (dateToMillis(task.getDate()) - System.currentTimeMillis() <= 86400000 & dateToMillis(task.getDate()) - System.currentTimeMillis() >= -10000) {

                    sb.append(task.getTitle()).append(", ");
                    showNotification = true;
                }
            }
        }

        NotificationChannel mChannel = null;

        int importance = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            importance = NotificationManager.IMPORTANCE_HIGH;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel("Channel-01", "MyChannel", importance);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(mChannel);
            }
        }

        if (showNotification) {
            notifBuilder = new NotificationCompat.Builder(context)

                    .setSmallIcon(R.drawable.ic_add_white)
                    .setContentTitle("Coming tasks")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(sb.substring(0, sb.length() - 2)))
                    .setContentText(sb.substring(0, sb.length() - 2))
                    .setChannelId("Channel");
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
            notifBuilder.setContentIntent(contentIntent);

            if (mNotificationManager != null) {
                mNotificationManager.notify(1, notifBuilder.build());
                showNotification = false;
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
