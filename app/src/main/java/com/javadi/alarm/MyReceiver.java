package com.javadi.alarm;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import static android.support.v4.content.ContextCompat.getSystemService;

public class MyReceiver extends BroadcastReceiver {

    public MyReceiver(){

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equalsIgnoreCase("com.javadi.alarm")){
            //Bundle bundle=intent.getExtras();
            App.mediaPlayer.start();
            //App.showNotification();
        }
    }
}
