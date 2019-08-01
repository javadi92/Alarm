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
            //App.mediaPlayer.start();
            //App.ringtoneAlarm.play();
            Intent service=new Intent(context, MyService.class);

            if(Build.VERSION.SDK_INT>=26){
                context.startForegroundService(service);
            }
            else {
                context.startService(service);
            }


            App.sharedPreferences.edit().putBoolean("stop_activity",true).commit();


            long[] pattern = {0, 1000, 1000, 2000, 2000, 3000, 3000, 2000, 2000};
            //App.vibrate.vibrate(pattern,0);

            Intent lockIntent = new Intent(context, StopActivity.class);
            lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(lockIntent);
        }
    }

}
