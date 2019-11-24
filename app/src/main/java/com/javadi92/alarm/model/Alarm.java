package com.javadi92.alarm.model;

import java.util.Comparator;

public class Alarm {

    private int id;
    private int hour;
    private int minute;
    private int available;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public static final Comparator<Alarm> ALARM_COMPARATOR=new Comparator<Alarm>() {
        @Override
        public int compare(Alarm alarm1, Alarm alarm2) {
            return alarm2.getAvailable()-alarm1.getAvailable();
        }
    };
}
