package com.garg.meha.app.spotify;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Top10TrackActivity extends AppCompatActivity {
    private static final String POSTION = "position";
    private static final String SPOTIFYID = "spotifyId";

    private static final String FRAGMENT_TAG = "tracks_fragment";
    private String spotifyIDfromintent = null;
    Top10TrackActivityFragment mTop10TrackActivityFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top10_track);

//        Intent intent = getIntent();
//        if (intent != null && intent.hasExtra(SPOTIFYID)) {
//            spotifyIDfromintent = intent.getStringExtra(SPOTIFYID);
//        }

//        startSpotifyServiceForAlbum(spotifyID);


        getFragmentManager().beginTransaction().add(R.id.fragment_container_top10, new Top10TrackActivityFragment()).commit();
//        FragmentManager fm = getFragmentManager();
//        mTop10TrackActivityFragment = (Top10TrackActivityFragment) fm.findFragmentByTag(FRAGMENT_TAG);
//        if (mTop10TrackActivityFragment == null) {
//            mTop10TrackActivityFragment = new Top10TrackActivityFragment();
//            Bundle args = new Bundle();
//            args.putString(SPOTIFYID, spotifyIDfromintent);
//            fm.beginTransaction().add(R.id.fragment_container_top10, mTop10TrackActivityFragment, FRAGMENT_TAG).commit();
//        }
    }

    private void startSpotifyServiceForAlbum(String spotifyID) {
        FetchAlbumTask fetchAlbumTask = new FetchAlbumTask();
        fetchAlbumTask.execute(spotifyID);
    }

    public void getIntentdata(Intent intent) {
        Bundle b = intent.getBundleExtra("track");
        Log.i("bundel", b.toString());
    }

    private class FetchAlbumTask extends AsyncTask<String, Void, List<Track>> {

        @Override
        protected List<Track> doInBackground(String... params) {
            //        Connect to the Spotify api with the wrapper
            SpotifyApi api = new SpotifyApi();

            //        Create a SpotifyService object that we can use to get desired data.
            SpotifyService spotify = api.getService();
            Map<String, Object> map = new HashMap<>();
            map.put("country", "SE");
            spotify.getArtistTopTrack(params[0], map, new Callback<Tracks>() {
                @Override
                public void success(Tracks results, Response response) {



//                    Log.v("Spotify", "success");
//                    ArrayList<Track> tracks = new ArrayList<Track>(results.tracks);
//                    Log.d("String of Track", tracks.toString());
//                    for (int i = 0; i < results.tracks.size(); i++) {
//                        Log.d("Results from Top 10", tracks.get(i).album.name);
//                        Log.d("Results from Tracks", tracks.get(i).name);
//                        Log.d("Results from Images Url", tracks.get(i).album.images.get(0).url);
//                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.v("Spotify", "failure");
                }
            });
            return null;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_top10_track, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
