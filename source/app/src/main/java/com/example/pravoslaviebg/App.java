package com.example.pravoslaviebg;

import android.app.Application;

import com.example.pravoslaviebg.services.UserNotificationManager;
import com.jakewharton.threetenabp.AndroidThreeTen;

import dagger.hilt.android.HiltAndroidApp;


public class App extends Application {

    private final UserNotificationManager notificationManager;

    public App() {
        this.notificationManager = new UserNotificationManager();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);

        this.notificationManager.createNotificationChannel(getApplicationContext());
    }
}
