package com.russellmartin.mylistsapplication.model;


import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import java.io.Serializable;


public class Category implements Serializable {
    public final ObservableInt catID = new ObservableInt();
    public final ObservableField<String> description = new ObservableField<>();

    public Category() {

    }

    public Category(int i, String d){
        catID.set(i);
        description.set(d);
    }

}
