package com.javadi.alarm.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.javadi.alarm.adapter.AlarmAdapter;
import com.javadi.alarm.R;
import com.javadi.alarm.database.DBC;
import com.javadi.alarm.model.Alarm;
import com.javadi.alarm.receiver.MyReceiver;
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
                    //Toast.makeText(MainActivity.this,alarms.get(position).getMinute()+"",Toast.LENGTH_LONG).show();
                    Cursor cursor=App.dbHelper.getAlarms();
                    if(cursor.moveToFirst()){
                        do{
                            if(cursor.getInt(cursor.getColumnIndex(DBC.hour))==alarms.get(position).getHour()){
                                if(cursor.getInt(cursor.getColumnIndex(DBC.minute))==alarms.get(position).getMinute()){
                                    App.dbHelper.deleteAlarm(cursor.getInt(cursor.getColumnIndex(DBC.ID)));
                                    alarms.remove(position);
                                    alarmAdapter.notifyDataSetChanged();
                                    Intent intent=new Intent(getApplicationContext(), MyReceiver.class);
                                    intent.setAction("com.javadi.alarm");
                                    AlarmManager alarmManager=(AlarmManager)getSystemService(getApplicationContext().ALARM_SERVICE);
                                    PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),cursor.getInt(cursor.getColumnIndex(DBC.ID)),intent,PendingIntent.FLAG_UPDATE_CURRENT );
                                    alarmManager.cancel(pendingIntent);
                                }
                            }
                        }while (cursor.moveToNext());
                    }
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView); //set swipe to recylcerview

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
