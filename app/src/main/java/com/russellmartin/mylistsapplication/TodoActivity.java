package com.russellmartin.mylistsapplication;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.russellmartin.mylistsapplication.databinding.ActivityTodoBinding;
import com.russellmartin.mylistsapplication.model.Todo;

public class TodoActivity extends AppCompatActivity {
    Todo todo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        Intent intent = getIntent();
        todo = (Todo) intent.getSerializableExtra("todo");
        ActivityTodoBinding binding =
                DataBindingUtil.setContentView(this,R.layout.activity_todo);
        binding.setTodo(todo);


    }
}

