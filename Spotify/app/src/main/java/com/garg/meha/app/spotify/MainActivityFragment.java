package com.garg.meha.app.spotify;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Image;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements SearchView.OnQueryTextListener, View.OnFocusChangeListener, AdapterView.OnItemClickListener {
    public static final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    private SearchView mSearchView;
    private ListView mListView;


    ArrayAdapter<String> adapter;

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
                startSpotifyService();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startSpotifyService() {
        FetchArtistTask fetchArtistTask = new FetchArtistTask();
        fetchArtistTask.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mSearchView = (SearchView) view.findViewById(R.id.search_artist);
        mListView = (ListView) view.findViewById(R.id.artist_list_view);

        String[] artistsList = {"Rihanna", "Adele", "John Lennon", "SP Balasubramaniam", "Jennifer Lopez",
                "Justin Timberlake", "Rihanna", "Adele", "John Lennon", "SP Balasubramaniam", "Jennifer Lopez", "Justin Timberlake"};

        ArrayList<String> artist = new ArrayList<>(Arrays.asList(artistsList));


        adapter = new ArrayAdapter<>(getActivity(), R.layout.artist_search_result, R.id.artistTV, R.id.artist_Image, artist);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
        mListView.setTextFilterEnabled(true);
        setUpSearchView();
        return view;
    }

    private void setUpSearchView() {
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setOnQueryTextFocusChangeListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint(getActivity().getResources().getString(R.string.search_artist));
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (TextUtils.isEmpty(query)) {
            mListView.clearTextFilter();
        } else {
            mListView.setFilterText(query.toString());
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), Top10TrackActivity.class);
        startActivity(intent);
    }

    public class FetchArtistTask extends AsyncTask<Void, Void, List<Artist>> {
        @Override
        protected List<Artist> doInBackground(Void... params) {
            //        Connect to the Spotify api with the wrapper
            SpotifyApi api = new SpotifyApi();

            //        Create a SpotifyService object that we can use to get desired data.
            SpotifyService spotify = api.getService();

            ArtistsPager results = spotify.searchArtists("Beyonce");
            List<Artist> artists = results.artists.items;
            int imageHeight = 64, imageWidth = 64;
            String url = null;
            for (int i = 0; i < artists.size(); i++) {
                Artist artist = artists.get(i);

                for (int j = 0; j < artist.images.size(); j++) {
                    if ((imageHeight == artist.images.get(j).height)
                            && (imageWidth == artist.images.get(j).width)) {
                        url = artist.images.get(j).url;
                    }
                }

                Log.i(LOG_TAG, i + " " + artist.name + " " + url);
            }


            return artists;
        }

        @Override
        protected void onPostExecute(List<Artist> artists) {
            if (artists != null) {
                adapter.clear();
                for (int i = 0; i < artists.size(); i++) {
                    adapter.add(artists.get(i).name);
                }
            }
        }
    }
}
