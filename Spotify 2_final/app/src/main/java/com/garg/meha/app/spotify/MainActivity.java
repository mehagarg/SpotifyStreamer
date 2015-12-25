package com.garg.meha.app.spotify;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class MainActivity extends ActionBarActivity implements MainActivityFragment.OnAlbumSelectedListener {

    private static final String SPOTIFYID = "spotifyId";
    private static final String TAG_MAIN_FRAGMENT = "main_fragment";
    private MainActivityFragment mMainFragment;
    private Boolean twoPane = false;
    boolean mIsLargeLayout;

    private static final String FRAGMENT_TAG = "tracks_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mIsLargeLayout = getResources().getBoolean(R.bool.large_layout);

        if (mIsLargeLayout || findViewById(R.id.fragment_container_top10) != null || isTablet(this)) {
            twoPane = true;
//            SharedPreferences mPref = getPreferences(MODE_PRIVATE);
//            SharedPreferences.Editor prefsEditor = mPref.edit();
//            prefsEditor.putBoolean("twoPane", twoPane);
//            prefsEditor.commit();

            FragmentManager fm = getFragmentManager();
            Top10TrackActivityFragment mTop10TrackActivityFragment = (Top10TrackActivityFragment) fm.findFragmentByTag(FRAGMENT_TAG);

            if (mTop10TrackActivityFragment == null) {
                mTop10TrackActivityFragment = new Top10TrackActivityFragment();
                fm.beginTransaction().add(R.id.fragment_container_top10, mTop10TrackActivityFragment, FRAGMENT_TAG).commit();
            }
        } else {
            twoPane = false;
        }
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    @Override
    public void onAlbumSelected(String spotifyId) {
        if (twoPane || mIsLargeLayout) {
            Bundle args = new Bundle();
            args.putString(SPOTIFYID, spotifyId);

            Top10TrackActivityFragment fragment = new Top10TrackActivityFragment();
            fragment.setArguments(args);

            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_top10, fragment)
                    .commit();
        } else {

            Intent intent = new Intent(this, Top10TrackActivity.class);
            intent.putExtra(SPOTIFYID, spotifyId);
            startActivity(intent);
        }
    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
//        twoPane = sharedPref.getBoolean("twoPane", false);
//    }
}
