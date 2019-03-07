package com.todo.todolist;

import android.support.annotation.NonNull;

import java.util.Date;

public class Task implements Comparable<Task> {

    String title;
    String task;
    int priority;
    String date;

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
        String[] dateArr = date.split("\\.");
        Date myDate = new Date(Integer.valueOf(dateArr[2]), Integer.valueOf(dateArr[1]), Integer.valueOf(dateArr[0]));
        return myDate;
    }
}
