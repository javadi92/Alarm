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

            long[] pattern = {0, 1000, 1000, 2000, 2000, 3000, 3000, 2000, 2000};
            App.vibrate.vibrate(pattern,-1);

            Intent lockIntent = new Intent(context, StopActivity.class);
            lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(lockIntent);
        }
    }
}
