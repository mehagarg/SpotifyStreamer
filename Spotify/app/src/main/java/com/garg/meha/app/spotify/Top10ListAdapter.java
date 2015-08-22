package com.garg.meha.app.spotify;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
public class Top10ListAdapter extends BaseAdapter {

    private Activity context;
    private Tracks results;

    public Top10ListAdapter(Activity context, Tracks results) {
        this.context = context;
        this.results = results;
    }

    @Override
    public int getCount() {
        return results.tracks.size();
    }

    @Override
    public Track getItem(int position) {
        return results.tracks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.artist_top10_list_item, null);
            holder = new Holder(convertView);
            convertView.setTag(holder);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // do something related to playing the track
                }
            });
        } else {
            holder = (Holder) convertView.getTag();
        }

        String url = null;
        int imageHeight = 64, imageWidth = 64;
        Track track = results.tracks.get(position);
        holder.top10_album.setText(track.name);
        holder.top10_track.setText(track.album.name);
        for (int j = 0; j < track.album.images.size(); j++) {
            if ((track.album.images.get(0).height!=imageHeight)
                    && (track.album.images.get(0).width!=imageWidth)) {
                url = track.album.images.get(0).url;
                Picasso.with(context).load(url).resize(imageWidth, imageHeight).into(holder.image);
            }
        }

        return convertView;
    }
}

class Holder {
    public final ImageView image;
    public final TextView top10_track;
    public final TextView top10_album;

    public Holder(View view) {
        image = (ImageView) view.findViewById(R.id.artist_Image_top10);
        top10_track = (TextView) view.findViewById(R.id.artistTrack_top10);
        top10_album = (TextView) view.findViewById(R.id.artistAlbum_top10);
    }

}


