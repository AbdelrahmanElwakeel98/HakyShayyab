package com.abdelrahman.irihackathon.Adapter;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abdelrahman.irihackathon.Common.Global;
import com.abdelrahman.irihackathon.Model.Answer;
import com.abdelrahman.irihackathon.Model.Manual;
import com.abdelrahman.irihackathon.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ManualAdapter extends RecyclerView.Adapter<ManualAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Manual> manuals;
    private Handler threadHandler = new Handler();

    public ManualAdapter(Context context, ArrayList<Manual> manuals){
        this.context = context;
        this.manuals = manuals;
    }

    @NonNull
    @Override
    public ManualAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (Global.chooseManual.equals("Manual")){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_manual,parent,false);
            ManualAdapter.ViewHolder holder = new ViewHolder(v);
            return holder;
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_experience,parent,false);
            ManualAdapter.ViewHolder holder = new ViewHolder(v);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ManualAdapter.ViewHolder holder, final int position) {

        if (Global.chooseManual.equals("Manual")){
            /*holder.title.setText(manuals.get(position).getTitle());
            holder.description.setText(manuals.get(position).getDescription());
            Picasso.get().load(manuals.get(position).getMedia()).into(holder.img);*/

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();

            String[] urlPart = manuals.get(position).getMedia().split("/");
            String[] getName = urlPart[urlPart.length - 1].split("\\?");

            // Get reference to the file
            StorageReference forestRef = storageRef.child(getName[0]);

            forestRef.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                @Override
                public void onSuccess(StorageMetadata storageMetadata) {
                    if (storageMetadata.getContentType().startsWith("image")){

                        holder.imgLayout.setVisibility(View.VISIBLE);
                        holder.title.setText(manuals.get(position).getTitle());
                        holder.description.setText(manuals.get(position).getDescription());
                        Picasso.get().load(manuals.get(position).getMedia()).into(holder.img);


                    } else if (storageMetadata.getContentType().startsWith("audio")){

                        holder.songLayout.setVisibility(View.VISIBLE);

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
                                // The duration in milliseconds
                                int duration = mediaPlayer.getDuration();

                                int currentPosition = mediaPlayer.getCurrentPosition();
                                if(currentPosition== 0)  {
                                    holder.seekBar.setMax(duration);

                                    String maxTimeString = millisecondsToString(duration);

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


                    } else if (storageMetadata.getContentType().startsWith("video")){

                        final int curr = 0;

                        holder.videoLayout.setVisibility(View.VISIBLE);
                        holder.title.setText(manuals.get(position).getTitle());
                        holder.description.setText(manuals.get(position).getDescription());

                        final MediaController mediaController = new MediaController(context);

                        // Set the videoView that acts as the anchor for the MediaController.
                        mediaController.setAnchorView(holder.videoView);


                        // Set MediaController for VideoView
                        holder.videoView.setMediaController(mediaController);

                        String videoUrl = manuals.get(position).getMedia();
                        Uri video = Uri.parse(videoUrl);
                        holder.videoView.setVideoURI(video);

                        holder.videoView.requestFocus();

                        // When the video file ready for playback.
                        holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                            public void onPrepared(MediaPlayer mediaPlayer) {


                                holder.videoView.seekTo(curr);
                                if (curr == 0) {
                                    holder.videoView.pause();
                                }

                                // When video Screen change size.
                                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                                    @Override
                                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

                                        // Re-Set the videoView that acts as the anchor for the MediaController
                                        mediaController.setAnchorView(holder.videoView);
                                    }
                                });
                            }
                        });

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(context, exception.getMessage(), Toast.LENGTH_LONG).show();
                }
            });





        } else {
            holder.title.setText(manuals.get(position).getTitle());
            holder.description.setText(manuals.get(position).getLocation());
            Picasso.get().load(manuals.get(position).getMedia()).into(holder.img);


        }

    }

    @Override
    public int getItemCount() {
        return manuals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public FrameLayout imgLayout, videoLayout;
        public LinearLayout songLayout;
        public TextView title, description, period;
        public ImageView img, start, pause;
        public SeekBar seekBar;
        public VideoView videoView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            imgLayout = itemView.findViewById(R.id.image_layout);
            songLayout = itemView.findViewById(R.id.song_layout);
            videoLayout = itemView.findViewById(R.id.video_layout);

            title = itemView.findViewById(R.id.txt_headline);
            description = itemView.findViewById(R.id.txt_description);
            period = itemView.findViewById(R.id.txt_max);

            seekBar = itemView.findViewById(R.id.seekBar);

            start = itemView.findViewById(R.id.button_start);
            pause = itemView.findViewById(R.id.button_pause);

            img = itemView.findViewById(R.id.img);
            videoView = itemView.findViewById(R.id.video_view);

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
