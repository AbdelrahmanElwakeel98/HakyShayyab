package com.abdelrahman.irihackathon.Adapter;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abdelrahman.irihackathon.Model.Answer;
import com.abdelrahman.irihackathon.Model.Manual;
import com.abdelrahman.irihackathon.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import android.os.Handler;
import android.widget.Toast;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Manual> manuals;
    private Handler threadHandler = new Handler();

    public SongAdapter(Context context, ArrayList<Manual> manuals){
        this.context = context;
        this.manuals = manuals;
    }

    @NonNull
    @Override
    public SongAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_songs,parent,false);
        SongAdapter.ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.title.setText(manuals.get(position).getTitle());
        holder.description.setText(manuals.get(position).getDescription());

        String url = manuals.get(position).getMedia(); // your URL here
        final MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare(); // might take long! (for buffering, etc)
        } catch (IOException e) {
            e.printStackTrace();
        }

        holder.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context, String.valueOf(position), Toast.LENGTH_LONG).show();
                // The duration in milliseconds
                int duration = mediaPlayer.getDuration();

                int currentPosition = mediaPlayer.getCurrentPosition();
                if(currentPosition== 0)  {
                    holder.seekBar.setMax(duration);

                    String maxTimeString = millisecondsToString(duration);

                    holder.period.setText(maxTimeString);

                } else if(currentPosition == duration)  {
                    // Resets the MediaPlayer to its uninitialized state.
                    mediaPlayer.reset();
                }

                mediaPlayer.start();

                // Create a thread to update position of SeekBar.
                UpdateSeekBarThread updateSeekBarThread= new UpdateSeekBarThread(mediaPlayer, holder.seekBar, holder.period);
                threadHandler.postDelayed(updateSeekBarThread,50);

                holder.pause.setEnabled(true);
                holder.start.setEnabled(false);

            }
        });

        holder.pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();
                holder.pause.setEnabled(false);
                holder.start.setEnabled(true);
            }
        });

    }

    @Override
    public int getItemCount() {
        return manuals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView title, description, period;
        public Button start, pause;
        public SeekBar seekBar;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            title = itemView.findViewById(R.id.txt_headline);
            description = itemView.findViewById(R.id.txt_description);
            period = itemView.findViewById(R.id.txt_max);
            seekBar = itemView.findViewById(R.id.seekBar);
            start = itemView.findViewById(R.id.button_start);
            pause = itemView.findViewById(R.id.button_pause);

        }
    }

    // Convert millisecond to string.
    private String millisecondsToString(int milliseconds)  {
        long minutes = TimeUnit.MILLISECONDS.toMinutes((long) milliseconds);
        long seconds =  TimeUnit.MILLISECONDS.toSeconds((long) milliseconds) ;
        return minutes+":"+ seconds;
    }

    // Thread to Update position for SeekBar.
    class UpdateSeekBarThread implements Runnable {

        private MediaPlayer mediaPlayer;
        private SeekBar seekBar;
        private TextView period;

        public UpdateSeekBarThread(MediaPlayer mediaPlayer, SeekBar seekBar, TextView period){
            this.mediaPlayer = mediaPlayer;
            this.seekBar = seekBar;
            this.period = period;
        }

        public void run()  {
            int currentPosition = mediaPlayer.getCurrentPosition();
            String currentPositionStr = millisecondsToString(currentPosition);
            period.setText(currentPositionStr);

            seekBar.setProgress(currentPosition);
            // Delay thread 50 milisecond.
            threadHandler.postDelayed(this, 50);
        }
    }
}
