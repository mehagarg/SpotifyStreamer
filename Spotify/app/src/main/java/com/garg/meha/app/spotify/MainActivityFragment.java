package com.garg.meha.app.spotify;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
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
    public static final String searchField = "SEARCH_ARTIST";

    OnAlbumSelectedListener mCallback;

    /**
     * interface
     */
    public interface OnAlbumSelectedListener {
        void onAlbumSelected(String results);
    }

    private EditText mSearchText;
    private ListView mListView;
    private TextView mDummyTextView;

    private String searchArtistField;

    private SpotifyListAdapter mSpotifyListAdapter;
    private List<Artist> artistData = new ArrayList<>();


    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    public String getSearchText() {
        return this.searchArtistField;
    }

    public void setSearchText(String searchArtistField) {
        this.searchArtistField = searchArtistField;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnAlbumSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnAlbumSelectedListener");
        }
    }

    // Performing Spotify api background service task
    private void startSpotifyService(String artistQueryParam) {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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
        mSpotifyListAdapter = new SpotifyListAdapter(getActivity(), artistData, mCallback);
        mListView.setAdapter(mSpotifyListAdapter);
        mSearchText.setOnEditorActionListener(this);
        return view;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            searchArtistField = mSearchText.getText().toString();
            setSearchText(searchArtistField);
            startSpotifyService(searchArtistField);
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
            // hide the progress dialog
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            // if search results are empty, display this toast
            if (artists == null || artists.isEmpty()) {
                Toast.makeText(getActivity(), "Cannot find the artist! Please try with a different name", Toast.LENGTH_LONG).show();
            }
            // set the adapter with the new data received
            if (artists != null || !artists.isEmpty()) {
                artistData.clear();
                artistData.addAll(artists);
                mSpotifyListAdapter.notifyDataSetChanged();
                mDummyTextView.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(searchField, mSearchText.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            startSpotifyService(savedInstanceState.getString(searchField));
        }
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getSearchText() != null) {
            startSpotifyService(getSearchText());
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }
}
