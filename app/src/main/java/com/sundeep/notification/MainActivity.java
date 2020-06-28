package com.sundeep.notification;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static androidx.work.ExistingWorkPolicy.APPEND;
import static androidx.work.ExistingWorkPolicy.REPLACE;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class MainActivity extends AppCompatActivity {

    String NOTIFICATION_ID = "appName_notification_id";
    String NOTIFICATION_WORK = "appName_notification_work";
    String MyPREFERENCES="RADO";

    Button bt;
    String CHANNEL_ID="uniqueId";

    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    Intent alarmIntent;

    IntentFilter intentFilter;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button=(Button)findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int month=6;
                int date=28;
                int hours=21;
                int mins=8;
                boolean[] days={true,true,true,true,true,true,true};


                Calendar notifyTime=createCalenderObject(month,date,hours,mins);
                long delay=notifyTime.getTimeInMillis()-Calendar.getInstance().getTimeInMillis();

                Data data = new Data.Builder()
                        .putInt(NOTIFICATION_ID, 100)
                        .build();
//
//        days=null;
                scheduleNotification(delay,data,days);

            }
        });

    }


    Calendar createCalenderObject(int month,int date,int hours,int mins){
        Calendar notifyTime = Calendar.getInstance();

        notifyTime.set(Calendar.MONTH, month-1);
        notifyTime.set(Calendar.DATE,date);
        if(hours<12){
            notifyTime.set(Calendar.HOUR,hours);
            notifyTime.set(Calendar.AM_PM,Calendar.AM);
        }else{
            notifyTime.set(Calendar.HOUR,hours-12);
            notifyTime.set(Calendar.AM_PM,Calendar.PM);
        }
//        notifyTime.set(Calendar.HOUR,hours);
        notifyTime.set(Calendar.MINUTE,mins);
        notifyTime.set(Calendar.SECOND,0);
//        // Displaying the modified result
//        System.out.println("Altered Month is: "
//                + notifyTime);
//        System.out.println("Altered Month is: "
//                + Calendar.getInstance());
//        System.out.println("Altered Month is: "
//                + notifyTime.getTimeInMillis());
//
//        System.out.println("Altered Month is: "
//                + Calendar.getInstance().getTimeInMillis());
//        System.out.println("Altered Month is: "
//                + ((notifyTime.getTimeInMillis()-Calendar.getInstance().getTimeInMillis())/(1000*60)));
//
//        System.out.println("Altered Month is: "
//                + notifyTime.get(Calendar.DAY_OF_WEEK));

        return notifyTime;
    }

    public static Calendar toCalendar(Date date){
        Calendar cal=Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }


    private void scheduleNotification(Long delay, Data data,boolean[] days) {

//        OneTimeWorkRequest notificationWork = new OneTimeWorkRequest.Builder(NotifyWork.class).setInitialDelay(delay, MILLISECONDS).setInputData(data).build();

        if(days==null){
            OneTimeWorkRequest notificationWork = new OneTimeWorkRequest.Builder(NotifyWork.class).setInitialDelay(delay, MILLISECONDS).setInputData(data).build();
            String workId=notificationWork.getId().toString();
            System.out.println("inside1:"+workId);
            SharedPreferences sharedpreferences = App.getAppContext().getSharedPreferences(workId, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("id", notificationWork.getId().toString());
            editor.commit();
            WorkManager instanceWorkManager = WorkManager.getInstance(this);
            instanceWorkManager.beginUniqueWork(NOTIFICATION_WORK, APPEND, notificationWork).enqueue();
            System.out.println("inside:"+1);
        }
        else{

            PeriodicWorkRequest notificationWork = new PeriodicWorkRequest.Builder(NotifyWork.class,16, TimeUnit.MINUTES).setInitialDelay(delay, MILLISECONDS).setInputData(data).build();


            String workId=notificationWork.getId().toString();

            System.out.println("inside:"+2);
            String workDaysString=getWorkDaysString(days);
            System.out.println("inside:"+workId);
            SharedPreferences sharedpreferences = App.getAppContext().getSharedPreferences(workId, Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.putString("id", notificationWork.getId().toString());
            editor.putString("days",workDaysString);
            editor.commit();


            WorkManager instanceWorkManager = WorkManager.getInstance(this);
            instanceWorkManager.enqueueUniquePeriodicWork(NOTIFICATION_WORK,ExistingPeriodicWorkPolicy.REPLACE,notificationWork);


//            System.out.println("sp:"+sharedpreferences.getString("id",null));
        }









        //System.out.println("ID1:"+notificationWork.getId());


//Job intent service
//        Intent mIntent = new Intent(this, MyJobIntentService.class);
//        mIntent.putExtra("maxCountValue", 1000);
//        MyJobIntentService.enqueueWork(this, mIntent);

//Normal service
//        Intent i= new Intent(MainActivity.this, MyService.class);
//        i.putExtra("KEY1", "Value to be used by the service");
//        startService(i);
    }

    String getWorkDaysString(boolean[] days){
        String daysString="";
        for(int i=0;i<days.length;i++){
            if(days[i]){
                daysString=daysString+(i+1);
            }
        }
        return daysString;
    }
}
