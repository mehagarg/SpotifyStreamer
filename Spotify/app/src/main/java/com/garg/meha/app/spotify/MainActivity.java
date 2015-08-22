package com.garg.meha.app.spotify;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class MainActivity extends ActionBarActivity implements MainActivityFragment.OnAlbumSelectedListener {

    private static final String SPOTIFYID = "spotifyId";
    private static final String TAG_MAIN_FRAGMENT = "main_fragment";
    private MainActivityFragment mMainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        FragmentManager fm = getFragmentManager();
        mMainFragment = (MainActivityFragment) fm.findFragmentByTag(TAG_MAIN_FRAGMENT);

        if (mMainFragment == null) {
            mMainFragment = new MainActivityFragment();
            fm.beginTransaction().add(R.id.fragment_container_main, mMainFragment, TAG_MAIN_FRAGMENT).commit();
        }
    }

    @Override
    public void onAlbumSelected(String spotifyId) {
        Intent intent = new Intent(this, Top10TrackActivity.class);
        intent.putExtra(SPOTIFYID, spotifyId);
        startActivity(intent);
    }
}
