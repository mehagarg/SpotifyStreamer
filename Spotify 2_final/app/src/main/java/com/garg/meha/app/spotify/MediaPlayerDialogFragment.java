package com.garg.meha.app.spotify;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.garg.meha.app.spotify.model.TrackDto;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import kaaes.spotify.webapi.android.models.Tracks;

/**
 * Created by meha on 12/23/15.
 * http://www.tutorialspoint.com/android/android_mediaplayer.htm
 * http://stackoverflow.com/questions/27759393/mediaplayer-seekbar-and-chronometer-sync-issue
 */
public class MediaPlayerDialogFragment extends DialogFragment {
    private ImageButton previusButton, playButton, nextButton;
    private ImageView albumArtWorkImageView;
    private MediaPlayer mediaPlayer;
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private SeekBar seekbar;
    private TextView artistName, albumName, trackName, startTimeText, progressTimeTex;

    Context context;
    TrackDto trackDto;

    private int imageWidth = 200;
    private int imageHeight = 200;

    String previewURL;

    Tracks tracks;
    int position;


    public static int oneTimeOnly = 0;

    public MediaPlayerDialogFragment() {
    }

    public static MediaPlayerDialogFragment newInstance(Tracks tracks, TrackDto trackDto) {

        Bundle args = new Bundle();

        MediaPlayerDialogFragment fragment = new MediaPlayerDialogFragment();
        fragment.setArguments(args);
        fragment.tracks = tracks;
        fragment.trackDto = trackDto;

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog =  super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.track_player, null);
        initLayout();
        setArtistName();
        setTractDetails();

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Playing sound", Toast.LENGTH_SHORT).show();

                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mediaPlayer.setDataSource(previewURL);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }

                try {
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();

                finalTime = mediaPlayer.getDuration();
                startTime = mediaPlayer.getCurrentPosition();

                if (oneTimeOnly == 0) {
                    seekbar.setMax((int) finalTime);
                    oneTimeOnly = 1;
                }
                progressTimeTex.setText(String.format("%d min, %d sec",
                                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)))
                );

                startTimeText.setText(String.format("%d min, %d sec",
                                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
                );

                seekbar.setProgress((int) startTime);
                myHandler.postDelayed(UpdateSongTime, 100);
//                pauseButton.setEnabled(true);
//                playButton.setEnabled(false);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) startTime;

                if ((temp + forwardTime) <= finalTime) {
                    startTime = startTime + forwardTime;
                    mediaPlayer.seekTo((int) startTime);
                    Toast.makeText(getActivity(), "You have Jumped forward 5 seconds", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Cannot jump forward 5 seconds", Toast.LENGTH_SHORT).show();
                }
            }
        });

        previusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) startTime;

                if ((temp - backwardTime) > 0) {
                    startTime = startTime - backwardTime;
                    mediaPlayer.seekTo((int) startTime);
                    Toast.makeText(getActivity(), "You have Jumped backward 5 seconds", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Cannot jump backward 5 seconds", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setView(view);

        return builder.create();
    }



    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            startTimeText.setText(String.format("%d min, %d sec",

                            TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                            toMinutes((long) startTime)))
            );
            seekbar.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
        }
    };

    private void initLayout() {
        previusButton = (ImageButton) getActivity().findViewById(R.id.previousButton);
        playButton = (ImageButton) getActivity().findViewById(R.id.playButton);
        nextButton =(ImageButton) getActivity().findViewById(R.id.nextButton);

        albumArtWorkImageView =(ImageView) getActivity().findViewById(R.id.albumArtwork);

        artistName =(TextView) getActivity().findViewById(R.id.artistName);
        albumName =(TextView) getActivity().findViewById(R.id.albumName);
        trackName =(TextView) getActivity().findViewById(R.id.trackName);
        trackName.setText("Song.mp3");

        startTimeText = (TextView) getActivity().findViewById(R.id.startTime);
        progressTimeTex = (TextView) getActivity().findViewById(R.id.progressedTime);


        mediaPlayer = new MediaPlayer();
        seekbar=(SeekBar) getActivity().findViewById(R.id.seekBarDuration);
        seekbar.setClickable(false);
    }

    private void setTractDetails() {
//        Intent intent = getIntent();
        imageHeight = (int) getActivity().getResources().getDimension(R.dimen.image_height);
        imageWidth = (int) getActivity().getResources().getDimension(R.dimen.image_width);
//        Bundle data = intent.getExtras();
//        trackDto = (TrackDto) data.getParcelable("track");
        trackName.setText(trackDto.getName().toString());
        albumName.setText(trackDto.getAlbumName().toString());
        Picasso.with(context).load(trackDto.getArtistArtWorkUrl().toString()).resize(imageWidth, imageHeight).into(albumArtWorkImageView);
        previewURL = trackDto.preview_url.toString();
        position = trackDto.position;
        tracks.tracks.get(position);

//        SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
//        Gson gson = new Gson();
//        String json = mPrefs.getString("trackList", "");
//        tracks = gson.fromJson(json, Tracks.class);
    }

    private void setArtistName() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("artistName", Context.MODE_PRIVATE);
        String artistNameSP = sharedPref.getString("artistName", "");
        artistName.setText(artistNameSP.toString());
    }


}
