package com.javadi.alarm.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;

import com.javadi.alarm.activity.StopActivity;
import com.javadi.alarm.service.MyService;
import com.javadi.alarm.util.App;

public class MyReceiver extends BroadcastReceiver {

    public MyReceiver(){

    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        if(intent.getAction().equalsIgnoreCase("com.javadi.alarm")){

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
