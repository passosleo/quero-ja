package com.leonardo.queroja.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.leonardo.queroja.data.DBHelper;
import com.leonardo.queroja.entities.UserEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public UserRepository(Context context) {
        dbHelper = new DBHelper(context);
        this.open();
    }

    private void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    private void close() {
        dbHelper.close();
    }

    private static final String[] tableColumns = {
            "user_id",
            "name",
            "email",
            "password",
            "created_at",
            "updated_at"
    };

    public long save(UserEntity user) {
        ContentValues values = new ContentValues();
        values.put(tableColumns[1], user.getName());
        values.put(tableColumns[2], user.getEmail());
        values.put(tableColumns[3], user.getPassword());
        values.put(tableColumns[4], user.getCreatedAt().toString());
        values.put(tableColumns[5], user.getUpdatedAt().toString());
        return database.insert("users", null, values);
    }

    public List<UserEntity> findAll() {
        Cursor cursor = database.query("users",
                tableColumns, null, null, null, null, null);

        List<UserEntity> users = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                UserEntity user = new UserEntity();
                user.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(tableColumns[0])));
                user.setName(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[1])));
                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[2])));
                user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[3])));
                user.setCreatedAt(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[4]))));
                user.setUpdatedAt(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[5]))));
                users.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return users;
    }

    public UserEntity findByEmail(String email) {
        Cursor cursor = database.query("users",
                tableColumns, "email = ?", new String[]{email}, null, null, null);

        UserEntity user = null;

        if (cursor.moveToFirst()) {
            user = new UserEntity();
            user.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(tableColumns[0])));
            user.setName(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[1])));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[2])));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[3])));
            user.setCreatedAt(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[4]))));
            user.setUpdatedAt(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[5]))));
        }

        cursor.close();

        return user;
    }
}
