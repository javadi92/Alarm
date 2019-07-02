package com.javadi.alarm.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.javadi.alarm.adapter.AlarmAdapter;
import com.javadi.alarm.R;
import com.javadi.alarm.database.DBC;
import com.javadi.alarm.model.Alarm;
import com.javadi.alarm.util.App;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fabAddAlarm;
    static RecyclerView recyclerView;
    TextView tv;
    public static AlarmAdapter alarmAdapter;
    public static List<Alarm> alarms;
    static int pending;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarms=new ArrayList<>();

        getAlarms();

        tv=(TextView)findViewById(R.id.tv);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager llm=new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        alarmAdapter=new AlarmAdapter(alarms);
        recyclerView.setAdapter(alarmAdapter);

        fabAddAlarm=(FloatingActionButton)findViewById(R.id.fab_add_alarm);
        fabAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, AddAlarmsActivity.class);
                intent.putExtra("add_button",1);
                Cursor cursor=App.dbHelper.getAlarms();
                if(cursor.moveToLast()){
                    pending=cursor.getInt(0)+1;
                }
                else {
                    pending=1;
                }
                Toast.makeText(MainActivity.this,pending+"",Toast.LENGTH_LONG).show();
                intent.putExtra("pending_id",pending);
                startActivity(intent);
            }
        });

        if(alarms.size()>0){
            tv.setVisibility(View.GONE);
        }
    }

    public static void getAlarms(){
        Cursor cursor= App.dbHelper.getAlarms();
        if(cursor.moveToFirst()){
            do{
                Alarm alarm=new Alarm();
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
        }
    }
}
