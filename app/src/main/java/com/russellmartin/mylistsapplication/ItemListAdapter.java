package com.russellmartin.mylistsapplication;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableInt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;

import com.russellmartin.mylistsapplication.databinding.ActivityListBinding;

import com.russellmartin.mylistsapplication.databinding.ListItemBinding;
import com.russellmartin.mylistsapplication.model.ItemList;

public class ItemListAdapter extends BaseAdapter {
    public ObservableArrayList<ItemList> list;
    private ObservableInt position = new ObservableInt();
    private LayoutInflater inflater;
    public ItemListAdapter(ObservableArrayList<ItemList> l) {
        list = l;
    }
    public ItemListAdapter() {
        list = new ObservableArrayList<ItemList>();
    }
    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }
    @Override
    public long getItemId(int position) {
        int id = list.get(position). listID.get();
        return id;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        ListItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_item, parent, false);
        binding.setList(list.get(position));
        return binding.getRoot();
    }
    //for the spinner
    public int getPosition(Spinner s) {
        return s.getSelectedItemPosition();
    }

    public int getPosition() {
        return position.get();
    }
    public void setPosition(int position) {
        this.position.set(position);
    }
}