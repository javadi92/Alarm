package com.javadi.alarm.service;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import com.javadi.alarm.R;
import com.javadi.alarm.model.Alarm;
import com.javadi.alarm.util.App;

public class MyService extends Service {

    public static Vibrator vibrate;
    private Uri alarmTone;
    public static Ringtone ringtoneAlarm;
    AudioManager am;
    MediaPlayer mediaPlayer;
    long[] pattern;

    @Override
    public void onCreate() {
        super.onCreate();
        alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtoneAlarm = RingtoneManager.getRingtone(this, alarmTone);
        am= (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        am.adjustVolume(AudioManager.ADJUST_UNMUTE, AudioManager.FLAG_PLAY_SOUND);
        mediaPlayer=MediaPlayer.create(getApplicationContext(),alarmTone);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //vibrate=(Vibrator)this.getSystemService(Context.VIBRATOR_SERVICE);
        pattern = new long[]{0, 1000, 1000, 2000, 2000, 3000, 3000, 2000, 2000};
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //vibrate.vibrate(pattern,0);
        //ringtoneAlarm.play();

        mediaPlayer.setLooping(true);
        mediaPlayer.start();


        /*if(am.getRingerMode()==AudioManager.RINGER_MODE_SILENT){
            am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            App.sharedPreferences.edit().putString("ringtone","silent").commit();
        }

        else if(am.getRingerMode()==AudioManager.RINGER_MODE_VIBRATE){
            am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            App.sharedPreferences.edit().putString("ringtone","vibrate").commit();
        }*/


        if(Build.VERSION.SDK_INT>=26){
            String NOTIFICATION_CHANNEL_ID = "com.javadi.alarm.service";
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

        int volume=am.getStreamVolume(AudioManager.STREAM_MUSIC);
        App.sharedPreferences.edit().putInt("volume",volume).commit();

        for(int j=volume;j>=0;j--){
            App.audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
        }


        //increase volume of phone gradually
        new Thread(new Runnable() {
            @Override
            public void run() {

                for(int i=0;i<16;i++){
                    am.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);

                    try {
                        Thread.sleep(4000);
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
        //ringtoneAlarm.stop();
        mediaPlayer.stop();
        //vibrate.cancel();
    }
}
