package com.garg.meha.app.spotify;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;

/**
 * Created by meha on 7/4/15.
 */
public class SpotifyListAdapter extends BaseAdapter {

    private Activity context;
    private List<Artist> artists;

    public SpotifyListAdapter(Activity context, List<Artist> artists) {
        this.context = context;
        this.artists = artists;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.artist_search_list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
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

        return convertView;
    }

    public static class ViewHolder{
        public  final ImageView image;
        public final TextView artistListItemText;
        public ViewHolder(View view) {
            image = (ImageView) view.findViewById(R.id.artist_Image);
            artistListItemText = (TextView) view.findViewById(R.id.artistTV);
        }

    }
}
