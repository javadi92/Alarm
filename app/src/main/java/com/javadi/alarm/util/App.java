package com.javadi.alarm.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import com.javadi.alarm.R;
import com.javadi.alarm.database.DBHelper;

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
