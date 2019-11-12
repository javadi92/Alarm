package com.javadi92.alarm.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;

import com.javadi92.alarm.database.DBHelper;

public class App extends Application {

    //public static MediaPlayer mediaPlayer;
    public static Context mContext;
    public static DBHelper dbHelper;
    public static SharedPreferences sharedPreferences;
    //public static Vibrator vibrate;
    //private Uri alarmTone;
    //public static Ringtone ringtoneAlarm;
    public static AudioManager audioManager;

    @Override
    public void onCreate() {
        super.onCreate();
        //alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        //ringtoneAlarm = RingtoneManager.getRingtone(getApplicationContext(), alarmTone);
        //mediaPlayer=MediaPlayer.create(getApplicationContext(), R.raw.alarm2);
        mContext=getApplicationContext();
        dbHelper=DBHelper.getInstance(mContext);
        audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        sharedPreferences=mContext.getSharedPreferences("pending_id",MODE_PRIVATE);
        //vibrate=(Vibrator)mContext.getSystemService(Context.VIBRATOR_SERVICE);
    }
}
