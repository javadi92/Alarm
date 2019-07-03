package com.javadi.alarm.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.javadi.alarm.R;
import com.javadi.alarm.activity.AddAlarmsActivity;
import com.javadi.alarm.model.Alarm;
import com.suke.widget.SwitchButton;
import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.myViewHolder> {

    private List<Alarm> alarms;

    public AlarmAdapter(List<Alarm> alarms){
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

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(myViewHolder.itemView.getContext(), AddAlarmsActivity.class);
                intent2.putExtra("p_id",i);
                myViewHolder.itemView.getContext().startActivity(intent2);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (alarms==null ?0:alarms.size());
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        TextView tvHour;
        TextView tvMinute;
        SwitchButton aSwitch;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHour=(TextView)itemView.findViewById(R.id.tv_hour);
            tvMinute=(TextView)itemView.findViewById(R.id.tv_minute);
            aSwitch=(SwitchButton)itemView.findViewById(R.id.switch_button);
        }
    }
}
