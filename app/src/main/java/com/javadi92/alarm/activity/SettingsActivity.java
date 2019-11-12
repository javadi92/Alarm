package com.javadi92.alarm.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;

import com.javadi92.alarm.R;

public class SettingsActivity extends AppCompatActivity {

    Toolbar toolbarSettings;
    CheckBox checkBoxVibrate;
    ConstraintLayout clVibrate,clRingtone,clSoundIncrease,clIncreaseTime,clSnooz;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        checkBoxVibrate=(CheckBox)findViewById(R.id.checkBox_vibrate);

        clVibrate=(ConstraintLayout)findViewById(R.id.cl_vibrate);
        clRingtone=(ConstraintLayout)findViewById(R.id.cl_ringtone);
        clSoundIncrease=(ConstraintLayout)findViewById(R.id.cl_increase_sound);
        clIncreaseTime=(ConstraintLayout)findViewById(R.id.cl_increase_time);
        clSnooz=(ConstraintLayout)findViewById(R.id.cl_snooz_time);

        clVibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBoxVibrate.isChecked()){
                    checkBoxVibrate.setChecked(false);
                }
                else {
                    checkBoxVibrate.setChecked(true);
                }
            }
        });

        toolbarSettings=(Toolbar)findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbarSettings);
        getSupportActionBar().setTitle("تنظیمات");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarSettings.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
