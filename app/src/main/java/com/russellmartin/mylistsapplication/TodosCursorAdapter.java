package com.russellmartin.mylistsapplication;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.russellmartin.mylistsapplication.data.MyListsContract;


public class TodosCursorAdapter extends CursorAdapter {
    public TodosCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.todo_list_item, null, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView todoTextView = (TextView) view.findViewById(R.id.tvNote);
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.Checkbox);
        boolean todoStatus = cursor.getColumnIndex(MyListsContract.TodosEntry.COLUM_DONE);
        int textColumn = cursor.getColumnIndex(MyListsContract.TodosEntry.COLUMN_TEXT);
        String text = cursor.getString(textColumn);
        todoTextView.setText(text);
        checkBox.setChecked(todoStatus);
        

    }
}
