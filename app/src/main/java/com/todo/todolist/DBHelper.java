package com.todo.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "taskDb";
    private static final String TABLE_TASKS = "tasks";

    private static final String KEY_ID = "_id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TASK = "task";

    List<Task> taskList;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_TASKS + "(" + KEY_ID
                + " integer primary key autoincrement," + KEY_TITLE + " text," + KEY_TASK + " text" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_TASKS);
        onCreate(sqLiteDatabase);
    }

    public void saveTask(String title, String task) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TITLE, title);
        contentValues.put(KEY_TASK, task);
        sqLiteDatabase.insert(TABLE_TASKS, null, contentValues);
        sqLiteDatabase.close();
    }

    public List<Task> loadTask() {
        taskList = new ArrayList<>();
        String[] projection = {"title", "task"};
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("tasks", projection, null, null,
                null, null, null);
        while (cursor.moveToNext()) {
            taskList.add(new Task(cursor.getString(0), cursor.getString(1)));
        }
        return taskList;
    }
}