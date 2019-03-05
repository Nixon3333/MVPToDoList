package com.todo.todolist;

public class Task {

    String title;
    String task;

    public Task(String title, String task) {
        this.title = title;
        this.task = task;
    }

    public String getTitle() {
        return title;
    }

    public String getTask() {
        return task;
    }
}
