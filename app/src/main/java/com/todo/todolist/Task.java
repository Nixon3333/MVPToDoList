package com.todo.todolist;

import android.support.annotation.NonNull;

public class Task implements Comparable<Task> {

    String title;
    String task;
    int priority;

    public Task(String title, String task, int priority) {
        this.title = title;
        this.task = task;
        this.priority = priority;
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

    @Override
    public int compareTo(@NonNull Task task) {
        return Integer.compare(priority, task.getPriority());
    }
}
