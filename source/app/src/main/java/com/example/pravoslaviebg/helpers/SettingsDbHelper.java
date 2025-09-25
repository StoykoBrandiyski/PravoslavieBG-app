package com.example.pravoslaviebg.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SettingsDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "user_settings.db";
    private static final int DB_VERSION = 1;

    public SettingsDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE settings ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "setting_key TEXT UNIQUE, "
                + "value INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS settings");
        onCreate(db);
    }
}
