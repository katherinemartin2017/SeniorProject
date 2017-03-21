package com.russellmartin.mylistsapplication.data;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;

/**
 * Created by Katherine on 3/19/17.
 */

public class TodosQueryHandler extends AsyncQueryHandler {
    public TodosQueryHandler(ContentResolver cr) {
        super(cr);
    }
}
