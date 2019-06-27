package com.javadi.alarm;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.view.WindowManager;

import static android.support.v4.content.ContextCompat.getSystemService;

public class MyReceiver extends BroadcastReceiver {

    public MyReceiver(){

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equalsIgnoreCase("com.javadi.alarm")){
            //Bundle bundle=intent.getExtras();
            App.mediaPlayer.start();

            Vibrator vibrate;
            vibrate=(Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrate.vibrate(5000);

            Intent lockIntent = new Intent(context, MainActivity.class);
            lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(lockIntent);
            /*PackageManager pm = context.getPackageManager();
            Intent launchIntent = pm.getLaunchIntentForPackage("com.javadi.alarm");
            //launchIntent.putExtra("some_data", "value");
            context.startActivity(launchIntent);*/


            /*NotificationCompat.Builder mBuilder =   new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_launcher) // notification icon
                    .setContentTitle("Notification!") // title for notification
                    .setContentText("Hello word") // message for notification
                    .setAutoCancel(true)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);; // clear notification after click

            mBuilder.setOngoing(true);
            Intent intent2 = new Intent(context, MyReceiver.class);
            @SuppressLint("WrongConstant") PendingIntent pi = PendingIntent.getActivity(context,0,intent,Intent.FLAG_ACTIVITY_NEW_TASK);
            mBuilder.setContentIntent(pi);
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(0, mBuilder.build());*/
        }
    }

    public interface OnAlarmsLoadedListener{
        public void onAlarmLoaded();
    }

}
