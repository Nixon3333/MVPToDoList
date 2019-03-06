package com.todo.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "taskDb";
    private static final String TABLE_TASKS = "tasks";

    private static final String KEY_ID = "_id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TASK = "task";
    private static final String KEY_PRIORITY = "priority";

    static List<Task> taskList = new ArrayList<>();

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_TASKS + "(" + KEY_ID
                + " integer primary key autoincrement," + KEY_TITLE + " text," + KEY_TASK + " text," + KEY_PRIORITY + " integer" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_TASKS);
        onCreate(sqLiteDatabase);
    }

    public void saveTask(String title, String task, int priority) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TITLE, title);
        contentValues.put(KEY_TASK, task);
        contentValues.put(KEY_PRIORITY, priority);
        sqLiteDatabase.insert(TABLE_TASKS, null, contentValues);
        sqLiteDatabase.close();
    }

    public List<Task> loadTask() {
        taskList = new ArrayList<>();
        String[] projection = {"title", "task", "priority"};
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("tasks", projection, null, null,
                null, null, null);
        while (cursor.moveToNext()) {
            taskList.add(new Task(cursor.getString(0), cursor.getString(1), cursor.getInt(2)));
        }
        sqLiteDatabase.close();
        Collections.sort(taskList);
        return taskList;
    }

    public void editTask(String title, String task, int priority, int position) {
        Log.d("edit", taskList.get(position).getTitle());

        String[] whereArgs = new String[3];
        whereArgs[0] = taskList.get(position).getTitle();
        whereArgs[1] = taskList.get(position).getTask();
        whereArgs[2] = String.valueOf(taskList.get(position).getPriority());
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("task", task);
        contentValues.put("priority", priority);
        sqLiteDatabase.update("tasks", contentValues, "title = ? AND task = ? AND priority = ?", whereArgs);
        sqLiteDatabase.close();
    }
}
