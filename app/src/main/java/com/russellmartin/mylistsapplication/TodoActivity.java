package com.russellmartin.mylistsapplication;


import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.support.v7.app.AlertDialog;

import com.russellmartin.mylistsapplication.data.MyListsContract;
import com.russellmartin.mylistsapplication.data.TodosQueryHandler;
import com.russellmartin.mylistsapplication.databinding.ActivityTodoBinding;
import com.russellmartin.mylistsapplication.model.Category;
import com.russellmartin.mylistsapplication.model.CategoryList;
import com.russellmartin.mylistsapplication.model.ItemList;
import com.russellmartin.mylistsapplication.model.Todo;

public class TodoActivity extends AppCompatActivity {
    Todo todo;
    TodosQueryHandler handler;
    Spinner spinner;
    CategoryList catList;;
    ItemList lists;
    CategoryListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int position=0;
        handler =  new TodosQueryHandler(getContentResolver());
        ActivityTodoBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_todo);
        Intent intent = getIntent();
        todo = (Todo)intent.getSerializableExtra("todo");
        /*catList = (CategoryList) intent.getSerializableExtra("categories");adapter
        adapter = new CategoryListAdapter(catList.ItemList);
        spinner=(Spinner) findViewById(R.id.spCategories);
        spinner.setAdapter(adapter);*/
        //set the bindings
        binding.setTodo(todo);
        //spinner, selected right
       /* if (Integer.valueOf(todo.category.get()) == 0) {
            position = 1;
        }
        else {
            for (Category cat : catList.ItemList) {
                if (Integer.valueOf(cat.catID.get()) == Integer.valueOf(todo.category.get())) {
                    break;
                }
                position++;
            }
            spinner.setSelection(position);
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete_todo) {
            //confirm?
            new AlertDialog.Builder(TodoActivity.this)
                    .setTitle(getString(R.string.delete_todo_dialog_title))
                    .setMessage(getString(R.string.delete_todo_dialog))
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            //delete
                            Uri uri =  Uri.withAppendedPath(MyListsContract.TodosEntry.CONTENT_URI, String.valueOf(todo.id.get()));

                            String selection = MyListsContract.TodosEntry._ID + "=?";
                            String[] arguments = new String[1];
                            arguments[0] = String.valueOf(todo.id.get());

                            handler.startDelete(1, null, uri
                                    , selection, arguments);
                            Intent intent = new Intent(TodoActivity.this, TodoListActivity.class);
                            startActivity(intent);

                        }})
                    .setNegativeButton(android.R.string.no, null).show();
        }
        return super.onOptionsItemSelected(item);
    }

    //I use the onpause method to save data to the db
    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        String [] args = new String[1];
        TodosQueryHandler handler =  new TodosQueryHandler(getContentResolver());
        //save data(existing todo)
        //read the current category
       /* Category cat = (Category)spinner.getSelectedItem();
        int catId = cat.catID.get();*/
        int listId = lists.listID.get();
        ContentValues values = new ContentValues();
        values.put(MyListsContract.TodosEntry.COLUMN_TEXT, todo.text.get());
        values.put(MyListsContract.TodosEntry.COLUMN_CATEGORY, 1);
        values.put(MyListsContract.TodosEntry.COLUMN_LIST, listId);
        values.put(MyListsContract.TodosEntry.COLUMN_DONE, todo.done.get());
        values.put(MyListsContract.TodosEntry.COLUMN_EXPIRED, todo.expired.get());
        if(todo != null && todo.id.get() != 0) {
            args[0] = String.valueOf(todo.id.get());
            handler.startUpdate(1,null,MyListsContract.TodosEntry.CONTENT_URI, values,
                    MyListsContract.TodosEntry._ID + "=?", args);
        }
        else if(todo != null && todo.id.get() == 0) {
            handler.startInsert(1,null,MyListsContract.TodosEntry.CONTENT_URI, values);
        }
    }
}

