package com.javadi92.alarm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.javadi92.alarm.activity.StopActivity;
import com.javadi92.alarm.service.MyService;
import com.javadi92.alarm.util.App;

import java.util.Calendar;

public class MyReceiver extends BroadcastReceiver {

    public MyReceiver(){

    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        if(intent.getAction().equalsIgnoreCase("com.javadi.alarm")){

            Calendar calendar=Calendar.getInstance();
            int hh=calendar.get(Calendar.HOUR_OF_DAY);
            int mm=calendar.get(Calendar.MINUTE);

            App.sharedPreferences.edit().putInt("hour_trigered", hh).commit();
            App.sharedPreferences.edit().putInt("minute_trigred",mm).commit();

            Intent service=new Intent(context, MyService.class);

            //start service to play music and vibrate device
            if(Build.VERSION.SDK_INT>=26){
                context.startForegroundService(service);
            }
            else {
                context.startService(service);
            }

            //show stop activity
            Intent lockIntent = new Intent(context, StopActivity.class);
            lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(lockIntent);
        }
    }

}
