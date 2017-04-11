package com.russellmartin.mylistsapplication;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.russellmartin.mylistsapplication.data.MyListsContract;


public class ListAdapter extends CursorAdapter {
    public ListAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, null, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView listTextView = (TextView) view.findViewById(R.id.tvList);
        int textColumn = cursor.getColumnIndex(MyListsContract.ListEntry.COLUMN_LIST_TEXT);
        String text = cursor.getString(textColumn);
        listTextView.setText(text);

    }
}
