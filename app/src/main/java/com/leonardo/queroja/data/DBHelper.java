package com.leonardo.queroja.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "queroja.db";
    private static final int DATABASE_VERSION = 3;

    private static final String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS users (" +
            "user_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT NOT NULL, " +
            "email TEXT NOT NULL UNIQUE, " +
            "password TEXT NOT NULL, " +
            "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
            "updated_at DATETIME);";

    private static final String CREATE_WISHLIST_TABLE = "CREATE TABLE IF NOT EXISTS wishlist (" +
            "wish_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "user_id INTEGER NOT NULL, " +
            "title TEXT NOT NULL, " +
            "description TEXT, " +
            "link TEXT NOT NULL, " +
            "status INTEGER NOT NULL DEFAULT 0, " +
            "priority INTEGER NOT NULL, " +
            "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
            "updated_at DATETIME, " +
            "FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_WISHLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS wishlist");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }
}
