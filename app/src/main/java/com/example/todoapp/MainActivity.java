package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.todoapp.Adapter.ToDoAdapter;
import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.Utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {

    private RecyclerView todosRecyclerView;
    private ToDoAdapter todosAdapter;
    private FloatingActionButton fab;

    private List<ToDoModel> todoList;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        db = new DatabaseHandler(this);
        db.openDatabase();

        todosRecyclerView = findViewById(R.id.todosRecyclerView);
        todosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        todosAdapter = new ToDoAdapter(db, this);
        todosRecyclerView.setAdapter(todosAdapter);

        fab = findViewById(R.id.fab);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(todosAdapter));
        itemTouchHelper.attachToRecyclerView(todosRecyclerView);

        todoList = db.getAllTodos();
        Collections.reverse(todoList);
        todosAdapter.setTodos(todoList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewTodo.newInstance().show(getSupportFragmentManager(), AddNewTodo.TAG);
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        todoList = db.getAllTodos();
        Collections.reverse(todoList);
        todosAdapter.setTodos(todoList);
        todosAdapter.notifyDataSetChanged();
    }
}