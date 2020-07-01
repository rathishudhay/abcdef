package com.sundeep.notification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;

import java.net.URI;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.graphics.Color.RED;
import static android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION;
import static android.media.AudioAttributes.USAGE_NOTIFICATION_RINGTONE;
import static android.media.RingtoneManager.TYPE_NOTIFICATION;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.O;
import static androidx.core.app.NotificationCompat.DEFAULT_ALL;
import static androidx.core.app.NotificationCompat.PRIORITY_MAX;
import static androidx.core.app.NotificationCompat.getShortcutId;
import static androidx.core.app.NotificationManagerCompat.IMPORTANCE_HIGH;
import static androidx.work.ExistingWorkPolicy.APPEND;
import static androidx.work.ExistingWorkPolicy.REPLACE;
import static androidx.work.ListenableWorker.Result.success;
import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

public class NotifyWork extends Worker {

    String NOTIFICATION_ID = "appName_notification_id";
    String NOTIFICATION_WORK = "appName_notification_work";
    String NOTIFICATION_NAME = "appName";
    String NOTIFICATION_CHANNEL = "appName_channel_01";

    public NotifyWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }


    @NonNull
    @Override
    public Result doWork() {
        int id = ((Long)(getInputData().getLong(NOTIFICATION_ID, 0))).intValue();
        System.out.println("doWork");
        String workId=getId().toString();
        SharedPreferences sh = App.getAppContext().getSharedPreferences(workId, Context.MODE_PRIVATE);
        System.out.println("ID123:"+sh.getString("id",null)+" :"+workId);
        System.out.println("days:"+sh.getString("days",null));

        System.out.println(((String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)))));

        String daysString=sh.getString("days",null);



        if(daysString==null){
            System.out.println("inside:"+3);
            sendNotification(id);
        }else if(daysString.contains((String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_WEEK))))){
            System.out.println("inside:"+4);
            sendNotification(id);
        }else{
            System.out.println("inside:"+5);
        }


        System.out.println("ID:"+getId());

        return success();

    }


    void sendNotification(int id){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.flags = FLAG_ACTIVITY_NEW_TASK || FLAG_ACTIVITY_CLEAR_TASK;
        intent.putExtra(NOTIFICATION_ID, id);


        NotificationManager notificationManager =(NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);


//        Bitmap bitmap = getApplicationContext().vectorToBitmap(R.drawable.ic_schedule_black_24dp)
//        val titleNotification = applicationContext.getString(R.string.notification_title)
//        val subtitleNotification = applicationContext.getString(R.string.notification_subtitle)
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("My Notification").setContentText("Hello world")
                .setPriority(PRIORITY_MAX)
                .setDefaults(DEFAULT_ALL).setContentIntent(pendingIntent).setAutoCancel(true);

//        notification.priority = PRIORITY_MAX

        if (SDK_INT >= O) {
            notification.setChannelId(NOTIFICATION_CHANNEL);

            Uri ringtoneManager = RingtoneManager.getDefaultUri(TYPE_NOTIFICATION);
            AudioAttributes audioAttributes =new AudioAttributes.Builder().setUsage(USAGE_NOTIFICATION_RINGTONE)
                    .setContentType(CONTENT_TYPE_SONIFICATION).build();

            @SuppressLint("WrongConstant") NotificationChannel channel =new NotificationChannel(NOTIFICATION_CHANNEL, NOTIFICATION_NAME, IMPORTANCE_HIGH);

            channel.enableLights(true);
            channel.setLightColor(RED);
            channel.enableVibration(true);
//            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            channel.setSound(ringtoneManager, audioAttributes);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(id, notification.build());

    }
}
