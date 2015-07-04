package com.garg.meha.app.spotify;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class Top10TrackActivityFragment extends Fragment {

    ListView mListView;

    public Top10TrackActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top10_track, container, false);

        mListView = (ListView) view.findViewById(R.id.top10_listView);

        String[] artistsList = {"Rihanna", "Adele", "John Lennon", "SP Balasubramaniam", "Jennifer Lopez",
                "Justin Timberlake", "Rihanna", "Adele", "John Lennon", "SP Balasubramaniam", "Jennifer Lopez", "Justin Timberlake"};
        ArrayList<String> artist = new ArrayList<>(Arrays.asList(artistsList));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.artist_top10_list_item, R.id.artistTV_top10, artist);
        mListView.setAdapter(adapter);

        return view;
    }
}
