package com.russellmartin.mylistsapplication.data;

//DatabaseHelper is a SQLite Helper class that creates the database
// Created referencing from PluralSite tutorial --> Building your First Android App with SQLite

// Created by: Katherine Martin 2/13/17

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.russellmartin.mylistsapplication.data.MyListsContract.*;

import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    //Constants for db name and version
    private static final String DATABASE_NAME = "mylists.db";
    private static final int DATABASE_VERSION = 1;


    //SQL to create tables: List, Todos, Categories
    private static final String TABLE_CATEGORIES_CREATE =
            "CREATE TABLE " + CategoriesEntry.TABLE_NAME + " (" +
                    CategoriesEntry._ID + " INTEGER PRIMARY KEY, " +
                    CategoriesEntry.COLUMN_DESCRIPTION + " TEXT " +
                    ")";
    private static final String TABLE_LISTS_CREATE =
            "CREATE TABLE " + ListEntry.TABLE_NAME + " (" +
                    ListEntry._ID + " INTEGER PRIMARY KEY, " +
                    ListEntry.COLUMN_LIST_TEXT + " TEXT " +
                    ")";
    private static final String TABLE_TODOS_CREATE =
            "CREATE TABLE " + TodosEntry.TABLE_NAME + " (" +
                    TodosEntry._ID + " INTEGER PRIMARY KEY, " +
                    TodosEntry.COLUMN_TEXT + " TEXT, " +
                    TodosEntry.COLUMN_CREATED + " TEXT default CURRENT_TIMESTAMP, " +
                    TodosEntry.COLUMN_EXPIRED + " TEXT, " +
                    TodosEntry.COLUMN_DONE + " INTEGER, " +
                    TodosEntry.COLUMN_CATEGORY + " INTEGER, " +
                    TodosEntry.COLUMN_LIST + " INTEGER, " +
                    " FOREIGN KEY(" + TodosEntry.COLUMN_CATEGORY + ") REFERENCES " + CategoriesEntry.TABLE_NAME
                    + "(" + CategoriesEntry._ID + "), " +
                    " FOREIGN KEY(" + TodosEntry.COLUMN_LIST + ") REFERENCES " + ListEntry.TABLE_NAME
                    + "(" + ListEntry._ID + ") " + ")";

    // Constructor for the DatabaseHelper, simplified
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CATEGORIES_CREATE);
        db.execSQL(TABLE_TODOS_CREATE);
        db.execSQL(TABLE_LISTS_CREATE);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TodosEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CategoriesEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ListEntry.TABLE_NAME);
        onCreate(db);

    }
}