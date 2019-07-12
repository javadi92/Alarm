package com.javadi.alarm.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import com.javadi.alarm.database.DBC;
import com.javadi.alarm.receiver.MyReceiver;
import java.util.Calendar;

public class ResetAlarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equalsIgnoreCase("android.intent.action.BOOT_COMPLETED")){
            Cursor cursor=App.dbHelper.getAlarms();
                if(cursor.moveToFirst()){
                    do{
                        int pend=cursor.getInt(0);
                        int H=cursor.getInt(cursor.getColumnIndex(DBC.hour));
                        int M=cursor.getInt(cursor.getColumnIndex(DBC.minute));
                        Calendar calendar=Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY,H);
                        calendar.set(Calendar.MINUTE,M);
                        calendar.set(Calendar.SECOND,0);
                        if(calendar.getTimeInMillis()< System.currentTimeMillis()){
                            calendar.add(Calendar.DATE,1);
                        }
                        Intent intent2=new Intent(context, MyReceiver.class);
                        intent2.setAction("com.javadi.alarm");
                        AlarmManager alarmManager=(AlarmManager)context.getSystemService(context.ALARM_SERVICE);
                        PendingIntent pendingIntent=PendingIntent.getBroadcast(context.getApplicationContext(),pend,intent2,PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),60000,pendingIntent);
                    }while (cursor.moveToNext());
                }
        }
    }
}
