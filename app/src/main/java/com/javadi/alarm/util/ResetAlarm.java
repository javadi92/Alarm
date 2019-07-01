package com.javadi.alarm.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.javadi.alarm.receiver.MyReceiver;

import java.util.Calendar;

public class ResetAlarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equalsIgnoreCase("android.intent.action.BOOT_COMPLETED")){
            SharedPreferences sh=context.getSharedPreferences("save_time",Context.MODE_PRIVATE);
            String hour=sh.getString("h",null);
            String minute=sh.getString("m",null);
            if(hour!=null && minute!=null){
                int H=(int)Integer.parseInt(hour);
                int M=(int)Integer.parseInt(minute);
                Calendar calendar=Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY,H);
                calendar.set(Calendar.MINUTE,M);
                calendar.set(Calendar.SECOND,0);
                Intent intent2=new Intent(context, MyReceiver.class);
                intent2.setAction("com.javadi.alarm");
                AlarmManager alarmManager=(AlarmManager)context.getSystemService(context.ALARM_SERVICE);
                PendingIntent pendingIntent=PendingIntent.getBroadcast(context.getApplicationContext(),0,intent2,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),60000,pendingIntent);
            }
        }
    }
}
