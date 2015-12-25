package com.garg.meha.app.spotify;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.garg.meha.app.spotify.model.TrackDto;
import com.garg.meha.app.spotify.model.TrackObj;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

/**
 * Created by meha on 7/4/15.
 */
public class Top10ListAdapter extends BaseAdapter {

    private Activity context;
    private Tracks results;
    private List<TrackObj> trackObjList = new LinkedList<>();
    private boolean mIsLargeLayout;

    public Top10ListAdapter(Activity context, Tracks results, Boolean mIsLargeLayout) {
        this.context = context;
        this.results = results;
        this.mIsLargeLayout = mIsLargeLayout;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
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

                v.setSelected(true);
                v.setBackgroundColor(context.getResources().getColor(R.color.green));
//                SharedPreferences mPrefs = context.getPreferences(Context.MODE_PRIVATE);
//                SharedPreferences.Editor prefsEditor = mPrefs.edit();
//                Gson gson = new Gson();
//                String json = gson.toJson(results);
//                prefsEditor.putString("trackList", json);
//                prefsEditor.commit();

                List<TrackObj> allTracks = addTrackToList(results);
                TrackDto trackDto = new TrackDto(
                        track.name,
                        track.duration_ms,
                        track.album.name,
                        track.album.images.get(0).url,
                        track.preview_url,
                        position, allTracks);


                Intent intent = new Intent(context, MediaPlayerFragment.class);
                intent.putExtra("track",
                        new TrackDto(
                                track.name,
                                track.duration_ms,
                                track.album.name,
                                track.album.images.get(0).url,
                                track.preview_url,
                                position, allTracks));
                context.startActivity(intent);

                // for DialogFragment
//                showDialog(trackDto);

//                TrackDto trackDto = new TrackDto(track.name,
//                                track.duration_ms,
//                                track.album.name,
//                                track.album.images.get(0).url,
//                                track.preview_url,
//                                position);
            }
        });

        return convertView;
    }

    private void showDialog(TrackDto trackDto) {
        FragmentManager fragmentManager = context.getFragmentManager();
        CustomDialogFrag newFragment = CustomDialogFrag.newInstance(trackDto);


        if (mIsLargeLayout) {
            // The device is using a large layout, so show the fragment as a dialog
            newFragment.show(fragmentManager, "dialog");
        } else {
            // The device is smaller, so show the fragment fullscreen
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            // For a little polish, specify a transition animation
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            // To make it fullscreen, use the 'content' root view as the container
            // for the fragment, which is always the root view for the activity
            transaction.add(R.id.fragment_container_track_player, newFragment)
                    .addToBackStack(null).commit();
        }

    }

    private List<TrackObj> addTrackToList(Tracks results) {
        for (Track track : results.tracks) {
            TrackObj trackObj = new TrackObj();
            trackObj.setAlbumName(track.album.name);
            trackObj.setArtistArtWorkUrl(track.album.images.get(0).url);
            trackObj.setDuration_ms(track.duration_ms);
            trackObj.setTrackName(track.name);
            trackObj.setPreview_url(track.preview_url);
            trackObjList.add(trackObj);
        }
        return trackObjList;
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


