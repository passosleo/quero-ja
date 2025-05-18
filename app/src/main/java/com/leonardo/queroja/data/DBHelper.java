package com.leonardo.queroja.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "queroja.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_WISHLIST_TABLE = "CREATE TABLE IF NOT EXISTS wishlist (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "title TEXT NOT NULL, " +
            "description TEXT, " +
            "link TEXT NOT NULL, " +
            "priority INTEGER NOT NULL, " +
            "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
            "updated_at DATETIM);";
    private static final String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS users (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT NOT NULL, " +
            "email TEXT NOT NULL UNIQUE, " +
            "password TEXT NOT NULL, " +
            "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
            "updated_at DATETIME);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_WISHLIST_TABLE);
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS wishlist");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }
}
