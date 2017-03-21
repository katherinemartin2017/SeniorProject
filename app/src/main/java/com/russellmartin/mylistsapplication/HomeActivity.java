package com.russellmartin.mylistsapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.russellmartin.mylistsapplication.data.DatabaseHelper;
import com.russellmartin.mylistsapplication.data.MyListsContract;
import com.russellmartin.mylistsapplication.model.ItemList;
import com.russellmartin.mylistsapplication.model.Todo;

// This Android Activity was created by modifying code from the PluralSite tutorial --> Building your First Android App with SQLite
// This Activity controls the the listView of lists that can be opened in order to display todos list items

// Created by: Katherine Martin 2/13/17

public class HomeActivity extends AppCompatActivity {
    Cursor cursor;
    String[] itemname ={
            "Grocery List",
            "Todo List",
            "Spring Break Packing",
            "Home Shopping List"
    };
    private void readData() {
        // Reads the list from mylistsapp.db file and adds them to a list of Strings

        String[] projection = {MyListsContract.ListEntry.COLUMN_LIST_TEXT,
                MyListsContract.ListEntry.TABLE_NAME + "." + MyListsContract.ListEntry._ID};
        cursor = getContentResolver().query(MyListsContract.TodosEntry.CONTENT_URI, projection, null, null, null);
    }
        @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);
            Toolbar toolbar = (Toolbar)  findViewById(R.id.toolbar2);
            setSupportActionBar(toolbar);
            final ListView lv=(ListView) findViewById(R.id.lvLists);
            /*readData();
            ListsCursorAdapter adapter = new ListsCursorAdapter(this, cursor, false);
            lv.setAdapter(adapter);*/

        // Adds the layout to the application including the list display and the toolbar
        lv.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item,
                R.id.tvList,itemname));

        // Controls when the User clicks the list names in order to open lists
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Intent intent = new Intent(HomeActivity.this, TodoListActivity.class);
                startActivity(intent);
              /*  //Info for list
                        cursor = (Cursor) adapterView.getItemAtPosition(pos);
                        int listID = cursor.getInt(cursor.getColumnIndex(MyListsContract.ListEntry._ID));
                        String listText = cursor.getString(cursor.getColumnIndex(MyListsContract.ListEntry.COLUMN_LIST_TEXT));
                        ItemList list = new ItemList(listID, listText);

                        Intent intent = new Intent(HomeActivity.this, TodoListActivity.class);
                        intent.putExtra("list", list);
                        startActivity(intent);*/

            }
        });

        // Controls when the User clicks the "Plus" fab in order to create lists
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}