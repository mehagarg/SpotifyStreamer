package com.garg.meha.app.spotify;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.garg.meha.app.spotify.model.ArtistDto;
import com.garg.meha.app.spotify.model.TrackDto;
import com.squareup.picasso.Picasso;

import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

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
        } else {
            holder = (Holder) convertView.getTag();
        }

        String url = null;
        int imageHeight = 64, imageWidth = 64;
        final Track track = results.tracks.get(position);
        holder.top10_album.setText(track.name);
        holder.top10_track.setText(track.album.name);
        for (int j = 0; j < track.album.images.size(); j++) {
            if ((track.album.images.get(j).height!=imageHeight)
                    && (track.album.images.get(j).width!=imageWidth)) {
                url = track.album.images.get(0).url;
                Picasso.with(context).load(url).resize(imageWidth, imageHeight).into(holder.image);
            }
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayTrackActivity.class);
                intent.putExtra("track",
                        new TrackDto(
                                track.name,
                                track.duration_ms,
                                track.album.name,
                                track.album.images.get(0).url,
                                track.preview_url));
                context.startActivity(intent);
            }
        });

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


