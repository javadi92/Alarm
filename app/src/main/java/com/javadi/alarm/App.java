package com.javadi.alarm;

import android.app.Application;
import android.media.MediaPlayer;

public class App extends Application {

    public static MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer=MediaPlayer.create(getApplicationContext(),R.raw.alarm);
    }
}
