package com.example.pravoslaviebg.services;

import android.content.Context;

import androidx.work.ListenableWorker;

public interface NotificationManagerInterface {

    ListenableWorker.Result notifyUser(Context context);
    void createNotificationChannel(Context context);
}
