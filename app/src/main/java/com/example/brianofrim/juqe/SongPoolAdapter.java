


package com.example.brianofrim.juqe;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jferris on 28/01/17.
 */

public class SongPoolAdapter extends ArrayAdapter<Song> {

    private Context context;
    private ArrayList<Song> songList = new ArrayList<>();


    public SongPoolAdapter(Context context, ArrayList<Song> songList) {
        super(context, 0, songList);
        this.context = context;
        this.songList = songList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.pool_list_item_dj, parent, false);
        }
        TextView songName = (TextView) convertView.findViewById(R.id.songName_dj_pool);
        TextView songArtist = (TextView) convertView.findViewById(R.id.songArtist_dj_pool);
        final Button removeFromPoolButton = (Button) convertView.findViewById(R.id.remove_from_pool);
        removeFromPoolButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                VenueController.removeURI(songList.get(position).getURI());
                VenueController.removeSong(songList.get(position));
                //removeFromPoolButton.setEnabled(false);
                notifyDataSetChanged();
            }
            //RelativeLayout listItem = (RelativeLayout) v.getParent();
            //listItem.setBackgroundColor(ContextCompat.getColor(context, R.color.
        });

        Song song = getItem(position);
        //Set color for completion

        songName.setText(song.getName());
        songArtist.setText(song.getArtist());
        return convertView;

    }
}