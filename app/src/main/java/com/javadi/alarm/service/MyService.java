package com.javadi.alarm.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;

import com.javadi.alarm.util.App;

public class MyService extends Service {

    public static Vibrator vibrate;
    private Uri alarmTone;
    public static Ringtone ringtoneAlarm;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtoneAlarm = RingtoneManager.getRingtone(this, alarmTone);
        vibrate=(Vibrator)this.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 1000, 1000, 2000, 2000, 3000, 3000, 2000, 2000};
        vibrate.vibrate(pattern,0);
        ringtoneAlarm.play();
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
}
