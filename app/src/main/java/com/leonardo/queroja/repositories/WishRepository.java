package com.leonardo.queroja.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.leonardo.queroja.data.DBHelper;
import com.leonardo.queroja.entities.WishEntity;
import com.leonardo.queroja.enums.Priority;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class WishRepository {

    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public WishRepository(Context context) {
        dbHelper = new DBHelper(context);
        this.open();
    }

    private void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    private void close() {
        dbHelper.close();
    }

    private static final String tableName = "wishlist";

    private static final String[] tableColumns = {
            "wish_id",
            "user_id",
            "title",
            "description",
            "link",
            "priority",
            "created_at",
            "updated_at"
    };

    public WishEntity save(WishEntity user) {
        ContentValues values = new ContentValues();
        values.put(tableColumns[1], user.getUserId());
        values.put(tableColumns[2], user.getTitle());
        values.put(tableColumns[3], user.getDescription());
        values.put(tableColumns[4], user.getLink());
        values.put(tableColumns[5], user.getPriority().getCode());
        values.put(tableColumns[6], user.getCreatedAt().toString());
        values.put(tableColumns[7], user.getUpdatedAt().toString());
        long id = database.insert(tableName, null, values);
        user.setWishId((int) id);
        return user;
    }

    public List<WishEntity> findAll() {
        Cursor cursor = database.query(tableName,
                tableColumns, null, null, null, null, null);

        List<WishEntity> wishes = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                WishEntity wish = new WishEntity();
                wish.setWishId(cursor.getInt(cursor.getColumnIndexOrThrow(tableColumns[0])));
                wish.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(tableColumns[1])));
                wish.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[2])));
                wish.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[3])));
                wish.setLink(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[4])));
                wish.setPriority(Priority.fromCode(cursor.getInt(cursor.getColumnIndexOrThrow(tableColumns[5]))));
                wish.setCreatedAt(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[6]))));
                wish.setUpdatedAt(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[7]))));
                wishes.add(wish);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return wishes;
    }

    public List<WishEntity> findByUserId(int userId) {
        String selection = String.format("%s = ?", tableColumns[1]);
        String[] selectionArgs = { String.valueOf(userId) };

        Cursor cursor = database.query(tableName,
                tableColumns, selection, selectionArgs, null, null, null);

        List<WishEntity> wishes = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                WishEntity wish = new WishEntity();
                wish.setWishId(cursor.getInt(cursor.getColumnIndexOrThrow(tableColumns[0])));
                wish.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(tableColumns[1])));
                wish.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[2])));
                wish.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[3])));
                wish.setLink(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[4])));
                wish.setPriority(Priority.fromCode(cursor.getInt(cursor.getColumnIndexOrThrow(tableColumns[5]))));
                wish.setCreatedAt(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[6]))));
                wish.setUpdatedAt(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[7]))));
                wishes.add(wish);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return wishes;
    }

    public void delete(WishEntity wish) {
        String whereClause = String.format("%s = ?", tableColumns[0]);
        String[] whereArgs = { String.valueOf(wish.getWishId()) };
        database.delete(tableName, whereClause, whereArgs);
    }
}
