package com.javadi.alarm.util;

import java.util.ArrayList;
import java.util.List;

public class SnoozShiftTime {

    private int hour;
    private int minute;

    public SnoozShiftTime(int hour,int minute){
        this.hour=hour;
        this.minute=minute;
    }

    public List finalTimes(int shift){
        if(minute>60-shift && hour<23){
            List<Integer> list=new ArrayList<>();
            list.add(hour+1);
            list.add(minute+shift-60);
            return list;
        }
        else if(minute>60-shift && hour==23){
            List<Integer> list=new ArrayList<>();
            list.add(0);
            list.add(minute+shift-60);
            return list;
        }
        else if(minute<60-shift && hour==23){
            List<Integer> list=new ArrayList<>();
            list.add(0);
            list.add(minute+shift);
            return list;
        }
        List<Integer> list=new ArrayList<>();
        list.add(hour);
        list.add(minute+shift);
        return list;
    }
}
