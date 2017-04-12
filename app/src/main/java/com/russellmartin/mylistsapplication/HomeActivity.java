package com.russellmartin.mylistsapplication;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.russellmartin.mylistsapplication.data.ListsQueryHandler;
import com.russellmartin.mylistsapplication.data.MyListsContract;
import com.russellmartin.mylistsapplication.model.ItemList;

// This Android Activity was created by modifying code from the PluralSite tutorial --> Building your First Android App with SQLite
// This Activity controls the the listView of lists that can be opened in order to display todos list items

// Created by: Katherine Martin 2/13/17

public class HomeActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{
    static final int ALL_RECORDS = -1;
    private static final int URL_LOADER = 0;
    Cursor cursor;
    ListAdapter adapter;

    private void deleteList(int id) {

        /*String[] args = {String.valueOf(id)};
        if(id == ALL_RECORDS){
            args = null;
        }
        ListsQueryHandler handler = new ListsQueryHandler(
                this.getContentResolver());
        handler.startDelete(1, null, MyListsContract.ListEntry.CONTENT_URI, MyListsContract.ListEntry._ID + " =?", args);
*/
        getContentResolver().delete(MyListsContract.ListEntry.CONTENT_URI, MyListsContract.ListEntry._ID + "=" + id, null);
    }
    private void createTestLists() {

        for (int i = 1; i < 20; i++) {
            ContentValues values = new ContentValues();
            values.put(MyListsContract.ListEntry.COLUMN_LIST_TEXT, "List #" + i);
            ListsQueryHandler handler = new ListsQueryHandler(
                    this.getContentResolver()
            );
            handler.startInsert(1, null, MyListsContract.ListEntry.CONTENT_URI, values);
        }

    }


        @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);
            getLoaderManager().initLoader(URL_LOADER, null, this);
            Toolbar toolbar = (Toolbar)  findViewById(R.id.toolbar2);
            setSupportActionBar(toolbar);
            final ListView lv = (ListView) findViewById(R.id.lvLists);
            adapter = new ListAdapter(this, cursor, false);
            lv.setAdapter(adapter);


        // Adds the layout to the application including the list display and the toolbar
        /*lv.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item,
                R.id.tvList,itemname));*/

        // Controls when the User clicks the list names in order to open lists
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
            //Info for list
                cursor = (Cursor) adapterView.getItemAtPosition(pos);
                int listID = cursor.getInt(cursor.getColumnIndex(MyListsContract.ListEntry._ID));
                String listText = cursor.getString(cursor.getColumnIndex(MyListsContract.ListEntry.COLUMN_LIST_TEXT));

                Intent intent = new Intent(HomeActivity.this, TodoListActivity.class);
                intent.putExtra("theListId", listID);
                startActivity(intent);

            }
        });

        // Controls when the User clicks the "Plus" fab in order to create lists
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ItemListActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
            case R.id.action_delete_all_lists:
                deleteList(1);
                break;
            case R.id.action_create_test_data:
                createTestLists();
                break;
        }

        return super.onOptionsItemSelected(item);

    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {MyListsContract.ListEntry.COLUMN_LIST_TEXT,
                MyListsContract.ListEntry.TABLE_NAME + "." + MyListsContract.ListEntry._ID};

        return new CursorLoader(this,
                MyListsContract.ListEntry.CONTENT_URI,
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