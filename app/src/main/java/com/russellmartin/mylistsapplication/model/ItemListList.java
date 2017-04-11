package com.russellmartin.mylistsapplication.model;

import android.content.ClipData;
import android.databinding.ObservableArrayList;

import java.io.Serializable;

/**
 * Created by Katherine on 3/20/17.
 */

public class ItemListList implements Serializable {
    public ObservableArrayList<ItemList> Lists;

    public ItemListList() {

        Lists = new ObservableArrayList<>();
    }

    public ItemListList(ObservableArrayList<ItemList> listList){
        Lists = listList;
    }
}
