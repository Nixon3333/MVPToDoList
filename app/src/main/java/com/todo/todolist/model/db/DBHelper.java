package com.todo.todolist.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.todo.todolist.model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "taskDb";
    private static final String TABLE_NAME = "tasks";

    private static final String KEY_ID = "_id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TASK = "task";
    private static final String KEY_PRIORITY = "priority";
    private static final String KEY_DATE = "date";
    private static final String KEY_DONE = "done";

    private static List<Task> taskList = new ArrayList<>();
    private List<String> datesList = new ArrayList<>();

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME + "(" + KEY_ID
                + " integer primary key autoincrement," + KEY_TITLE + " text," + KEY_TASK + " text,"
                + KEY_PRIORITY + " integer," + KEY_DATE + " text," + KEY_DONE + " integer default 0" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        switch (i) {
            case 1:
                sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + KEY_DONE + " integer default 0");
        }
        //sqLiteDatabase.execSQL("drop table if exists " + TABLE_NAME);
        //onCreate(sqLiteDatabase);
    }

    public void saveTask(Task task) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TITLE, task.getTitle());
        contentValues.put(KEY_TASK, task.getTask());
        contentValues.put(KEY_PRIORITY, task.getPriority());
        contentValues.put(KEY_DATE, task.getDate());
        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();
    }

    public List<Task> loadTask() {
        taskList = new ArrayList<>();
        String[] projection = {"title", "task", "priority", "date", "done"};
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("tasks", projection, null, null,
                null, null, null);
        while (cursor.moveToNext()) {
            taskList.add(new Task(cursor.getString(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3), cursor.getInt(4)));
        }
        cursor.close();
        sqLiteDatabase.close();
        Collections.sort(taskList);
        return taskList;
    }

    public void editTask(Task task, int position) {
        Log.d("edit", taskList.get(position).getTitle());

        String[] whereArgs = new String[3];
        whereArgs[0] = taskList.get(position).getTitle();
        whereArgs[1] = taskList.get(position).getTask();
        whereArgs[2] = String.valueOf(taskList.get(position).getPriority());
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", task.getTitle());
        contentValues.put("task", task.getTask());
        contentValues.put("priority", task.getPriority());
        contentValues.put("date", task.getDate());
        sqLiteDatabase.update("tasks", contentValues, "title = ? AND task = ? AND priority = ?", whereArgs);
        sqLiteDatabase.close();
    }

    public void deleteTask(int position) {
        String[] whereArgs = new String[4];
        whereArgs[0] = taskList.get(position).getTitle();
        whereArgs[1] = taskList.get(position).getTask();
        whereArgs[2] = String.valueOf(taskList.get(position).getPriority());
        whereArgs[3] = taskList.get(position).getDate();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete("tasks", "title = ? AND task = ? AND priority = ? AND date = ?", whereArgs);
        sqLiteDatabase.close();
    }

    public String[] getEditTask(int position) {
        String[] task = new String[4];
        task[0] = taskList.get(position).getTitle();
        task[1] = taskList.get(position).getTask();
        task[2] = taskList.get(position).getDate();
        task[3] = String.valueOf(taskList.get(position).getPriority());
        return task;
    }

    public List<String> getTaskDates() {
        datesList = new ArrayList<>();
        String[] projection = {"date"};
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("tasks", projection, null, null,
                null, null, null);
        while (cursor.moveToNext()) {
            datesList.add(cursor.getString(0));
        }
        cursor.close();
        sqLiteDatabase.close();
        return datesList;
    }

    public void switchDone(int position) {
        Task task = taskList.get(position);
        String[] whereArgs = new String[4];
        whereArgs[0] = task.getTitle();
        whereArgs[1] = task.getTask();
        whereArgs[2] = String.valueOf(task.getPriority());
        whereArgs[3] = task.getDate();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", task.getTitle());
        contentValues.put("task", task.getTask());
        contentValues.put("priority", task.getPriority());
        contentValues.put("date", task.getDate());
        if (task.isDone() == 1)
            contentValues.put("done", 0);
        else
            contentValues.put("done", 1);
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.update("tasks", contentValues, "title = ? AND task = ? AND priority = ? AND date = ?", whereArgs);
        sqLiteDatabase.close();
    }
}
