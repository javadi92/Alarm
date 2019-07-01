package com.javadi.alarm.util;

import android.app.Application;
import android.content.Context;
import android.media.MediaPlayer;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.javadi.alarm.R;
import com.javadi.alarm.database.DBHelper;

public class App extends Application {

    public static MediaPlayer mediaPlayer;
    public static Context mContext;
    public static DBHelper dbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer=MediaPlayer.create(getApplicationContext(), R.raw.alarm2);
        mContext=getApplicationContext();
        dbHelper=DBHelper.getInstance(mContext);
    }
}
