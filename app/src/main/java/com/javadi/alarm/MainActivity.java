 package com.javadi.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

 public class MainActivity extends AppCompatActivity {

     Button btnSet,btnStop;
     EditText etHour,etMinute;
     TextView tvTimeSeted;
     static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences= MainActivity.this.getSharedPreferences("save_time",MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etHour=(EditText)findViewById(R.id.et_hour);
        etMinute=(EditText)findViewById(R.id.et_minute);
        tvTimeSeted=(TextView)findViewById(R.id.tv_time_seted);
        getTime();
        btnSet=(Button)findViewById(R.id.btn_set);
        btnStop=(Button)findViewById(R.id.btn_stop);

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime(Integer.parseInt(etHour.getText().toString()),Integer.parseInt(etMinute.getText().toString()));
                saveTime();
                getTime();
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
        editor.putString("h",etHour.getText().toString());
        editor.putString("m",etMinute.getText().toString());
        editor.commit();
        editor.apply();
        Toast.makeText(MainActivity.this,"ساعت تنظیم شد",Toast.LENGTH_LONG).show();
    }

     public void deleteTime(){
         SharedPreferences.Editor editor=sharedPreferences.edit();
         editor.putString("h","");
         editor.putString("m","");
         editor.commit();
         editor.apply();
         Toast.makeText(MainActivity.this,"ساعت حذف شد",Toast.LENGTH_LONG).show();
         tvTimeSeted.setText("");
     }

    public void getTime(){
        SharedPreferences save_time=getSharedPreferences("save_time",MODE_PRIVATE);
        String hour=save_time.getString("h","");
        String minute=save_time.getString("m","");
        String time=hour+" : "+minute;
        tvTimeSeted.setText(time);
    }
}
