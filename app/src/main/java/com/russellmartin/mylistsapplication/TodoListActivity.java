package com.russellmartin.mylistsapplication;

import android.content.CursorLoader;
import android.content.Loader;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
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
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.DialogFragment;
import android.widget.Spinner;


import com.russellmartin.mylistsapplication.data.DatabaseHelper;
import com.russellmartin.mylistsapplication.data.MyListsContract;
import com.russellmartin.mylistsapplication.data.MyListsContract.*;
import com.russellmartin.mylistsapplication.data.MyListsProvider;
import com.russellmartin.mylistsapplication.data.TodosQueryHandler;
import com.russellmartin.mylistsapplication.databinding.ActivityCategoryBinding;
import com.russellmartin.mylistsapplication.databinding.ActivityTodoBinding;
import com.russellmartin.mylistsapplication.databinding.TodoListItemBinding;
import com.russellmartin.mylistsapplication.model.Category;
import com.russellmartin.mylistsapplication.model.CategoryList;
import com.russellmartin.mylistsapplication.model.Todo;

import java.util.ArrayList;
import java.util.List;

// This Android Activity was created by modifing code from the PluralSite tutorial --> Building your First Android App with SQLite
// This Activity controls the list of todos items and the actions controlled on them

// Created by: Katherine Martin 2/13/17

public class TodoListActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{
    static final int ALL_CATEGORIES = -1;
    static final int ALL_RECORDS = -1;
    private static final int URL_LOADER = 0;
    Cursor cursor;
    TodosCursorAdapter adapter;
    CategoryList list = new CategoryList();

    Spinner spinner;
    CategoryListAdapter categoryAdapter;

    
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

        String[] args = {String.valueOf(id)};
        if(id == ALL_RECORDS){
            args = null;
        }
        TodosQueryHandler handler = new TodosQueryHandler(
                this.getContentResolver());
        handler.startDelete(1, null, TodosEntry.CONTENT_URI, TodosEntry._ID + " =?", args);

        /*getContentResolver().delete(TodosEntry.CONTENT_URI,CategoriesEntry._ID + "=" + id, null);*/
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
            values.put(MyListsContract.TodosEntry.COLUMN_LIST, 1);
            values.put(MyListsContract.TodosEntry.COLUMN_CREATED, "2017-03-05");
            values.put(MyListsContract.TodosEntry.COLUMN_EXPIRED, "2018-07-20");
            int done = (i%2 ==1) ? 1 : 0;
            values.put(MyListsContract.TodosEntry.COLUMN_DONE, 1);
            TodosQueryHandler handler = new TodosQueryHandler(
                    this.getContentResolver()
            );
            handler.startInsert(1, null, TodosEntry.CONTENT_URI, values);

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
        spinner = (Spinner) findViewById(R.id.spinCategories);
        getLoaderManager().initLoader(URL_LOADER, null, this);
        setCategories();
        final ListView lv = (ListView) findViewById(R.id.lvTodos);
        adapter = new TodosCursorAdapter(this, cursor, false);
        lv.setAdapter(adapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Intent intent = getIntent();
        setSupportActionBar(toolbar);


        //Creates the editText for when an item is clicked
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                cursor = (Cursor) adapterView.getItemAtPosition(pos);
                int todoID = cursor.getInt(cursor.getColumnIndex(TodosEntry._ID));
                String todoText = cursor.getString(cursor.getColumnIndex(TodosEntry.COLUMN_TEXT));
                String todoExpiredDate = cursor.getString(cursor.getColumnIndex(TodosEntry.COLUMN_EXPIRED));
                int todoDone = cursor.getInt(cursor.getColumnIndex(TodosEntry.COLUMN_DONE));
                String todoCreated = cursor.getString(cursor.getColumnIndex(TodosEntry.COLUMN_CREATED));
                String todoCategory = cursor.getString(cursor.getColumnIndex(TodosEntry.COLUMN_CATEGORY));
                String todoList = cursor.getString(cursor.getColumnIndex(TodosEntry.COLUMN_LIST));
                boolean boolDone = (todoDone == 1);
                Todo todo = new Todo(todoID, todoText, todoExpiredDate, todoCreated,
                        boolDone, todoCategory, todoList);

                Intent intent = new Intent(TodoListActivity.this, TodoActivity.class);
                intent.putExtra("todo", todo);
                startActivity(intent);
            }
        });

        // Controls when the User clicks the "Plus" fab in order to create new todos object
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Todo todo = new Todo(0, "", "", "", false, "0", "1");
                Intent intent = new Intent(TodoListActivity.this, TodoActivity.class);
                //pass the ID to the todoActivity
                intent.putExtra("todo", todo);
                intent.putExtra("categories", list);
                startActivity(intent);
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
    private void setCategories(){
        final TodosQueryHandler categoriesHandler = new TodosQueryHandler(
                this.getContentResolver()) {
            @Override
            protected void onQueryComplete(int token, Object cookie,
                                           Cursor cursor) {
                try {
                    if ((cursor != null)) {
                        int i = 0;
                        list.ItemList.add(i, new Category(ALL_CATEGORIES, "All Categories"));
                        i++;
                        while (cursor.moveToNext()) {
                            list.ItemList.add(i, new Category(
                                    cursor.getInt(0),
                                    cursor.getString(1)
                            ));
                            i++;
                        }
                    }
                } finally {
                    //cm = null;
                }
            }
        };
        categoriesHandler.startQuery(1, null, MyListsContract.CategoriesEntry.CONTENT_URI, null, null, null,
                MyListsContract.CategoriesEntry.COLUMN_DESCRIPTION);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {TodosEntry.COLUMN_TEXT,
                TodosEntry.TABLE_NAME + "." + TodosEntry._ID,
                TodosEntry.COLUMN_CREATED,
                TodosEntry.COLUMN_EXPIRED,
                TodosEntry.COLUMN_DONE,
                TodosEntry.COLUMN_CATEGORY,
                TodosEntry.COLUMN_LIST,
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
        if(categoryAdapter == null){
            categoryAdapter = new CategoryListAdapter(list.ItemList);
            spinner.setAdapter(categoryAdapter);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);

    }
}