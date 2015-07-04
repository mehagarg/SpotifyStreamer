package com.garg.meha.app.spotify;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;


/**
 * Fragment of main activity
 */
public class MainActivityFragment extends Fragment implements EditText.OnEditorActionListener {
    public static final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    private EditText mSearchText;
    private ListView mListView;
    private TextView mDummyTextView;

    SpotifyListAdapter mSpotifyListAdapter;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_artist_list, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_start_spotify:
                // think of something else
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Performing Spotify api background service task
    private void startSpotifyService(String artistQueryParam) {
        FetchArtistTask fetchArtistTask = new FetchArtistTask();
        fetchArtistTask.execute(artistQueryParam);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mSearchText = (EditText) view.findViewById(R.id.search_artist_editText);
        mListView = (ListView) view.findViewById(R.id.artist_list_view);
        mDummyTextView = (TextView) view.findViewById(R.id.dummyText);

        mSearchText.setOnEditorActionListener(this);
        return view;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String artistQueryParam = mSearchText.getText().toString();
            startSpotifyService(artistQueryParam);
            return true;
        }
        return false;
    }

    /**
     * FetchArtistTask extends Background AysncTask to download data from Spotify Wrapper class
     */
    public class FetchArtistTask extends AsyncTask<String, Void, List<Artist>> {
        private ProgressDialog dialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Downloading albums ...");
            dialog.show();
        }

        @Override
        protected List<Artist> doInBackground(String... params) {
            //        Connect to the Spotify api with the wrapper
            SpotifyApi api = new SpotifyApi();

            //        Create a SpotifyService object that we can use to get desired data.
            SpotifyService spotify = api.getService();

            ArtistsPager results = spotify.searchArtists(params[0]);
            List<Artist> artists = results.artists.items;

            return artists;
        }

        @Override
        protected void onPostExecute(List<Artist> artists) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            if (artists == null) {
                Toast.makeText(getActivity(), "Cannot match the artist's name. Please try again", Toast.LENGTH_LONG).show();
            }
            if (artists != null) {
                mDummyTextView.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
                mSpotifyListAdapter = new SpotifyListAdapter(getActivity(), artists);
                mListView.setAdapter(mSpotifyListAdapter);
            }
        }
    }
}
