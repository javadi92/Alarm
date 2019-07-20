 package com.javadi.alarm.activity;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.javadi.alarm.database.DBC;
import com.javadi.alarm.receiver.MyReceiver;
import com.javadi.alarm.R;
import com.javadi.alarm.util.App;
import java.util.Calendar;

 public class AddAlarmsActivity extends AppCompatActivity {

     Button btnSet;
     TextView tvTimeSeted;
     TimePicker timePicker;
     static SharedPreferences sharedPreferences;
     int pendingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        timePicker=(TimePicker)findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);

        tvTimeSeted=(TextView)findViewById(R.id.tv_time_seted);
        btnSet=(Button)findViewById(R.id.btn_set);

        btnSet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(Build.VERSION.SDK_INT < 23){
                    int getHour = timePicker.getCurrentHour();
                    int getMinute = timePicker.getCurrentMinute();

                    try{
                        if(checkAlarmExists(getHour,getMinute)){
                            Toast.makeText(AddAlarmsActivity.this,"این آلارم وجود دارد",Toast.LENGTH_LONG).show();
                        }
                        else{
                            App.dbHelper.insertAlarm(getHour,getMinute);
                            Cursor cursor=App.dbHelper.getAlarms();
                            int count =cursor.getCount();
                            int n=0;
                            if(cursor.moveToFirst()){
                                do{
                                    n++;
                                    if(n==count){
                                        pendingId=cursor.getInt(0);
                                        break;
                                    }
                                }while (cursor.moveToNext());
                            }
                            setTime(getHour,getMinute,pendingId);
                        }
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                } else{
                    int getHour = timePicker.getHour();
                    int getMinute = timePicker.getMinute();
                    try{
                        if(checkAlarmExists(getHour,getMinute)){
                            Toast.makeText(AddAlarmsActivity.this,"این آلارم وجود دارد",Toast.LENGTH_LONG).show();
                        }
                        else{
                            App.dbHelper.insertAlarm(getHour,getMinute);
                            Cursor cursor=App.dbHelper.getAlarms();
                            int count =cursor.getCount();
                            int n=0;
                            if(cursor.moveToFirst()){
                                do{
                                    n++;
                                    if(n==count){
                                        pendingId=cursor.getInt(0);
                                        break;
                                    }
                                }while (cursor.moveToNext());
                            }
                            setTime(getHour,getMinute,pendingId);
                        }
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                }
                finish();
                //Toast.makeText(AddAlarmsActivity.this,pendingId+"",Toast.LENGTH_SHORT).show();
                Toast.makeText(AddAlarmsActivity.this,"آلارم فعال شد",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setTime(int Hour,int Minute,int id){
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,Hour);
        calendar.set(Calendar.MINUTE,Minute);
        calendar.set(Calendar.SECOND,0);
        if(calendar.getTimeInMillis()< System.currentTimeMillis()){
            calendar.add(Calendar.DATE,1);
        }
        Intent intent=new Intent(AddAlarmsActivity.this,MyReceiver.class);
        intent.setAction("com.javadi.alarm");
        AlarmManager alarmManager=(AlarmManager)getSystemService(getApplicationContext().ALARM_SERVICE);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),60000,pendingIntent);
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
