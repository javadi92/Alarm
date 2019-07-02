 package com.javadi.alarm.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.javadi.alarm.receiver.MyReceiver;
import com.javadi.alarm.R;
import com.javadi.alarm.util.App;
import java.util.Calendar;

 public class AddAlarmsActivity extends AppCompatActivity {

     Button btnSet,btnStop;
     TextView tvTimeSeted;
     TimePicker timePicker;
     static SharedPreferences sharedPreferences;
     int pendingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);


        int check_add_button=getIntent().getIntExtra("add_button",0);
        if(check_add_button==1){
            pendingId=getIntent().getIntExtra("pending_id",0);
        }
        else{
            pendingId=getIntent().getIntExtra("p_id",0);
        }

        //sharedPreferences= getApplicationContext().getSharedPreferences("save_time",MODE_PRIVATE);

        timePicker=(TimePicker)findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);

        tvTimeSeted=(TextView)findViewById(R.id.tv_time_seted);
        btnSet=(Button)findViewById(R.id.btn_set);
        btnStop=(Button)findViewById(R.id.btn_stop);

        btnSet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT < 23){
                    int getHour = timePicker.getCurrentHour();
                    int getMinute = timePicker.getCurrentMinute();
                    setTime(getHour,getMinute);
                    try{
                        App.dbHelper.insertAlarm(getHour,getMinute);
                        MainActivity.alarms.clear();
                        MainActivity.getAlarms();
                        MainActivity.alarmAdapter.notifyDataSetChanged();
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                } else{
                    int getHour = timePicker.getHour();
                    int getMinute = timePicker.getMinute();
                    setTime(getHour,getMinute);
                    try{
                        App.dbHelper.insertAlarm(getHour,getMinute);
                        MainActivity.alarms.clear();
                        MainActivity.getAlarms();
                        MainActivity.alarmAdapter.notifyDataSetChanged();
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                }
                Toast.makeText(AddAlarmsActivity.this,"آلارم با موفقیت تنظیم شد",Toast.LENGTH_LONG).show();
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), MyReceiver.class);
                intent.setAction("com.javadi.alarm");
                AlarmManager alarmManager=(AlarmManager)getSystemService(getApplicationContext().ALARM_SERVICE);
                PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),pendingId,intent,PendingIntent.FLAG_UPDATE_CURRENT );
                alarmManager.cancel(pendingIntent);
                App.mediaPlayer.stop();
                App.mediaPlayer=MediaPlayer.create(getApplicationContext(),R.raw.alarm);
            }
        });
    }

    private void setTime(int Hour,int Minute){
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,Hour);
        calendar.set(Calendar.MINUTE,Minute);
        calendar.set(Calendar.SECOND,0);
        Intent intent=new Intent(AddAlarmsActivity.this,MyReceiver.class);
        intent.setAction("com.javadi.alarm");
        AlarmManager alarmManager=(AlarmManager)getSystemService(getApplicationContext().ALARM_SERVICE);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),pendingId,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),60000,pendingIntent);
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),60000,pendingIntent);
    }

    /*public void getTime(){
        //SharedPreferences save_time=getSharedPreferences("save_time",MODE_PRIVATE);
    }*/
}
