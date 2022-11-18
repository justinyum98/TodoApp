package com.example.todoapp.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.todoapp.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "toDoListDatabase";
    private static final String TODO_TABLE = "todoTable";
    private static final String ID = "id";
    private static final String TODO = "todo";
    private static final String STATUS = "status";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TODO + " TEXT, "
            + STATUS + " INTEGER)";
    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the older tables.
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        // Create tables again.
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void insertTodo(ToDoModel todo) {
        ContentValues cv = new ContentValues();
        cv.put(TODO, todo.getTodo());
        cv.put(STATUS, 0);
        db.insert(TODO_TABLE, null, cv);
    }

    @SuppressLint("Range")
    public List<ToDoModel> getAllTodos() {
        List<ToDoModel> todoList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try {
            cur = db.query(TODO_TABLE, null, null, null, null, null, null, null);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    do {
                        ToDoModel todo = new ToDoModel();
                        todo.setId(cur.getInt(cur.getColumnIndex(ID)));
                        todo.setTodo(cur.getString(cur.getColumnIndex(TODO)));
                        todo.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        todoList.add(todo);
                    } while (cur.moveToNext());
                }
            }
        } finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return todoList;
    }

    public void updateStatus(int id, int status) {
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] { String.valueOf(id) });
    }

    public void updateTodo(int id, String todo) {
        ContentValues cv = new ContentValues();
        cv.put(TODO, todo);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] { String.valueOf(id) });
    }

    public void deleteTodo(int id) {
        db.delete(TODO_TABLE, ID + "= ?", new String[] { String.valueOf(id) });
    }
}
