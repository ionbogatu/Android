package com.example.myshop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(@Nullable Context context) {
        super(context, "preferences", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE preferences (ID INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(255) NOT NULL, value VARCHAR(255) NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS preferences");
        onCreate(db);
    }

    public boolean saveData(String name, String value) {
        SQLiteDatabase dbRead = this.getReadableDatabase();
        SQLiteDatabase dbWrite = this.getWritableDatabase();

        Cursor c = dbRead.rawQuery("SELECT id FROM preferences WHERE name = '" + name.trim() + "'", null);
        if (c.getCount() > 0) { // update
            ContentValues contentValues = new ContentValues();
            contentValues.put("value", value);

            if (dbWrite.update("preferences", contentValues, "name = ?", new String[] {name}) == -1) {
                return false;
            }
        } else { // insert
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", name);
            contentValues.put("value", value);

            if (dbWrite.insert("preferences", null, contentValues) == -1) {
                return false;
            }
        }

        c.close();
        dbRead.close();
        dbWrite.close();

        return true;
    }

    public String retrieveData(String name) {
        SQLiteDatabase dbRead = this.getReadableDatabase();

        Cursor c = dbRead.rawQuery("SELECT value FROM preferences WHERE name = '" + name.trim() + "'", null);
        if (c.getCount() > 0) { // update
            while (c.moveToNext()) {
                return c.getString(0);
            }
        }

        c.close();
        dbRead.close();

        return "";
    }
}
