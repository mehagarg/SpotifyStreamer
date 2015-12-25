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
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.garg.meha.app.spotify.model.TrackDto;
import com.garg.meha.app.spotify.model.TrackObj;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import kaaes.spotify.webapi.android.models.Tracks;

/**
 * Created by meha on 12/23/15.
 * http://www.tutorialspoint.com/android/android_mediaplayer.htm
 */
public class CustomDialogFrag extends DialogFragment {
    private ImageButton previusButton, playButton, nextButton;
    private ImageView albumArtWorkImageView;
    private MediaPlayer mediaPlayer;
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();;
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private SeekBar seekbar;
    private TextView artistName, albumName, trackName, startTimeText, progressTimeTex;
    private List<TrackObj> trackObjList;

    Context context;

    private int imageWidth = 200;
    private int imageHeight = 200;

    String previewURL;

    Tracks tracks;
    int position;
    View rootView;

    public static int oneTimeOnly = 0;
    private  TrackDto trackDto;


    public static CustomDialogFrag newInstance(TrackDto trackDto) {

        Bundle args = new Bundle();

        CustomDialogFrag fragment = new CustomDialogFrag();
        fragment.setArguments(args);
        fragment.trackDto = trackDto;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.track_player, null);
        } else {
            ((ViewGroup) container.getParent()).removeView(rootView);
        }
        initLayout(rootView);
        setArtistName();
        setTractDetails();
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playTrack();
                runSeekbar();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position <= trackObjList.size() - 2) {
                    TrackObj trackObj = trackObjList.get(position + 1);
                    setTrackDetails(trackObj, position + 1);
                    playTrack();
                    runSeekbar();
                }
            }
        });

        previusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position > 0) {
                    TrackObj trackObj = trackObjList.get(position - 1);
                    setTrackDetails(trackObj, position - 1);
                }
            }
        });
        return rootView;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;

    }


    private void runSeekbar() {
        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();

        if (oneTimeOnly == 0) {
            seekbar.setMax((int) finalTime);
            oneTimeOnly = 1;
        }
        progressTimeTex.setText(String.format("%d : %d",
                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)))
        );

        startTimeText.setText(String.format("%d : %d",
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
        );

        seekbar.setProgress((int) startTime);
        myHandler.postDelayed(UpdateSongTime, 100);
    }

    private void playTrack() {
        if (mediaPlayer.isPlaying()) {
            pauseTrack();
        } else {
            playButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_pause));
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
        }
    }

    private void pauseTrack() {
        mediaPlayer.pause();
        playButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_play));
    }

    private void setTrackDetails(TrackObj trackObj, int newPosition) {
        imageHeight = (int) getResources().getDimension(R.dimen.image_height);
        imageWidth = (int) getResources().getDimension(R.dimen.image_width);
        trackName.setText(trackObj.getTrackName().toString());
        albumName.setText(trackObj.getAlbumName().toString());
        Picasso.with(context).load(trackObj.getArtistArtWorkUrl().toString()).resize(imageWidth, imageHeight).into(albumArtWorkImageView);
        previewURL = trackObj.preview_url.toString();

        position = newPosition;
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            startTimeText.setText(String.format("%d : %d",

                            TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                            toMinutes((long) startTime)))
            );
            if (startTime == finalTime) {
                playButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_play));
            }
            seekbar.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
        }
    };

    private void initLayout(View rootView) {
        previusButton = (ImageButton) rootView.findViewById(R.id.previousButton);
        playButton = (ImageButton) rootView.findViewById(R.id.playButton);
        nextButton =(ImageButton) rootView.findViewById(R.id.nextButton);

        albumArtWorkImageView =(ImageView) rootView.findViewById(R.id.albumArtwork);

        artistName =(TextView) rootView.findViewById(R.id.artistName);
        albumName =(TextView) rootView.findViewById(R.id.albumName);
        trackName =(TextView) rootView.findViewById(R.id.trackName);
//        trackName.setText("Song.mp3");

        startTimeText = (TextView) rootView.findViewById(R.id.startTime);
        progressTimeTex = (TextView) rootView.findViewById(R.id.progressedTime);


        mediaPlayer = new MediaPlayer();
        seekbar=(SeekBar) rootView.findViewById(R.id.seekBarDuration);
//        seekbar.setClickable(false);
    }

    private void setTractDetails() {
        Intent intent = getActivity().getIntent();
        imageHeight = (int) getResources().getDimension(R.dimen.image_height);
        imageWidth = (int) getResources().getDimension(R.dimen.image_width);
        Bundle data = intent.getExtras();
        TrackDto trackDto = (TrackDto) data.getParcelable("track");
        trackName.setText(trackDto.getName().toString());
        albumName.setText(trackDto.getAlbumName().toString());
        Picasso.with(context).load(trackDto.getArtistArtWorkUrl().toString()).resize(imageWidth, imageHeight).into(albumArtWorkImageView);
        previewURL = trackDto.preview_url.toString();

        position = trackDto.position;

        trackObjList = trackDto.trackObjList;

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
