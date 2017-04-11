package com.russellmartin.mylistsapplication;


import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.widget.ListView;

import com.russellmartin.mylistsapplication.model.Category;
import com.russellmartin.mylistsapplication.model.ItemList;

public class ItemListListBinder {
    @BindingAdapter("bind:items")
    public static void bindList(ListView view, ObservableArrayList<ItemList> list){
        ItemListAdapter adapter;
        if (list == null ){
            adapter = new ItemListAdapter();
        }
        else {
            adapter = new ItemListAdapter(list);
        }
        view.setAdapter(adapter);
    }
}
