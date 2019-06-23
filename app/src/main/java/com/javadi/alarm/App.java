package com.javadi.alarm;

import android.app.Application;
import android.content.Context;
import android.media.MediaPlayer;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class App extends Application {

    public static MediaPlayer mediaPlayer;
    public static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer=MediaPlayer.create(getApplicationContext(),R.raw.alarm);
        mContext=getApplicationContext();
    }

    public static void showNotification(){
        NotificationCompat.Builder builder=new NotificationCompat.Builder(mContext,"1");
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentTitle("Time");
        builder.setContentText("Alarm activated");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(mContext);
        notificationManagerCompat.notify(1,builder.build());
    }
}
