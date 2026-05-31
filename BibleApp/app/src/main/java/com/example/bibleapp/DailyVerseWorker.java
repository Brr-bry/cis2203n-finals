package com.example.bibleapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class DailyVerseWorker extends Worker {

    public DailyVerseWorker(
            @NonNull Context context,
            @NonNull WorkerParameters workerParams
    ) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        Context context = getApplicationContext();
        String channelId = "daily_verse_channel";

        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // ================= CHANNEL =================
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel =
                    new NotificationChannel(
                            channelId,
                            "Daily Verse",
                            NotificationManager.IMPORTANCE_DEFAULT
                    );

            channel.setDescription("Daily Bible verse notification");
            manager.createNotificationChannel(channel);
        }

        // ================= OPEN APP INTENT =================
        Intent intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT |
                        (Build.VERSION.SDK_INT >= 23 ? PendingIntent.FLAG_IMMUTABLE : 0)
        );

        // ================= NOTIFICATION =================
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle("Daily Verse")
                        .setContentText("Start your day with God’s Word 🙏")
                        .setContentIntent(pendingIntent) // 👈 IMPORTANT
                        .setAutoCancel(true);

        manager.notify(1001, builder.build());

        reschedule();

        return Result.success();
    }

    // ================= RESCHEDULE 7AM =================
    private void reschedule() {

        Calendar now = Calendar.getInstance();

        Calendar next7am = Calendar.getInstance();
        next7am.set(Calendar.HOUR_OF_DAY, 7);
        next7am.set(Calendar.MINUTE, 0);
        next7am.set(Calendar.SECOND, 0);
        next7am.set(Calendar.MILLISECOND, 0);

        // ✅ FIX: if it's already past 7AM today → schedule tomorrow
        if (now.after(next7am)) {
            next7am.add(Calendar.DAY_OF_YEAR, 1);
        }

        long delay = next7am.getTimeInMillis() - now.getTimeInMillis();

        OneTimeWorkRequest request =
                new OneTimeWorkRequest.Builder(DailyVerseWorker.class)
                        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                        .build();

        WorkManager.getInstance(getApplicationContext())
                .enqueueUniqueWork(
                        "daily_verse_once",
                        ExistingWorkPolicy.REPLACE,
                        request
                );
    }
}