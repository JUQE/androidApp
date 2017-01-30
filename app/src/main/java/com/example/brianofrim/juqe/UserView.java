package com.example.brianofrim.juqe;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class UserView extends Activity {
    private ArrayList<Song> songArray = new ArrayList<>();
    private SongListAdapter songAdapter;
    private ListView songList;
    private String code;
    private DatabaseReference mDatabase;
    private ImageView imageView;
    private Song nowPlaying;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_view);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            code = extras.getString("code");
        }

        songList = (ListView) findViewById(R.id.allSongs);
        final TextView songName = (TextView) findViewById(R.id.songName);
        final TextView songArtist = (TextView) findViewById(R.id.songArtist);
        songList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            }
        });


        final ValueEventListener nowPlayingListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    nowPlaying = dataSnapshot.getValue(Song.class);
                }

                imageView = (ImageView) findViewById(R.id.albumImage);

                if (imageView != null && nowPlaying != null) {
                    new ImageDownloaderTask(imageView).execute(nowPlaying.getAlbumArt());
                    songName.setText(nowPlaying.getName());
                    songArtist.setText(nowPlaying.getArtist());
                }
                songAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        VenueController.getDbRef().child("songLists").child(code).child("nowPlaying")
                .addValueEventListener(nowPlayingListener);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                // A new comment has been added, add it to the displayed list
                Song song = dataSnapshot.getValue(Song.class);
                //song.setHash(dataSnapshot.getKey());
                songAdapter.add(song);
                Collections.sort(songArray);
                songAdapter.update();
                // ...
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                Song song = dataSnapshot.getValue(Song.class);
                for(Song s: songArray) {
                    if(s.getURI().equals(song.getURI())) {
                        s.setVotes(song.getVotes());
                        break;
                    }
                }
                Collections.sort(songArray);
                songAdapter.update();
                // ...
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                Song song = dataSnapshot.getValue(Song.class);
                songAdapter.removeByURI(song);
                Collections.sort(songArray);
                songAdapter.update();

                // ...
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.

                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.child("songLists").child(code).child("songPool").addChildEventListener(childEventListener);


        }


    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
//        Song newSong = new Song("11", "The Killers", "aadghghj41203498", 11);
//        songArray.add(newSong);
//        newSong = new Song("100", "The Killers", "aadghghj41203498", 100);
//        songArray.add(newSong);
//        newSong = new Song("0", "The Killers", "aadghghj41203498", 0);
//        songArray.add(newSong);
//        newSong = new Song("10", "The Killers", "aadghghj41203498", 10);
//        songArray.add(newSong);
        Collections.sort(songArray);
        songAdapter = new SongListAdapter(this, songArray);
        songList.setAdapter(songAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
