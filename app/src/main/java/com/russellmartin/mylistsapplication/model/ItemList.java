package com.russellmartin.mylistsapplication.model;


import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import java.io.Serializable;


public class ItemList implements Serializable {
    public final ObservableInt listID = new ObservableInt();
    public final ObservableField<String> list_text = new ObservableField<>();

    public ItemList() {

    }

    public ItemList(int i, String d){
        listID.set(i);
        list_text.set(d);
    }

}
