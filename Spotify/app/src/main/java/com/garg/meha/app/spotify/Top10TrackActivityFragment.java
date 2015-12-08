package com.garg.meha.app.spotify;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
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

/**
 * A placeholder fragment containing a simple view.
 */
public class Top10TrackActivityFragment extends Fragment {

    private ListView mListView;
    private TextView mDummyTextView;
    private static final String SPOTIFYID = "spotifyId";
    private Top10ListAdapter mTop10ListAdapter;

    public Top10TrackActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_top10_track, container, false);
        mListView = (ListView) view.findViewById(R.id.top10_listView);
        mDummyTextView = (TextView) view.findViewById(R.id.dummyTextTop10);

        Intent intent = getActivity().getIntent();
        String spotifyId = intent.getStringExtra(SPOTIFYID);
        startSpotifyServiceForAlbum(spotifyId);
        return view;
    }

    private void startSpotifyServiceForAlbum(String spotifyID) {
        FetchAlbumTask fetchAlbumTask = new FetchAlbumTask();
        fetchAlbumTask.execute(spotifyID);
    }

    private class FetchAlbumTask extends AsyncTask<String, Void, Tracks> {
        private ProgressDialog dialog = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Downloading Top 10 tracks of this album ...");
            dialog.show();
        }

        @Override
        protected Tracks doInBackground(String... params) {
            //        Connect to the Spotify api with the wrapper
            SpotifyApi api = new SpotifyApi();

            //        Create a SpotifyService object that we can use to get desired data.
            SpotifyService spotify = api.getService();
            Map<String, Object> map = new HashMap<>();
            map.put("country", "SE");
            Tracks tracks = spotify.getArtistTopTrack(params[0], map);
            return tracks;
        }

        @Override
        protected void onPostExecute(Tracks tracks) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            if (tracks == null) {
                Toast.makeText(getActivity(), "Cannot match the artist's name. Please try again", Toast.LENGTH_LONG).show();
            }
            if (tracks != null) {
                mDummyTextView.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
                mTop10ListAdapter = new Top10ListAdapter(getActivity(), tracks);
                mListView.setAdapter(mTop10ListAdapter);
            }

        }
    }


}


