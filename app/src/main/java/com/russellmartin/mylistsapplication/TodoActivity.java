package com.russellmartin.mylistsapplication;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class TodoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        Intent intent = getIntent();
        String context = intent.getStringExtra("Content");
        EditText editTodo = (EditText) findViewById(R.id.editTodo);
        editTodo.setText(context);
    }
}

