package com.garg.meha.app.spotify;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.QueryMap;

/**
 * Created by meha on 7/4/15.
 */
public class SpotifyListAdapter extends BaseAdapter {

    private Activity context;
    private List<Artist> artists;
    private String spotifyID = null;
    private MainActivityFragment.OnAlbumSelectedListener mCallBack;

    final String COUNTRY_PARAM = "country";

    public SpotifyListAdapter(Activity context, List<Artist> artists, MainActivityFragment.OnAlbumSelectedListener mCallback) {
        this.context = context;
        this.artists = artists;
        this.mCallBack = mCallback;
    }

    @Override
    public int getCount() {
        return artists.size();
    }

    @Override
    public Artist getItem(int position) {
        return artists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.artist_search_list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    startSpotifyServiceForAlbum(spotifyID);

                    mCallBack.onAlbumSelected(spotifyID);
                }
            });
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        int imageHeight = 64, imageWidth = 64;
        String url = null;
        Artist artist = artists.get(position);
        holder.artistListItemText.setText(artist.name);
        for (int j = 0; j < artist.images.size(); j++) {
            if ((imageHeight == artist.images.get(j).height)
                    && (imageWidth == artist.images.get(j).width)) {
                url = artist.images.get(j).url;
                Picasso.with(context).load(url).into(holder.image);
            }
        }
        spotifyID = artist.id;

        return convertView;
    }

    private void startSpotifyServiceForAlbum(String spotifyID) {
        FetchAlbumTask fetchAlbumTask = new FetchAlbumTask();
        fetchAlbumTask.execute(spotifyID);
    }

    public static class ViewHolder {
        public final ImageView image;
        public final TextView artistListItemText;

        public ViewHolder(View view) {
            image = (ImageView) view.findViewById(R.id.artist_Image);
            artistListItemText = (TextView) view.findViewById(R.id.artistTV);
        }

    }

    private class FetchAlbumTask extends AsyncTask<String, Void, List<Track>> {
        private ProgressDialog dialog = null;

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
                    Log.v("Spotify", "success");
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    ArrayList<Track> tracks = new ArrayList<Track>(results.tracks);
                    Log.d("String of Track", tracks.toString());
                    for (int i = 0; i < results.tracks.size(); i++) {
                        Log.d("Results from Top 10", tracks.get(i).album.name);
                        Log.d("Results from Tracks", tracks.get(i).name);
                        Log.d("Results from Images Url", tracks.get(i).album.images.get(0).url);
                    }
                    Toast.makeText(context, "Presenting list of songs for this item", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(context, Top10TrackActivity.class);
//                    intent.putExtra("track", tracks.toString());
//                    context.startActivity(intent);
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.v("Spotify", "failure");
                }
            });

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.setMessage("Downloading top 10 tracks ...");
            dialog.show();
        }
    }

}
