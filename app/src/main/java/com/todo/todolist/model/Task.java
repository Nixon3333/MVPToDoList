package com.todo.todolist.model;

import android.support.annotation.NonNull;
import java.util.Calendar;
import java.util.Date;

public class Task implements Comparable<Task> {

    private String title;
    private String task;
    private int priority;
    private String date;

    public Task(String title, String task, int priority, String date) {
        this.title = title;
        this.task = task;
        this.priority = priority;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getTask() {
        return task;
    }

    public int getPriority() {
        return priority;
    }

    public String getDate() {
        return date;
    }

    @Override
    public int compareTo(@NonNull Task task) {
        if (Integer.compare(priority, task.getPriority()) == 0) {

            return convertDate(this.date).compareTo(convertDate(task.getDate()));
        }

        return Integer.compare(priority, task.getPriority());
    }

    private Date convertDate(String date) {
        Calendar calendar = Calendar.getInstance();
        String[] dateArr = date.split("\\.");
        calendar.set(Integer.valueOf(dateArr[2]) + 1900, Integer.valueOf(dateArr[1]), Integer.valueOf(dateArr[0]));
        return calendar.getTime();
    }
}
