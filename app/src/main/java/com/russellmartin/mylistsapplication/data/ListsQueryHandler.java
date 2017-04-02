package com.russellmartin.mylistsapplication.data;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;

/**
 * Created by Katherine on 4/2/17.
 */

public class ListsQueryHandler extends AsyncQueryHandler {
    public ListsQueryHandler(ContentResolver cr) {
        super(cr);
    }
}
