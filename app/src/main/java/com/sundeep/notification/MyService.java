package com.sundeep.notification;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import static androidx.work.ExistingWorkPolicy.REPLACE;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class MyService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful
        String NOTIFICATION_ID = "appName_notification_id";
        String NOTIFICATION_WORK = "appName_notification_work";

        Long delay=4*1000l;
        Data data = new Data.Builder()
                .putInt(NOTIFICATION_ID, 100)
                .build();



        OneTimeWorkRequest notificationWork = new OneTimeWorkRequest.Builder(NotifyWork.class).setInitialDelay(delay, MILLISECONDS).setInputData(data).build();

        WorkManager instanceWorkManager = WorkManager.getInstance(this);
        instanceWorkManager.beginUniqueWork(NOTIFICATION_WORK, REPLACE, notificationWork).enqueue();
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }
}