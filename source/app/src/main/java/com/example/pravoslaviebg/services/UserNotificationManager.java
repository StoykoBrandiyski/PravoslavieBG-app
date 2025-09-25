package com.example.pravoslaviebg.services;

import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ListenableWorker;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.pravoslaviebg.R;
import com.example.pravoslaviebg.models.quote.QuoteDaily;
import com.example.pravoslaviebg.models.saint.SaintDailyMind;
import com.example.pravoslaviebg.repositories.QuoteRepository;
import com.example.pravoslaviebg.repositories.SaintRepository;
import com.example.pravoslaviebg.services.workers.DailyNotificationWorker;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Response;

public class UserNotificationManager implements NotificationManagerInterface{

    private final SaintRepository saintRepository;
    private final QuoteRepository quoteRepository;

    public UserNotificationManager() {
        this.saintRepository = new SaintRepository();
        this.quoteRepository = new QuoteRepository();
    }

    @Override
    public ListenableWorker.Result notifyUser(Context context) {
        StringBuilder stringBuilder = new StringBuilder();

        Response<List<SaintDailyMind>> response = this.saintRepository.getSaintDailyNotificationSync();
        if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
            SaintDailyMind item = response.body().get(0);
            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM"));
            stringBuilder.append(date).append(" - ").append(item.getSaintName());
            stringBuilder.append("\r\n");
            stringBuilder.append(item.getDailyMindContent());

            sendNotification(context, "Дневна мисъл", stringBuilder.toString());
            return ListenableWorker.Result.success();
        }
        if (response.isSuccessful() && response.body() != null) {
            Response<QuoteDaily> quoteResponse = this.quoteRepository.getSaintDailyNotificationSync();
            if (quoteResponse.isSuccessful() && quoteResponse.body() != null) {
                QuoteDaily quoteDaily = quoteResponse.body();
                stringBuilder.append(quoteDaily.getChapter());
                stringBuilder.append("\r\n");
                stringBuilder.append(quoteDaily.getContent());

                sendNotification(context, "Цитат от Библията", stringBuilder.toString());
                return ListenableWorker.Result.success();
            }
        }
        return ListenableWorker.Result.retry();
    }

    @Override
    public void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "daily_mind_channel",
                    "Daily Mind Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Shows the daily mind notification from the saints");

            NotificationManager manager = getSystemService(context, NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
                //scheduleDailyMindWork(context);
            }
        }
    }

    private void sendNotification(Context context, String headerTitle, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "daily_mind_channel")
                .setSmallIcon(R.drawable.ic_menu_camera)
                .setContentTitle(headerTitle)
                .setContentText(content)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED) {
            NotificationManagerCompat.from(context).notify(1, builder.build());
        } else {
            Log.w("DailyMindWorker", "Notification permission not granted.");
        }
    }

    private void scheduleDailyMindWork(Context context) {
        Calendar now = Calendar.getInstance();
        Calendar daily9am = Calendar.getInstance();
        daily9am.set(Calendar.HOUR_OF_DAY, 9);
        daily9am.set(Calendar.MINUTE, 0);
        daily9am.set(Calendar.SECOND, 0);

        if (now.after(daily9am)) {
            daily9am.add(Calendar.DAY_OF_MONTH, 1);
        }

        long delay = daily9am.getTimeInMillis() - System.currentTimeMillis();

        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(
                DailyNotificationWorker.class,
                24, TimeUnit.HOURS
        )
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .addTag("daily_mind")
                .build();

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "daily_mind_work",
                ExistingPeriodicWorkPolicy.REPLACE,
                workRequest
        );
    }
}
