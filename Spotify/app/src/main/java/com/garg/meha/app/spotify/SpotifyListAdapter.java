package com.garg.meha.app.spotify;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by meha on 7/4/15.
 */
public class SpotifyListAdapter extends BaseAdapter {

    private Activity context;
    private List<Artist> artists;
    private String spotifyID = null;
    private MainActivityFragment.OnAlbumSelectedListener mCallBack;

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
            if ((artist.images.get(0).height!=imageHeight)
                    && (artist.images.get(0).width!=imageWidth)) {
                url = artist.images.get(j).url;
                Picasso.with(context).load(url).resize(imageWidth, imageHeight).into(holder.image);
            }
        }
        spotifyID = artist.id;
        return convertView;
    }

    public static class ViewHolder {
        public final ImageView image;
        public final TextView artistListItemText;

        public ViewHolder(View view) {
            image = (ImageView) view.findViewById(R.id.artist_Image);
            artistListItemText = (TextView) view.findViewById(R.id.artistTV);
        }
    }

}
