package com.javadi92.alarm.adapter;

import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.javadi92.alarm.R;

import java.util.ArrayList;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.myViewHolder> {

    List<Uri> uris=new ArrayList<>();
    RadioButton radioButton;
    public static MediaPlayer mediaPlayer;

    public MusicAdapter (List<Uri> uris){
        this.uris=uris;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.alarm_music_holder,viewGroup,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder myViewHolder, final int i) {
        Ringtone ringtone = RingtoneManager.getRingtone(myViewHolder.itemView.getContext(), uris.get(i));
        String title = ringtone.getTitle(myViewHolder.itemView.getContext());
        myViewHolder.rbMusic.setText(title);

        myViewHolder.rbMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(myViewHolder.itemView.getContext(),"aaa",Toast.LENGTH_LONG).show();
                if(mediaPlayer!=null){
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                    }
                }
                mediaPlayer=MediaPlayer.create(myViewHolder.itemView.getContext(),uris.get(i));


                mediaPlayer.start();
                if(radioButton==null){
                    //myViewHolder.rbMusic.setChecked(true);
                    radioButton=myViewHolder.rbMusic;
                }
                else {
                    radioButton.setChecked(false);
                    radioButton=myViewHolder.rbMusic;
                    //radioButton.setChecked(true);
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return uris.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        RadioButton rbMusic;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            rbMusic=(RadioButton) itemView.findViewById(R.id.radioButton);
        }
    }
}
