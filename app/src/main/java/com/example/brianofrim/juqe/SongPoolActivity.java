package com.example.brianofrim.juqe;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;

import java.util.ArrayList;

public class SongPoolActivity extends Activity {

    //UI elements
    private ListView songPoolLV;
    private SongPoolAdapter poolAdapter;
    private Song nowPlaying;
    ImageView imageView;
    int start = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_pool2);
        songPoolLV = (ListView) findViewById(R.id.songPoolListView);
        final TextView songName = (TextView) findViewById(R.id.songName);
        final TextView songArtist = (TextView) findViewById(R.id.songArtist);


        final ValueEventListener nowPlayingListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    nowPlaying = dataSnapshot.getValue(Song.class);
                    VenueController.removeSong(nowPlaying);
                }

                imageView = (ImageView) findViewById(R.id.albumImage);

                if (imageView != null && nowPlaying != null) {
                    new ImageDownloaderTask(imageView).execute(nowPlaying.getAlbumArt());
                    songName.setText(nowPlaying.getName());
                    songArtist.setText(nowPlaying.getArtist());
                }

                poolAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        VenueController.getDbRef().child("songLists").child(VenueController.getCurrVenue().getCode()).child("nowPlaying")
                .addValueEventListener(nowPlayingListener);

        ImageButton playButton = (ImageButton) findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (start == 0) {
                    start++;
                    VenueController.nextTrack();
                } else {
                    playerController.playPauseToggle();
                }
            }
        });

        ImageButton skipButton = (ImageButton) findViewById(R.id.skipButton);
        skipButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VenueController.nextTrack();
            }
        });
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        poolAdapter = new SongPoolAdapter(this, (ArrayList<Song>) VenueController.getCurrSongList().getSongPool());
        songPoolLV.setAdapter(poolAdapter);
    }

    public static void update() {

    }
}
