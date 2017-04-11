package com.russellmartin.mylistsapplication;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.russellmartin.mylistsapplication.data.MyListsContract;
import com.russellmartin.mylistsapplication.data.TodosQueryHandler;
import com.russellmartin.mylistsapplication.databinding.ActivityListBinding;
import com.russellmartin.mylistsapplication.model.ItemList;
import com.russellmartin.mylistsapplication.model.ItemListList;

public class ItemListActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {
    protected ItemList l;
    protected ItemListList ls;
    private static final int URL_LOADER = 0;
    ObservableArrayList<ItemList> list;
    Cursor cursor;
    ItemListAdapter adapter;
    ActivityListBinding binding;
    TodosQueryHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getLayoutInflater();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list);
        getLoaderManager().initLoader(URL_LOADER, null, this);
        handler = new TodosQueryHandler(getContentResolver());

        ListView myList = (ListView) findViewById(R.id.lvtheLists);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                l = ls.Lists.get(position);
                binding.setList(l);
            }
        });
//New button will add a new line on the list
        final Button btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                l = new ItemList();
                binding.setList(l);
            }
        });
//delete button will delete an item from the list
        final Button btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new AlertDialog.Builder(ItemListActivity.this)
                        .setTitle(getString(R.string.delete_list_dialog))
                        .setMessage(getString(R.string.delete_list_dialog_title))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //delete
                                ls.Lists.remove(l);
                                Toast.makeText(ItemListActivity.this, "List deleted", Toast.LENGTH_SHORT).show();
                                String[] args = new String[1];
                                Uri uri = Uri.withAppendedPath(MyListsContract.CategoriesEntry.CONTENT_URI, String.valueOf(l.listID.get()));
                                handler.startDelete(1, null, uri
                                        , null, null);
                                l = null;
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();

            }
        });
//save button
        final Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (l != null && l.listID.get() != 0) {
                    //update existing category
                    Log.d("Save Click", "update");
                    String[] args = new String[1];
                    ContentValues values = new ContentValues();
                    values.put(MyListsContract.ListEntry.COLUMN_LIST_TEXT,
                            l.list_text.get());
                    args[0] = String.valueOf(l.listID.get());
                    handler.startUpdate(1, null, MyListsContract.ListEntry.CONTENT_URI,
                            values, MyListsContract.ListEntry._ID + "=?", args);
                    Toast.makeText(ItemListActivity.this, "List saved", Toast.LENGTH_SHORT).show();
                } else if (l != null && l.listID.get() == 0) {
                    //add new category
                    ContentValues values = new ContentValues();
                    values.put(MyListsContract.ListEntry.COLUMN_LIST_TEXT,
                            l.list_text.get());
                    handler.startInsert(1, null, MyListsContract.ListEntry.CONTENT_URI,
                            values);
                    Toast.makeText(ItemListActivity.this, "List created", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public void onResume(){
        getLoaderManager().restartLoader(URL_LOADER, null, this);
        super.onResume();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {MyListsContract.ListEntry.TABLE_NAME
                + "." + MyListsContract.ListEntry._ID,
                MyListsContract.ListEntry.COLUMN_LIST_TEXT};
        return new CursorLoader(
                this,
                MyListsContract.ListEntry.CONTENT_URI,
                projection,
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        final ListView lv=(ListView) findViewById(R.id.lvtheLists);
        list = new ObservableArrayList<>();
        int i=0;
        //fills the observablelist of categories
        // Move cursor before first so we can still iterate after config change
        data.moveToPosition(-1);
        while (data.moveToNext())
        {
            list.add(i, new ItemList(
                    data.getInt(0),
                    data.getString(1)
            ));
            i++;
        }
        adapter = new ItemListAdapter(list);
        lv.setAdapter(adapter);
        //set bindings
        //classes
        l = new ItemList();
        ls = new ItemListList(list);
        binding.setLists(ls);
        binding.setList(l);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.list = null;
    }
}
