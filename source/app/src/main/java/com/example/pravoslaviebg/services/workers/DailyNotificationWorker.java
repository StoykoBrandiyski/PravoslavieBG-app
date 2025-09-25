package com.example.pravoslaviebg.services.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.pravoslaviebg.repositories.SettingsRepository;
import com.example.pravoslaviebg.services.UserNotificationManager;
import com.example.pravoslaviebg.services.NotificationManagerInterface;


public class DailyNotificationWorker extends Worker {

    private final SettingsRepository settingsRepository;
    private final NotificationManagerInterface notificationManager;

    public DailyNotificationWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);

        this.settingsRepository = new SettingsRepository(context);
        this.notificationManager = new UserNotificationManager();
    }

    @NonNull
    @Override
    public Result doWork() {
        if (!settingsRepository.isNotificationEnabled()) {
            return Result.success();
        }

       return this.notificationManager.notifyUser(getApplicationContext());
    }
}
