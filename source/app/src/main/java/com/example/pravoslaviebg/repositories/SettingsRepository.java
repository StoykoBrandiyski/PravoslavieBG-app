package com.example.pravoslaviebg.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pravoslaviebg.helpers.SettingsDbHelper;

public class SettingsRepository {
    private final SQLiteDatabase db;

    public SettingsRepository(Context context) {
        SettingsDbHelper helper = new SettingsDbHelper(context);
        db = helper.getWritableDatabase();
    }

    public void setNotificationEnabled(boolean enabled) {
        ContentValues values = new ContentValues();
        values.put("setting_key", "daily_notifications_enabled");
        values.put("value", enabled ? 1 : 0);
        db.insertWithOnConflict("settings", null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public boolean isNotificationEnabled() {
        Cursor cursor = db.rawQuery("SELECT value FROM settings WHERE setting_key = ?", new String[]{"daily_notifications_enabled"});
        if (cursor.moveToFirst()) {
            return cursor.getInt(0) == 1;
        }
        cursor.close();
        return false; // Default to false if not set
    }
}
