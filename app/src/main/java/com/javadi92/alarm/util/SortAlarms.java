package com.javadi92.alarm.util;

import com.javadi92.alarm.model.Alarm;
import java.util.ArrayList;
import java.util.List;

public class SortAlarms {

    List<Alarm> alarms=new ArrayList<>();
    List<Alarm> sortedAlarms=new ArrayList<>();

    public SortAlarms(List<Alarm> alarms){
        this.alarms=alarms;
    }

    public List<Alarm> sort(){
        for(int i=0;i<alarms.size();i++){
            if(alarms.get(i).getAvailable()==1){
                sortedAlarms.add(alarms.get(i));
            }
        }
        if(sortedAlarms.size()==alarms.size()){
            return sortedAlarms;
        }
        else {
            for(int i=0;i<alarms.size();i++){
                if(alarms.get(i).getAvailable()==0){
                    sortedAlarms.add(alarms.get(i));
                }
            }
        }
        return sortedAlarms;
    }
}
