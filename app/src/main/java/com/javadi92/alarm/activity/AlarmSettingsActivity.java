package com.javadi92.alarm.activity;

import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.javadi92.alarm.R;
import com.javadi92.alarm.adapter.MusicAdapter;
import java.util.ArrayList;
import java.util.List;

public class AlarmSettingsActivity extends AppCompatActivity {

    TextView tvRingtone;
    AlertDialog dialog;
    Spinner spinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_settings);

        tvRingtone=(TextView)findViewById(R.id.tv_ringtone);

        spinner=(Spinner)findViewById(R.id.spinner);
        String[] items=new String[]{"هر یک ثانیه","هر دو ثانیه","هر سه ثانیه"};
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,items);
        spinner.setAdapter(adapter);

        tvRingtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Uri> uris=new ArrayList<>();
                RingtoneManager manager = new RingtoneManager(getApplicationContext());
                manager.setType(RingtoneManager.TYPE_ALARM);
                Cursor cursor = manager.getCursor();
                while (cursor.moveToNext()) {
                    String title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
                    Uri ringtoneURI = manager.getRingtoneUri(cursor.getPosition());
                    uris.add(ringtoneURI);
                    // Do something with the title and the URI of ringtone
                }

                final AlertDialog.Builder builder=new AlertDialog.Builder(AlarmSettingsActivity.this);
                LayoutInflater inflater = (LayoutInflater) AlarmSettingsActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.ringtones_list, null);
                Button btnConfirm,btnCancle;
                RecyclerView recyclerView=(RecyclerView)dialogView.findViewById(R.id.rec_ringtones);
                LinearLayoutManager llm=new LinearLayoutManager(AlarmSettingsActivity.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(llm);
                final MusicAdapter musicAdapter=new MusicAdapter(uris);

                recyclerView.setAdapter(musicAdapter);
                btnConfirm=(Button)dialogView.findViewById(R.id.btn_confirm);
                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(),"موفقیت آمیز",Toast.LENGTH_LONG).show();
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
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(MusicAdapter.mediaPlayer!=null){
            MusicAdapter.mediaPlayer.stop();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(MusicAdapter.mediaPlayer!=null){
            MusicAdapter.mediaPlayer.stop();
        }
    }
}
