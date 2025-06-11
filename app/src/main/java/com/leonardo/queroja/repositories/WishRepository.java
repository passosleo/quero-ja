package com.leonardo.queroja.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.leonardo.queroja.data.DBHelper;
import com.leonardo.queroja.entities.WishEntity;
import com.leonardo.queroja.enums.Priority;
import com.leonardo.queroja.enums.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            "updated_at",
            "status"
    };

    public List<WishEntity> findAll() {
        Cursor cursor = database.query(
                tableName,
                tableColumns,
                null,
                null,
                null,
                null,
                String.format("%s DESC", tableColumns[7])
        );

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
                wish.setStatus(Status.fromCode(cursor.getInt(cursor.getColumnIndexOrThrow(tableColumns[8]))));
                wishes.add(wish);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return wishes;
    }

    public Optional<WishEntity> findById(int wishId) {
        String selection = String.format("%s = ?", tableColumns[0]);
        String[] selectionArgs = { String.valueOf(wishId) };

        Cursor cursor = database.query(tableName,
                tableColumns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            WishEntity wish = new WishEntity();
            wish.setWishId(cursor.getInt(cursor.getColumnIndexOrThrow(tableColumns[0])));
            wish.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(tableColumns[1])));
            wish.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[2])));
            wish.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[3])));
            wish.setLink(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[4])));
            wish.setPriority(Priority.fromCode(cursor.getInt(cursor.getColumnIndexOrThrow(tableColumns[5]))));
            wish.setCreatedAt(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[6]))));
            wish.setUpdatedAt(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(tableColumns[7]))));
            wish.setStatus(Status.fromCode(cursor.getInt(cursor.getColumnIndexOrThrow(tableColumns[8]))));
            cursor.close();
            return Optional.of(wish);
        }

        cursor.close();
        return Optional.empty();
    }

    public List<WishEntity> findByUserId(int userId) {
        String selection = String.format("%s = ?", tableColumns[1]);
        String[] selectionArgs = { String.valueOf(userId) };

        Cursor cursor = database.query(
                tableName,
                tableColumns,
                selection,
                selectionArgs,
                null,
                null,
                String.format("%s DESC", tableColumns[7])
        );

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
                wish.setStatus(Status.fromCode(cursor.getInt(cursor.getColumnIndexOrThrow(tableColumns[8]))));
                wishes.add(wish);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return wishes;
    }

    public List<WishEntity> findByUserIdAndStatus(int userId, Status status) {
        String selection = String.format("%s = ? AND %s = ?", tableColumns[1], tableColumns[8]);
        String[] selectionArgs = { String.valueOf(userId), String.valueOf(status.getCode()) };

        Cursor cursor = database.query(
                tableName,
                tableColumns,
                selection,
                selectionArgs,
                null,
                null,
                String.format("%s DESC", tableColumns[7])
        );

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
                wish.setStatus(Status.fromCode(cursor.getInt(cursor.getColumnIndexOrThrow(tableColumns[8]))));
                wishes.add(wish);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return wishes;
    }

    public WishEntity create(WishEntity wish) {
        ContentValues values = new ContentValues();
        values.put(tableColumns[1], wish.getUserId());
        values.put(tableColumns[2], wish.getTitle());
        values.put(tableColumns[3], wish.getDescription());
        values.put(tableColumns[4], wish.getLink());
        values.put(tableColumns[5], wish.getPriority().getCode());
        values.put(tableColumns[6], wish.getCreatedAt().toString());
        values.put(tableColumns[7], LocalDateTime.now().toString());
        values.put(tableColumns[8], wish.getStatus().getCode());
        long id = database.insert(tableName, null, values);
        wish.setWishId((int) id);
        return wish;
    }

    public WishEntity update(WishEntity wish) {
        ContentValues values = new ContentValues();
        values.put(tableColumns[1], wish.getUserId());
        values.put(tableColumns[2], wish.getTitle());
        values.put(tableColumns[3], wish.getDescription());
        values.put(tableColumns[4], wish.getLink());
        values.put(tableColumns[5], wish.getPriority().getCode());
        values.put(tableColumns[6], wish.getCreatedAt().toString());
        values.put(tableColumns[7], LocalDateTime.now().toString());
        values.put(tableColumns[8], wish.getStatus().getCode());

        String whereClause = String.format("%s = ?", tableColumns[0]);
        String[] whereArgs = { String.valueOf(wish.getWishId()) };

        database.update(tableName, values, whereClause, whereArgs);
        return wish;
    }

    public void delete(WishEntity wish) {
        String whereClause = String.format("%s = ?", tableColumns[0]);
        String[] whereArgs = { String.valueOf(wish.getWishId()) };
        database.delete(tableName, whereClause, whereArgs);
    }
}
