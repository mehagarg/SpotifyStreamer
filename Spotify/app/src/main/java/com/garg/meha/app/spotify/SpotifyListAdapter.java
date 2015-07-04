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

import kaaes.spotify.webapi.android.models.Artists;

/**
 * Created by meha on 7/4/15.
 */
public class SpotifyListAdapter extends BaseAdapter {

    private Activity context;
    private List<Artists> artists;
    public SpotifyListAdapter(Activity context, int resourceId, List<Artists> artists) {
        this.context = context;
        this.artists = artists;
    }

    @Override
    public int getCount() {
        return artists.size();
    }

    @Override
    public Artists getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.artist_search_result, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").into(holder.image);
        holder.artistListItemText.setText(artists.get(position).getClass().getName());

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
