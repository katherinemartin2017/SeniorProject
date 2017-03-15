package com.russellmartin.mylistsapplication;

import android.content.CursorLoader;
import android.content.Loader;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.russellmartin.mylistsapplication.data.DatabaseHelper;
import com.russellmartin.mylistsapplication.data.MyListsContract;
import com.russellmartin.mylistsapplication.data.MyListsContract.*;
import com.russellmartin.mylistsapplication.data.MyListsProvider;

import java.util.ArrayList;
import java.util.List;

// This Android Activity was created by modifing code from the PluralSite tutorial --> Building your First Android App with SQLite
// This Activity controls the list of todos items and the actions controlled on them

// Created by: Katherine Martin 2/13/17

public class TodoListActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{
    static final int ALL_RECORDS = -1;
    private static final int URL_LOADER = 0;
    Cursor cursor;
    TodosCursorAdapter adapter;


    private void updateTodo() {
        int id = 2;
        String[] args = {String.valueOf(id)};
        ContentValues values = new ContentValues();
        values.put(TodosEntry.COLUMN_TEXT, "Call Mr Clark");
        int numRows = getContentResolver().update(TodosEntry.CONTENT_URI, values,
                TodosEntry._ID + "=?", args);

        Log.d("Update Rows", String.valueOf(numRows));
    }

    private void deleteTodo(int id) {

        getContentResolver().delete(TodosEntry.CONTENT_URI,CategoriesEntry._ID + "=" + id, null);
    }

    private void createCategory() {
        ContentValues values = new ContentValues();
        values.put(MyListsContract.CategoriesEntry.COLUMN_DESCRIPTION, "School");
        Uri uri = getContentResolver().insert(MyListsContract.CategoriesEntry.CONTENT_URI, values);
        Log.d("MainActivity", "Inserted Category" + uri);

    }

    private void createList() {
        ContentValues values = new ContentValues();
        values.put(MyListsContract.ListEntry.COLUMN_LIST_TEXT, "Home Depot Shopping");
        Uri uri = getContentResolver().insert(MyListsContract.ListEntry.CONTENT_URI, values);
        Log.d("MainActivity", "Inserted List" + uri);
    }

    private void createTestTodos() {

        for (int i = 1; i < 20; i++) {
            ContentValues values = new ContentValues();
            values.put(MyListsContract.TodosEntry.COLUMN_TEXT, "Todo Item #" + i);
            values.put(MyListsContract.TodosEntry.COLUMN_CATEGORY, 1);
            values.put(MyListsContract.TodosEntry.COLUMN_CREATED, "2017-03-05");
            values.put(MyListsContract.TodosEntry.COLUMN_DONE, 0);
            values.put(MyListsContract.TodosEntry.COLUMN_LIST, 1);
            Uri uri = getContentResolver().insert(TodosEntry.CONTENT_URI, values);
        }

    }


    private void readData() {
        // Reads the list from mylistsapp.db file and adds them to a list of Strings

        String[] projection = {TodosEntry.COLUMN_TEXT,
                TodosEntry.TABLE_NAME + "." + TodosEntry._ID,
                TodosEntry.COLUMN_CREATED,
                TodosEntry.COLUMN_EXPIRED,
                TodosEntry.COLUMN_DONE,
                CategoriesEntry.TABLE_NAME + "." +
                        MyListsContract.CategoriesEntry.COLUMN_DESCRIPTION,
                ListEntry.TABLE_NAME + "." +
                        MyListsContract.ListEntry.COLUMN_LIST_TEXT};
        cursor = getContentResolver().query(TodosEntry.CONTENT_URI, projection, null, null, null);

       /* int i = c.getCount();
        Log.d("Record Count", String.valueOf(i));
        String rowContent = "";
        while(c.moveToNext()){
            for(i=0; i<=6; i++){
                rowContent += c.getString(i) + " -";
            }
            Log.i("Row" + String.valueOf(c.getPosition()), rowContent);
            rowContent = "";
        }
        c.close();*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getLoaderManager().initLoader(URL_LOADER, null, this);
        final ListView lv = (ListView) findViewById(R.id.lvTodos);
        adapter = new TodosCursorAdapter(this, cursor, false);
        lv.setAdapter(adapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Adds the layout to the application including the list display and the toolbar
       /*lv.setAdapter(new ArrayAdapter<String>(this, R.layout.todo_list_item,
                R.id.tvNote,listname));*/

/*        //Creates the editText for when an item is clicked
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Intent intent = new Intent(TodoListActivity.this, TodoActivity.class);
                String content= (String) lv.getItemAtPosition(pos);
                intent.putExtra("Content", content);
                startActivity(intent);
            }
        });*/
        // Controls when the User clicks the "Plus" fab in order to create new todos object
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement

        switch (id) {
            case R.id.action_categories:
                //get the categories cursor for the
                Intent intent = new Intent(TodoListActivity.this, CategoryActivity.class);
                startActivity(intent);
                break;
            case R.id.action_delete_all_todos:
                deleteTodo(1);
                break;
            case R.id.action_create_test_data:
                createTestTodos();
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {TodosEntry.COLUMN_TEXT,
                TodosEntry.TABLE_NAME + "." + TodosEntry._ID,
                TodosEntry.COLUMN_CREATED,
                TodosEntry.COLUMN_EXPIRED,
                TodosEntry.COLUMN_DONE,
                CategoriesEntry.TABLE_NAME + "." +
                        MyListsContract.CategoriesEntry.COLUMN_DESCRIPTION,
                ListEntry.TABLE_NAME + "." +
                        MyListsContract.ListEntry.COLUMN_LIST_TEXT};

        return new CursorLoader(this,
                TodosEntry.CONTENT_URI,
                projection,
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);

    }
}