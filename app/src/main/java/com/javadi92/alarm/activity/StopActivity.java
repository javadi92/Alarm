package com.javadi92.alarm.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import com.javadi92.alarm.database.DBC;
import com.javadi92.alarm.receiver.MyReceiver;
import com.javadi92.alarm.R;
import com.javadi92.alarm.service.MyService;
import com.javadi92.alarm.util.App;
import com.javadi92.alarm.util.SnoozShiftTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StopActivity extends AppCompatActivity {

    static int pendingId;
    Button btnStop,btnSnooz;
    ConstraintLayout cl;
    int count=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stop_alarm);


        final Window win= getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        btnStop=(Button)findViewById(R.id.btn_stop);
        btnSnooz=(Button)findViewById(R.id.btn_snooz);
        cl=(ConstraintLayout)findViewById(R.id.cl);

        int h=App.sharedPreferences.getInt("hour_trigered",-1);
        int m=App.sharedPreferences.getInt("minute_trigred",-1);

        int is_run=App.sharedPreferences.getInt("is_run",0);
        if(is_run==0){
            App.sharedPreferences.edit().putInt("is_run",1).commit();
            App.sharedPreferences.edit().putInt("h",h).commit();
            App.sharedPreferences.edit().putInt("m",m).commit();
            Cursor cursor=App.dbHelper.getAlarms();
            if(cursor.moveToFirst()){
                do{
                    if(cursor.getInt(cursor.getColumnIndex(DBC.hour))==h && cursor.getInt(cursor.getColumnIndex(DBC.minute))==m){
                        pendingId=cursor.getInt(0);
                        App.dbHelper.updateAlarm(cursor.getInt(0),h,
                                m,0);
                    }
                }while (cursor.moveToNext());
            }
            App.sharedPreferences.edit().putInt("pending_id",pendingId).commit();
        }
        else {
            pendingId=App.sharedPreferences.getInt("pending_id",0);
        }


        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"آلارم متوقف شد",Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(),pendingId+"",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(), MyReceiver.class);
                intent.setAction("com.javadi.alarm");
                AlarmManager alarmManager=(AlarmManager)getSystemService(getApplicationContext().ALARM_SERVICE);
                PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),pendingId,intent,PendingIntent.FLAG_UPDATE_CURRENT );
                alarmManager.cancel(pendingIntent);

                Intent stopservice=new Intent(StopActivity.this, MyService.class);
                stopService(stopservice);

                Intent alarmChanged = new Intent("android.intent.action.ALARM_CHANGED");
                alarmChanged.putExtra("alarmSet", false/*enabled*/);
                getApplicationContext().sendBroadcast(alarmChanged);

                App.sharedPreferences.edit().putInt("is_run",0).commit();
                App.sharedPreferences.edit().putInt("pending_id",0).commit();

                App.sharedPreferences.edit().putBoolean("stop_activity",false).commit();

                /*int end=App.sharedPreferences.getInt("volume",0);
                for(int i=0;i<end ;i++){
                    App.audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
                }*/
                //App.sharedPreferences.edit().putInt("volume",0).commit();

                finishAffinity();
            }
        });

        btnSnooz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar=Calendar.getInstance();

                Intent intent=new Intent(getApplicationContext(), MyReceiver.class);
                intent.setAction("com.javadi.alarm");
                AlarmManager alarmManager=(AlarmManager)getSystemService(getApplicationContext().ALARM_SERVICE);
                PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),pendingId,intent,PendingIntent.FLAG_UPDATE_CURRENT );
                alarmManager.cancel(pendingIntent);

                Intent stopservice=new Intent(StopActivity.this, MyService.class);
                stopService(stopservice);

                App.sharedPreferences.edit().putInt("is_run",0).commit();
                App.sharedPreferences.edit().putInt("pending_id",0).commit();

                App.sharedPreferences.edit().putBoolean("stop_activity",false).commit();

                /*int end=App.sharedPreferences.getInt("volume",0);
                for(int i=0;i<end ;i++){
                    App.audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
                }
                App.sharedPreferences.edit().putInt("volume",0).commit();*/

                List<Integer> list=new ArrayList<>();
                SnoozShiftTime snoozShiftTime=new SnoozShiftTime(calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE));
                list=snoozShiftTime.finalTimes(5);
                int snooz_hour=list.get(0);
                int snooz_minute=list.get(1);

                if(!checkAlarmExists(snooz_hour,snooz_minute)){

                    setTime(snooz_hour,snooz_minute,pendingId+1);
                    App.dbHelper.insertAlarm(snooz_hour,snooz_minute);
                }

                finishAffinity();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (count<10000){

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(count%2==0){
                                cl.setBackgroundColor(Color.parseColor("#6DAAF8"));
                            }
                            else{
                                cl.setBackgroundColor(Color.RED);
                            }
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    count++;
                }
            }
        }).start();

    }

    @Override
    public void onBackPressed() {

    }

    private void setTime(int Hour,int Minute,int id){
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,Hour);
        calendar.set(Calendar.MINUTE,Minute);
        calendar.set(Calendar.SECOND,0);
        calendar.add(Calendar.MILLISECOND,0);
        if(calendar.getTimeInMillis()< System.currentTimeMillis()){
            calendar.add(Calendar.DATE,1);
        }
        Intent intent=new Intent(getApplicationContext(),MyReceiver.class);
        intent.setAction("com.javadi.alarm");
        AlarmManager alarmManager=(AlarmManager)getSystemService(getApplicationContext().ALARM_SERVICE);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        //alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),60000,pendingIntent);
        if(Build.VERSION.SDK_INT>23){
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        }
        else{
            alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        }
        if(Build.VERSION.SDK_INT<22){
            Intent alarmChanged = new Intent("android.intent.action.ALARM_CHANGED");
            alarmChanged.putExtra("alarmSet", true);
            getApplicationContext().sendBroadcast(alarmChanged);
            //alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        }
        else{
            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(),pendingIntent),pendingIntent);
        }

    }

    private boolean checkAlarmExists(int hour,int minute){
        Cursor cursor=App.dbHelper.getAlarms();
        if(cursor.moveToFirst()){
            do{
                if(cursor.getInt(cursor.getColumnIndex(DBC.hour))==hour && cursor.getInt(cursor.getColumnIndex(DBC.minute))==minute){
                    return true;
                }
            }while (cursor.moveToNext());
        }
        return false;
    }

}
