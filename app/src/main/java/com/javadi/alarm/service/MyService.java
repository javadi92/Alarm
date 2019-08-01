package com.javadi.alarm.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.javadi.alarm.R;
import com.javadi.alarm.util.App;

public class MyService extends Service {

    public static Vibrator vibrate;
    private Uri alarmTone;
    public static Ringtone ringtoneAlarm;
    long[] pattern;

    @Override
    public void onCreate() {
        super.onCreate();
        alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtoneAlarm = RingtoneManager.getRingtone(this, alarmTone);
        vibrate=(Vibrator)this.getSystemService(Context.VIBRATOR_SERVICE);
        pattern = new long[]{0, 1000, 1000, 2000, 2000, 3000, 3000, 2000, 2000};
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        vibrate.vibrate(pattern,0);
        ringtoneAlarm.play();

        if(Build.VERSION.SDK_INT>=26){
            String NOTIFICATION_CHANNEL_ID = "com.javadi.alarm.service ";
            String channelName = "My Background Service";
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
            Notification notification = notificationBuilder.setOngoing(true)
                    .setSmallIcon(R.drawable.alarm)
                    .setContentTitle("آلارم فعال شد")
                    .setPriority(NotificationManager.IMPORTANCE_MIN)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .build();
            startForeground(2, notification);
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<8;i++){
                    App.audioManager.setStreamVolume(AudioManager.STREAM_ALARM,1,AudioManager.FLAG_PLAY_SOUND);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ringtoneAlarm.stop();
        vibrate.cancel();
    }

    class play extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }


}
