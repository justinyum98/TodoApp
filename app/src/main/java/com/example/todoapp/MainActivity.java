package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.todoapp.Adapter.ToDoAdapter;
import com.example.todoapp.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView todosRecyclerView;
    private ToDoAdapter todosAdapter;

    private List<ToDoModel> todoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        todoList = new ArrayList<>();

        todosRecyclerView = findViewById(R.id.todosRecyclerView);
        todosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        todosAdapter = new ToDoAdapter(this);
        todosRecyclerView.setAdapter(todosAdapter);

        ToDoModel todo = new ToDoModel();
        todo.setTodo("This is a Test Task");
        todo.setStatus(0);
        todo.setId(1);

        todoList.add(todo);
        todoList.add(todo);
        todoList.add(todo);
        todoList.add(todo);
        todoList.add(todo);

        todosAdapter.setTodos(todoList);
    }
}