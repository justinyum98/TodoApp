package com.example.todoapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.Utils.DatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class AddNewTodo extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";

    private EditText newTodoText;
    private Button newTodoSaveButton;
    private DatabaseHandler db;

    public static AddNewTodo newInstance() {
        return new AddNewTodo();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_todo, container, false);
        Objects.requireNonNull(getDialog()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newTodoText = requireView().findViewById(R.id.newTodoText);
        newTodoSaveButton = requireView().findViewById(R.id.newTodoButton);

        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String todo = bundle.getString("todo");
            newTodoText.setText(todo);
            assert todo != null;
            if (todo.length() > 0) {
                newTodoSaveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark));
            }
        }

        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        newTodoText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")) {
                    newTodoSaveButton.setEnabled(false);
                    newTodoSaveButton.setTextColor(Color.GRAY);
                } else {
                    newTodoSaveButton.setEnabled(true);
                    newTodoSaveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        final boolean finalIsUpdate = isUpdate;
        newTodoSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = newTodoText.getText().toString();
                if (finalIsUpdate) {
                    db.updateTodo(bundle.getInt("id"), text);
                } else {
                    ToDoModel todo = new ToDoModel();
                    todo.setTodo(text);
                    todo.setStatus(0);
                    db.insertTodo(todo);
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        Activity activity = getActivity();
        if (activity instanceof DialogCloseListener) {
            ((DialogCloseListener)activity).handleDialogClose(dialog);
        }
    }
}
