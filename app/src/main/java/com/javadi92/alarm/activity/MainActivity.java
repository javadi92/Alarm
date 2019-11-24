package com.javadi92.alarm.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.SortedList;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.javadi92.alarm.adapter.AlarmAdapter;
import com.javadi92.alarm.R;
import com.javadi92.alarm.database.DBC;
import com.javadi92.alarm.model.Alarm;
import com.javadi92.alarm.receiver.MyReceiver;
import com.javadi92.alarm.service.MyService;
import com.javadi92.alarm.util.App;
import com.javadi92.alarm.util.SortAlarms;
import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        TimePickerDialog.OnTimeSetListener {

    FloatingActionButton fabAddAlarm;
    RecyclerView recyclerView;
    TextView tv,tv2;
    ImageView imageViewSettings;
    Toolbar toolbarMain;
    AlarmAdapter alarmAdapter;
    List<Alarm> alarms;
    static int pending;
    private static final String TIMEPICKER = "TimePickerDialog";
    int pendingId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(MyService.ringtoneAlarm!=null){
            if(MyService.ringtoneAlarm.isPlaying()){
                Intent intent2=new Intent(MainActivity.this,StopActivity.class);
                startActivity(intent2);
            }
        }

        alarms=new ArrayList<>();

        //getAlarms();

        tv=(TextView)findViewById(R.id.tv);
        tv2=(TextView)findViewById(R.id.tv2);
        imageViewSettings=(ImageView)findViewById(R.id.img_settings);
        toolbarMain=(Toolbar)findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbarMain);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager llm=new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        alarmAdapter=new AlarmAdapter(getApplicationContext(),alarms);
        recyclerView.setAdapter(alarmAdapter);

        ItemTouchHelper.SimpleCallback simpleCallback=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition(); //get position which is swipe

                if (i == ItemTouchHelper.LEFT) {    //if swipe left
                    //Cursor cursor=App.dbHelper.getAlarms();
                    if(alarms.size()>0){

                        //Toast.makeText(MainActivity.this,alarms.get(position).getId()+"",Toast.LENGTH_LONG).show();

                        //cancel alarm
                        Intent intent=new Intent(getApplicationContext(), MyReceiver.class);
                        intent.setAction("com.javadi.alarm");
                        AlarmManager alarmManager=(AlarmManager)getSystemService(getApplicationContext().ALARM_SERVICE);
                        PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),alarms.get(position).getId(),intent,PendingIntent.FLAG_UPDATE_CURRENT );
                        //Toast.makeText(MainActivity.this,alarms.get(position).getId()+"",Toast.LENGTH_SHORT).show();
                        alarmManager.cancel(pendingIntent);
                        //App.mediaPlayer.stop();
                        //App.vibrate.cancel();

                        Intent stopservice=new Intent(MainActivity.this, MyService.class);
                        stopService(stopservice);
                        //App.mediaPlayer= MediaPlayer.create(getApplicationContext(),R.raw.alarm2);
                        App.sharedPreferences.edit().putInt("is_run",0).commit();
                        App.sharedPreferences.edit().putInt("pending_id",0).commit();

                        Intent alarmChanged = new Intent("android.intent.action.ALARM_CHANGED");
                        alarmChanged.putExtra("alarmSet", false/*enabled*/);
                        MainActivity.this.sendBroadcast(alarmChanged);

                        //delete from database
                        alarmAdapter.deleteAlarm(alarms.get(position).getId());

                        //show textview that no alarms available to show
                        if(alarms.size()==0){
                            tv.setVisibility(View.VISIBLE);
                            tv2.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        };

        imageViewSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingsIntent=new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(settingsIntent);
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView); //set swipe to recylcerview

        fabAddAlarm=(FloatingActionButton)findViewById(R.id.fab_add_alarm);
        fabAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PersianCalendar now = new PersianCalendar();
                com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog tpd =
                        com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog.newInstance(new com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                                if(Build.VERSION.SDK_INT < 23){
                                    int getHour = hourOfDay;
                                    int getMinute = minute;

                                    try{
                                        if(checkAlarmExists(getHour,getMinute)){
                                            Toast.makeText(MainActivity.this,"این آلارم وجود دارد",Toast.LENGTH_LONG).show();
                                        }
                                        else{
                                            Toast.makeText(MainActivity.this,"آلارم فعال شد",Toast.LENGTH_LONG).show();
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
                                    int getHour = hourOfDay;
                                    int getMinute = minute;
                                    try{
                                        if(checkAlarmExists(getHour,getMinute)){
                                            Toast.makeText(MainActivity.this,"این آلارم وجود دارد",Toast.LENGTH_LONG).show();
                                        }
                                        else{
                                            Toast.makeText(MainActivity.this,"آلارم فعال شد",Toast.LENGTH_LONG).show();
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
                                //finish();
                                //Toast.makeText(AddAlarmsActivity.this,pendingId+"",Toast.LENGTH_SHORT).show();

                                alarms.clear();
                                getAlarms();
                                Collections.sort(alarms,Alarm.ALARM_COMPARATOR);
                                if(alarms.size()==0){
                                    tv.setVisibility(View.VISIBLE);
                                    tv2.setVisibility(View.VISIBLE);
                                }
                                else{
                                    tv.setVisibility(View.GONE);
                                    tv2.setVisibility(View.GONE);
                                }

                                alarmAdapter.notifyDataSetChanged();
                            }
                            }, now.get(PersianCalendar.HOUR_OF_DAY), now.get(PersianCalendar.MINUTE), true);
                tpd.setThemeDark(false);
                //tpd.setTypeface(fontName);
                tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {

                    }
                });
                tpd.show(getFragmentManager(), TIMEPICKER);
            }
        });

        if(alarms.size()>0){
            tv.setVisibility(View.GONE);
            tv2.setVisibility(View.GONE);
        }
    }

    public void getAlarms(){
        Cursor cursor= App.dbHelper.getAlarms();
        if(cursor.moveToFirst()){
            do{
                Alarm alarm=new Alarm();
                alarm.setId(cursor.getInt(cursor.getColumnIndex(DBC.ID)));
                alarm.setHour(cursor.getInt(cursor.getColumnIndex(DBC.hour)));
                alarm.setMinute(cursor.getInt(cursor.getColumnIndex(DBC.minute)));
                alarm.setAvailable(cursor.getInt(cursor.getColumnIndex(DBC.available)));
                if(!alarms.contains(alarm)){
                    alarms.add(alarm);
                }
            }while (cursor.moveToNext());
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(alarms.size()>0){
            tv.setVisibility(View.GONE);
            tv2.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        alarms.clear();
        getAlarms();
        Collections.sort(alarms,Alarm.ALARM_COMPARATOR);
        alarmAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

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
        Intent intent=new Intent(MainActivity.this,MyReceiver.class);
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
            MainActivity.this.sendBroadcast(alarmChanged);
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
