package com.garg.meha.app.spotify;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.garg.meha.app.spotify.model.TrackDto;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class PlayTrackActivity extends AppCompatActivity {
    private TextView artistName;
    private TextView albumName;
    private TextView trackName;
    private TextView startDuration;
    private TextView progressDuration;
    private SeekBar seekBar;
    private ImageButton playButton;
    private ImageButton previousTrackButton;
    private ImageButton nextTrackButton;
    private ImageView albumArtwork;
    private MediaPlayer mediaPlayer;


    Context context;

    private int imageWidth = 200;
    private int imageHeight = 200;

    String previewURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_player);

        initLayout();
        setArtistName();
        setTractDetails();

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMedia(previewURL);
            }
        });

    }

    private void setTractDetails() {
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        TrackDto trackDto = (TrackDto) data.getParcelable("track");
        trackName.setText(trackDto.getName().toString());
        albumName.setText(trackDto.getAlbumName().toString());
        Picasso.with(context).load(trackDto.getArtistArtWorkUrl().toString()).resize(imageWidth, imageHeight).into(albumArtwork);
        setProgressTime(trackDto.duration_ms);
        previewURL = trackDto.preview_url.toString();
        seekBar.setMax((int) trackDto.duration_ms);
    }

    Runnable run = new Runnable() {
        @Override
        public void run() {
            updateSeekbar();
        }
    };


    private void updateSeekbar() {
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        seekBar.postDelayed(run, 100);
    }

    private void playMedia(String previewURL) {
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

    private void setProgressTime(long duration_ms) {
        long minute = TimeUnit.MILLISECONDS.toMinutes(duration_ms);
        String timeLeft = (Long.valueOf(minute).toString());
        progressDuration.setText(timeLeft);
    }

    private void initLayout() {
        artistName = (TextView) findViewById(R.id.artistName);
        albumName = (TextView) findViewById(R.id.albumName);
        trackName = (TextView) findViewById(R.id.trackName);
        startDuration = (TextView) findViewById(R.id.startTime);
        progressDuration = (TextView) findViewById(R.id.progressedTime);
        seekBar = (SeekBar) findViewById(R.id.seekBarDuration);
        playButton = (ImageButton) findViewById(R.id.playButton);
        previousTrackButton = (ImageButton) findViewById(R.id.previousButton);
        nextTrackButton = (ImageButton) findViewById(R.id.nextButton);
        albumArtwork = (ImageView) findViewById(R.id.albumArtwork);
        mediaPlayer = new MediaPlayer();
    }

    private void setArtistName() {
        SharedPreferences sharedPref = getSharedPreferences("artistName", MODE_PRIVATE);
        String artistNameSP = sharedPref.getString("artistName", "");
        artistName.setText(artistNameSP.toString());
    }
}
