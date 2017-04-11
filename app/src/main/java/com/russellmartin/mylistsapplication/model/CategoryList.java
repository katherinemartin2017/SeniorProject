package com.russellmartin.mylistsapplication.model;

import android.content.ClipData;
import android.databinding.ObservableArrayList;

import java.io.Serializable;

/**
 * Created by Katherine on 3/20/17.
 */

public class CategoryList implements Serializable {
    public ObservableArrayList<Category> CatList;

    public CategoryList() {
        CatList = new ObservableArrayList<>();
    }

    public CategoryList(ObservableArrayList<Category> catList){
        CatList = catList;
    }
}
