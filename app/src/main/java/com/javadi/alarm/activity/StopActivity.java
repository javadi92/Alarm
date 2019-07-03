package com.javadi.alarm.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.javadi.alarm.database.DBC;
import com.javadi.alarm.receiver.MyReceiver;
import com.javadi.alarm.R;
import com.javadi.alarm.util.App;
import com.ncorti.slidetoact.SlideToActView;
import org.jetbrains.annotations.NotNull;
import java.util.Calendar;
import static com.javadi.alarm.activity.AddAlarmsActivity.sharedPreferences;

public class StopActivity extends AppCompatActivity {

    static int pendingId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stop_alarm);

        final Window win= getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        SlideToActView sta = (SlideToActView) findViewById(R.id.example);

        sta.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(@NotNull SlideToActView slideToActView) {
                Calendar calendar=Calendar.getInstance();
                int h=calendar.getTime().getHours();
                int m=calendar.getTime().getMinutes();
                Cursor cursor=App.dbHelper.getAlarms();
                if(cursor.moveToFirst()){
                    do{
                        if(cursor.getInt(cursor.getColumnIndex(DBC.hour))==h){
                            if(cursor.getInt(cursor.getColumnIndex(DBC.minute))==m){
                                pendingId=cursor.getInt(0);
                            }
                        }
                    }while (cursor.moveToNext());
                }
                //Toast.makeText(getApplicationContext(),"آلارم متوقف شد",Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),pendingId+"",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getApplicationContext(), MyReceiver.class);
                intent.setAction("com.javadi.alarm");
                AlarmManager alarmManager=(AlarmManager)getSystemService(getApplicationContext().ALARM_SERVICE);
                PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),pendingId,intent,PendingIntent.FLAG_UPDATE_CURRENT );
                alarmManager.cancel(pendingIntent);
                App.mediaPlayer.stop();
                App.mediaPlayer= MediaPlayer.create(getApplicationContext(),R.raw.alarm2);
                /*SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putInt("h",00);
                editor.putInt("m",00);
                editor.commit();
                editor.apply();*/
                finishAffinity();
            }
        });
    }
}