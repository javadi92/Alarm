package com.javadi.alarm.adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.javadi.alarm.R;
import com.javadi.alarm.activity.MainActivity;
import com.javadi.alarm.database.DBC;
import com.javadi.alarm.model.Alarm;
import com.javadi.alarm.receiver.MyReceiver;
import com.javadi.alarm.util.App;
import java.util.Calendar;
import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.myViewHolder> {

    private List<Alarm> alarms;
    private Context mContext;
    private boolean checkAlarmExists=false;

    public AlarmAdapter(Context context,List<Alarm> alarms){
        this.mContext=context;
        this.alarms=alarms;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.alarm_holder,viewGroup,false);;
        return new myViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final myViewHolder myViewHolder, final int i) {
        String hour=alarms.get(i).getHour()+"";
        String minute=alarms.get(i).getMinute()+"";
        if(alarms.get(i).getHour()<10){
            hour="0"+hour;
        }
        if (alarms.get(i).getMinute()<10){
            minute="0"+minute;
        }
        myViewHolder.tvHour.setText(hour);
        myViewHolder.tvMinute.setText(minute);

        final int h=Integer.parseInt(hour);
        final int m=Integer.parseInt(minute);

        if(alarms.get(i).getAvailable()==1){
            checkAlarmExists=true;
            myViewHolder.switchCompat.setChecked(true);
            myViewHolder.tvHour.setTextColor(Color.parseColor("#0A2DF1"));
            myViewHolder.textView.setTextColor(Color.parseColor("#0A2DF1"));
            myViewHolder.tvMinute.setTextColor(Color.parseColor("#0A2DF1"));
        }

        else if(alarms.get(i).getAvailable()==0){
            myViewHolder.switchCompat.setChecked(false);
            myViewHolder.tvHour.setTextColor(Color.LTGRAY);
            myViewHolder.textView.setTextColor(Color.LTGRAY);
            myViewHolder.tvMinute.setTextColor(Color.LTGRAY);
        }

        if(checkAlarmExists){
            Intent alarmChanged = new Intent("android.intent.action.ALARM_CHANGED");
            alarmChanged.putExtra("alarmSet", true/*enabled*/);
            mContext.sendBroadcast(alarmChanged);
        }

        myViewHolder.switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id=1;
                Cursor cursor=App.dbHelper.getAlarms();
                if(cursor.moveToFirst()){
                    do{
                        if(cursor.getInt(cursor.getColumnIndex(DBC.hour))==h && cursor.getInt(cursor.getColumnIndex(DBC.minute))==m){
                            id=cursor.getInt(0);
                            break;
                        }
                    }while (cursor.moveToNext());
                }
                Alarm alarm2=new Alarm();
                alarm2.setId(id);
                alarm2.setHour(h);
                alarm2.setMinute(m);
                if(myViewHolder.switchCompat.isChecked()){
                    Intent alarmChanged = new Intent("android.intent.action.ALARM_CHANGED");
                    alarmChanged.putExtra("alarmSet", true/*enabled*/);
                    mContext.sendBroadcast(alarmChanged);
                    checkAlarmExists=true;
                    myViewHolder.tvHour.setTextColor(Color.parseColor("#0A2DF1"));
                    myViewHolder.textView.setTextColor(Color.parseColor("#0A2DF1"));
                    myViewHolder.tvMinute.setTextColor(Color.parseColor("#0A2DF1"));
                    Toast.makeText(mContext,"آلارم فعال شد",Toast.LENGTH_SHORT).show();
                    Calendar calendar=Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY,h);
                    calendar.set(Calendar.MINUTE,m);
                    calendar.set(Calendar.SECOND,0);
                    if(calendar.getTimeInMillis()< System.currentTimeMillis()){
                        calendar.add(Calendar.DATE,1);
                    }
                    Intent intent=new Intent(mContext,MyReceiver.class);
                    intent.setAction("com.javadi.alarm");
                    AlarmManager alarmManager=(AlarmManager)mContext.getSystemService(mContext.ALARM_SERVICE);
                    PendingIntent pendingIntent=PendingIntent.getBroadcast(mContext,id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),60000,pendingIntent);
                    App.dbHelper.updateAlarm(id,h,m,1);
                    alarm2.setAvailable(1);
                    alarms.set(i,alarm2);
                }else {
                    myViewHolder.tvHour.setTextColor(Color.LTGRAY);
                    myViewHolder.textView.setTextColor(Color.LTGRAY);
                    myViewHolder.tvMinute.setTextColor(Color.LTGRAY);
                    Toast.makeText(mContext,"آلارم غیر فعال شد",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(mContext, MyReceiver.class);
                    intent.setAction("com.javadi.alarm");
                    AlarmManager alarmManager=(AlarmManager)mContext.getSystemService(mContext.ALARM_SERVICE);
                    PendingIntent pendingIntent=PendingIntent.getBroadcast(mContext,id,intent,PendingIntent.FLAG_UPDATE_CURRENT );
                    //Toast.makeText(mContext,id+"",Toast.LENGTH_SHORT).show();
                    alarmManager.cancel(pendingIntent);
                    App.mediaPlayer.stop();
                    App.mediaPlayer= MediaPlayer.create(mContext,R.raw.alarm2);
                    App.dbHelper.updateAlarm(id,h,m,0);
                    alarm2.setAvailable(0);
                    alarms.set(i,alarm2);
                    if(!checkActiveAlarm()){
                        Intent alarmChanged = new Intent("android.intent.action.ALARM_CHANGED");
                        alarmChanged.putExtra("alarmSet", false);
                        mContext.sendBroadcast(alarmChanged);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (alarms==null ?0:alarms.size());
    }


    class myViewHolder extends RecyclerView.ViewHolder{

        TextView tvHour;
        TextView textView;
        TextView tvMinute;
        //SwitchButton aSwitch;
        SwitchCompat switchCompat;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHour=(TextView)itemView.findViewById(R.id.tv_hour);
            textView=(TextView)itemView.findViewById(R.id.textView);
            tvMinute=(TextView)itemView.findViewById(R.id.tv_minute);
            //aSwitch=(SwitchButton)itemView.findViewById(R.id.switch_button);
            switchCompat=(SwitchCompat)itemView.findViewById(R.id.switch_compat);
        }
    }

    public void deleteAlarm(int id){
        App.dbHelper.deleteAlarm(id);
        for(int i=0;i<alarms.size();i++){
            if(alarms.get(i).getId()==id){
                alarms.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, alarms.size());
                //notifyDataSetChanged();
                break;
            }
        }
    }

    private boolean checkActiveAlarm(){
        for(int i=0;i<alarms.size();i++){
            if(alarms.get(i).getAvailable()==1){
                return true;
            }
        }
        return false;
    }
}
