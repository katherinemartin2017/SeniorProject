package com.russellmartin.mylistsapplication.model;


import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import java.io.Serializable;


public class Todo implements Serializable {
    public ObservableInt id = new ObservableInt();
    public ObservableField<String> text = new ObservableField<>();
    public ObservableField<String> created = new ObservableField<>();
    public ObservableField<String> expired = new ObservableField<>();
    public ObservableField<String> category = new ObservableField<>();
    public ObservableField<String> list = new ObservableField<>();
    public ObservableBoolean done = new ObservableBoolean();

    public Todo (int id, String text, String created, String expired, boolean done, String category, String list ){
        this.id.set(id);
        this.text.set(text);
        this.created.set(created);
        this.expired.set(expired);
        this.done.set(done);
        this.category.set(category);
        this.list.set(list);
    }
}
