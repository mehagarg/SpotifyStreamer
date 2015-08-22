package com.garg.meha.app.spotify;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Top10TrackActivity extends AppCompatActivity {

    private static final String FRAGMENT_TAG = "tracks_fragment";
    Top10TrackActivityFragment mTop10TrackActivityFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top10_track);

        FragmentManager fm = getFragmentManager();
        mTop10TrackActivityFragment = (Top10TrackActivityFragment) fm.findFragmentByTag(FRAGMENT_TAG);

        if (mTop10TrackActivityFragment == null) {
            mTop10TrackActivityFragment = new Top10TrackActivityFragment();
            fm.beginTransaction().add(R.id.fragment_container_top10, mTop10TrackActivityFragment, FRAGMENT_TAG).commit();
        }
    }
}
