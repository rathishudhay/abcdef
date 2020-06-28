package com.sundeep.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import static androidx.work.ExistingWorkPolicy.REPLACE;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
//import android.support.annotation.NonNull;
//import android.support.v4.app.JobIntentService;

public class MyJobIntentService extends JobIntentService {

    final Handler mHandler = new Handler();
    private static final String TAG = "MyJobIntentService";
    /**
     * Unique job ID for this service.
     */
    private static final int JOB_ID = 2;

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        String NOTIFICATION_ID = "appName_notification_id";
        String NOTIFICATION_WORK = "appName_notification_work";

        Long delay=4*1000l;
        Data data = new Data.Builder()
                .putInt(NOTIFICATION_ID, 100)
                .build();



        OneTimeWorkRequest notificationWork = new OneTimeWorkRequest.Builder(NotifyWork.class).setInitialDelay(delay, MILLISECONDS).setInputData(data).build();

        WorkManager instanceWorkManager = WorkManager.getInstance(this);
        instanceWorkManager.beginUniqueWork(NOTIFICATION_WORK, REPLACE, notificationWork).enqueue();

    }

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, MyJobIntentService.class, JOB_ID, intent);
    }

}