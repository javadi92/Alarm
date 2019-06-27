 package com.javadi.alarm;

import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import static android.support.v4.content.ContextCompat.getSystemService;

 public class MainActivity extends AppCompatActivity {

     Button btnSet,btnStop;
     //EditText etHour,etMinute;
     TextView tvTimeSeted;
     TimePicker timePicker;
     static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Window win= getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        sharedPreferences= MainActivity.this.getSharedPreferences("save_time",MODE_PRIVATE);

        timePicker=(TimePicker)findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);


        tvTimeSeted=(TextView)findViewById(R.id.tv_time_seted);
        getTime();
        btnSet=(Button)findViewById(R.id.btn_set);
        btnStop=(Button)findViewById(R.id.btn_stop);

        btnSet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT < 23){
                    int getHour = timePicker.getCurrentHour();
                    int getMinute = timePicker.getCurrentMinute();
                    setTime(getHour,getMinute);
                    saveTime();
                    getTime();
                } else{
                    int getHour = timePicker.getHour();
                    int getMinute = timePicker.getMinute();
                    setTime(getHour,getMinute);
                    saveTime();
                    getTime();
                }
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,MyReceiver.class);
                intent.setAction("com.javadi.alarm");
                AlarmManager alarmManager=(AlarmManager)getSystemService(getApplicationContext().ALARM_SERVICE);
                PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT );
                alarmManager.cancel(pendingIntent);
                App.mediaPlayer.stop();
                App.mediaPlayer=MediaPlayer.create(getApplicationContext(),R.raw.alarm);
                deleteTime();
            }
        });
    }

    private void setTime(int Hour,int Minute){
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,Hour);
        calendar.set(Calendar.MINUTE,Minute);
        calendar.set(Calendar.SECOND,0);
        Intent intent=new Intent(MainActivity.this,MyReceiver.class);
        intent.setAction("com.javadi.alarm");
        AlarmManager alarmManager=(AlarmManager)getSystemService(getApplicationContext().ALARM_SERVICE);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),60000,pendingIntent);
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),60000,pendingIntent);
    }


    public void saveTime(){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        if(Build.VERSION.SDK_INT < 23){
            int getHour = timePicker.getCurrentHour();
            int getMinute = timePicker.getCurrentMinute();
            editor.putInt("h",getHour);
            editor.putInt("m",getMinute);
            editor.commit();
            editor.apply();
            Toast.makeText(MainActivity.this,"ساعت تنظیم شد",Toast.LENGTH_LONG).show();
        } else{
            int getHour = timePicker.getHour();
            int getMinute = timePicker.getMinute();
            editor.putInt("h",getHour);
            editor.putInt("m",getMinute);
            editor.commit();
            editor.apply();
            Toast.makeText(MainActivity.this,"ساعت تنظیم شد",Toast.LENGTH_LONG).show();
        }
    }

     public void deleteTime(){
         SharedPreferences.Editor editor=sharedPreferences.edit();
         editor.putInt("h",00);
         editor.putInt("m",00);
         editor.commit();
         editor.apply();
         Toast.makeText(MainActivity.this,"ساعت حذف شد",Toast.LENGTH_LONG).show();
     }

    public void getTime(){
        SharedPreferences save_time=getSharedPreferences("save_time",MODE_PRIVATE);
        int hour=save_time.getInt("h",00);
        int minute=save_time.getInt("m",00);
        String time="";
        if(hour<10 && minute>10){
            time="0"+hour+" : "+minute;
        }
        else if(hour>10 && minute<10){
            time=hour+" : "+"0"+minute;
        }
        else if(hour<10 && minute<10){
            time="0"+hour+" : "+"0"+minute;
        }
        else{
            time=hour+" : "+minute;
        }
        tvTimeSeted.setText(time);
    }

}
