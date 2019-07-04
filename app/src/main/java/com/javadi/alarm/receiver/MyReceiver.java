package com.javadi.alarm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import com.javadi.alarm.activity.StopActivity;
import com.javadi.alarm.util.App;

public class MyReceiver extends BroadcastReceiver {

    public MyReceiver(){

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equalsIgnoreCase("com.javadi.alarm")){
            App.mediaPlayer.start();

            Vibrator vibrate;
            vibrate=(Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrate.vibrate(3000);

            Intent lockIntent = new Intent(context, StopActivity.class);
            lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(lockIntent);
        }
    }
}
