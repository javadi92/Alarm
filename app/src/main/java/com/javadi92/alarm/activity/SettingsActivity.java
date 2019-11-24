package com.javadi92.alarm.activity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.javadi92.alarm.R;
import com.javadi92.alarm.adapter.MusicAdapter;
import com.javadi92.alarm.util.App;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbarSettings;
    CheckBox checkBoxVibrate,checkBoxIncreaseSound;
    TextView tvRingtone,tvIncreaseTime;
    ConstraintLayout clVibrate,clRingtone,clSoundIncrease,clIncreaseTime,clSnooz;
    AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        checkBoxVibrate=(CheckBox)findViewById(R.id.checkBox_vibrate);
        checkBoxIncreaseSound=(CheckBox)findViewById(R.id.checkBox_increase_sound);

        clVibrate=(ConstraintLayout)findViewById(R.id.cl_vibrate);
        clRingtone=(ConstraintLayout)findViewById(R.id.cl_ringtone);
        clSoundIncrease=(ConstraintLayout)findViewById(R.id.cl_increase_sound);
        clIncreaseTime=(ConstraintLayout)findViewById(R.id.cl_increase_time);
        clSnooz=(ConstraintLayout)findViewById(R.id.cl_snooz_time);

        tvRingtone=(TextView)findViewById(R.id.tv_ringtone);
        Ringtone ringtone = RingtoneManager.getRingtone(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
        String title = ringtone.getTitle(this);
        tvRingtone.setText(App.sharedPreferences.getString("alarm_name",title) );

        tvIncreaseTime=(TextView)findViewById(R.id.tv_increase_time);
        tvIncreaseTime.setText(App.sharedPreferences.getInt("increase_time",5)+" ثانیه");

        clVibrate.setOnClickListener(this);
        clSoundIncrease.setOnClickListener(this);
        clRingtone.setOnClickListener(this);
        clIncreaseTime.setOnClickListener(this);
        checkBoxIncreaseSound.setOnClickListener(this);
        checkBoxIncreaseSound.setOnClickListener(this);

        if(App.sharedPreferences.getBoolean("checkIncrease",false)){
            checkBoxIncreaseSound.setChecked(true);
        }

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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case (R.id.cl_vibrate):
                if(checkBoxVibrate.isChecked()){
                    checkBoxVibrate.setChecked(false);
                }
                else {
                    checkBoxVibrate.setChecked(true);
                }
                break;
            case (R.id.cl_ringtone)  :
                List<Uri> uris=new ArrayList<>();
                RingtoneManager manager = new RingtoneManager(getApplicationContext());
                manager.setType(RingtoneManager.TYPE_ALARM);
                Cursor cursor = manager.getCursor();
                while (cursor.moveToNext()) {
                    //String title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
                    Uri ringtoneURI = manager.getRingtoneUri(cursor.getPosition());
                    uris.add(ringtoneURI);
                    // Do something with the title and the URI of ringtone
                }

                final AlertDialog.Builder builder=new AlertDialog.Builder(SettingsActivity.this);
                LayoutInflater inflater = (LayoutInflater) SettingsActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.ringtones_list, null);
                Button btnConfirm,btnCancle;
                final RecyclerView recyclerView=(RecyclerView)dialogView.findViewById(R.id.rec_ringtones);
                LinearLayoutManager llm=new LinearLayoutManager(SettingsActivity.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(llm);
                final MusicAdapter musicAdapter=new MusicAdapter(uris);

                recyclerView.setAdapter(musicAdapter);
                btnConfirm=(Button)dialogView.findViewById(R.id.btn_confirm);
                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvRingtone.setText(MusicAdapter.name);
                        App.sharedPreferences.edit().putString("uri", String.valueOf(MusicAdapter.uri)).commit();
                        App.sharedPreferences.edit().putString("alarm_name", MusicAdapter.name).commit();
                        dialog.cancel();
                        if(MusicAdapter.mediaPlayer!=null){
                            MusicAdapter.mediaPlayer.stop();
                        }
                    }
                });
                btnCancle=(Button)dialogView.findViewById(R.id.btn_cancle);
                btnCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                        if(MusicAdapter.mediaPlayer!=null){
                            MusicAdapter.mediaPlayer.stop();
                        }
                    }
                });
                builder.setView(dialogView);
                dialog=builder.create();
                dialog.setCancelable(false);
                dialog.show();
                break;
            case (R.id.cl_increase_sound):
                if(checkBoxIncreaseSound.isChecked()){
                    checkBoxIncreaseSound.setChecked(false);
                    App.sharedPreferences.edit().putBoolean("checkIncrease",false).commit();
                }
                else {
                    checkBoxIncreaseSound.setChecked(true);
                    App.sharedPreferences.edit().putBoolean("checkIncrease",true).commit();
                }
                break;
            case (R.id.checkBox_increase_sound):
                if(!checkBoxIncreaseSound.isChecked()){
                    checkBoxIncreaseSound.setChecked(false);
                    App.sharedPreferences.edit().putBoolean("checkIncrease",false).commit();
                }
                else {
                    checkBoxIncreaseSound.setChecked(true);
                    App.sharedPreferences.edit().putBoolean("checkIncrease",true).commit();
                }
                break;
            case (R.id.cl_increase_time):
                AlertDialog.Builder builder2=new AlertDialog.Builder(SettingsActivity.this);
                builder2.setTitle("تعیین زمان چرت زدن");

                final EditText editText=new EditText(SettingsActivity.this);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                editText.setHint("زمان را وارد کنید");
                builder2.setView(editText);
                builder2.setPositiveButton("تایید", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int increase_time= (int) Integer.parseInt(editText.getText().toString());
                        if(increase_time>60 || increase_time<1){
                            Toast.makeText(SettingsActivity.this,"زمان وارد شده باید بین 1 تا 60 ثانیه باشد",Toast.LENGTH_LONG).show();
                        }
                        else {
                            App.sharedPreferences.edit().putInt("increase_time",increase_time).commit();
                            tvIncreaseTime.setText(increase_time+" ثانیه");
                            dialog.dismiss();
                        }

                    }
                });
                builder2.setNegativeButton("لغو", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.cancel();
                    }
                });
                dialog=builder2.create();
                dialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                dialog.show();
                break;
        }
    }
}
