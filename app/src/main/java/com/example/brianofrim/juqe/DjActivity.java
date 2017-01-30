package com.example.brianofrim.juqe;



import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
import android.support.v7.util.SortedList;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.spotify.sdk.android.authentication.AuthenticationClient;
        import com.spotify.sdk.android.authentication.AuthenticationRequest;
        import com.spotify.sdk.android.authentication.AuthenticationResponse;
        import com.spotify.sdk.android.player.Config;
        import com.spotify.sdk.android.player.ConnectionStateCallback;
        import com.spotify.sdk.android.player.Error;
        import com.spotify.sdk.android.player.Player;
        import com.spotify.sdk.android.player.PlayerEvent;
        import com.spotify.sdk.android.player.Spotify;
        import com.spotify.sdk.android.player.SpotifyPlayer;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.AlbumsPager;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class DjActivity extends Activity implements
        SpotifyPlayer.NotificationCallback, ConnectionStateCallback
{

    // TODO: Replace with your client ID
    private static final String CLIENT_ID = "7c30472359f34bd5963914d4474fb2e0";
    // TODO: Replace with your redirect URI
    private static final String REDIRECT_URI = "juqe://callback";

    // Request code that will be used to verify if the result comes from correct activity
    // Can be any integer
    private static final int REQUEST_CODE = 1337;

    private Player mPlayer;

    //UI elements
    private ListView seachResultsList;
//    private Spinner searchTypeSpinner;
    private Button searchButton;
    private EditText searchText;
    private Button poolButton;

    private TextView venueNameTextBox;

    private ResultsListAdapter songAdapter;

    private ArrayList<Song> songSearchResultsArraylist;

    SpotifyApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dj);

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

      api =  new SpotifyApi();

        seachResultsList = (ListView) findViewById(R.id.searchResultsList);
        //searchTypeSpinner = (Spinner) findViewById(R.id.searchTypeSpinner);
        searchButton = (Button) findViewById(R.id.searchButton);
        searchText = (EditText) findViewById(R.id.searchTextBox);
        poolButton = (Button) findViewById(R.id.goToPoolButton);
        venueNameTextBox = (TextView) findViewById(R.id.BoothTextView);
        venueNameTextBox.setText(VenueController.getCurrVenue().getName());

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchSongs();

            }
        });

        poolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSongPool();

            }
        });

        songSearchResultsArraylist = new ArrayList<Song>();

        //test <code>
        //VenueController.createVenue("testVen3","V Cool place");
        //VenueController.addSong( new Song("some song", "lol", "sagrgeagerg", 0));

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.dj_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        //respond to menu item selection
//        if(item.getItemId() == R.id.pool_menu_item){
//            Intent intent = new Intent(this, SongPoolActivity.class);
//            startActivity(intent);
//        }
//        return true;
//    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        songAdapter = new ResultsListAdapter(this, songSearchResultsArraylist);
        seachResultsList.setAdapter(songAdapter);

        //VenueController.nextTrack();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            api.setAccessToken(response.getAccessToken());
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                    @Override
                    public void onInitialized(SpotifyPlayer spotifyPlayer) {
                        mPlayer = spotifyPlayer;
                        mPlayer.addConnectionStateCallback(DjActivity.this);
                        mPlayer.addNotificationCallback(DjActivity.this);
                        playerController.setPlayer(mPlayer);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        // VERY IMPORTANT! This must always be called or else you will leak resources
        //Spotify.destroyPlayer(this);
        super.onDestroy();
    }




    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d("MainActivity", "Playback event received: " + playerEvent.name());
//        switch (playerEvent) {
//            // Handle event type as necessary
//            default:
//                break;
//        }
        if(playerEvent.name().equals("kSpPlaybackNotifyTrackDelivered")){
            Log.d("MainActivity", "Track Changed");
            VenueController.nextTrack();
        }
    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d("MainActivity", "Playback error received: " + error.name());
        switch (error) {
            // Handle error type as necessary
            default:
                break;
        }
    }

    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");
       // mPlayer.playUri(null, "spotify:track:36lLAemjMocqp4XYr4Umgn", 0, 0);

    }

    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Error e) {
        Log.d("MainActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");

    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }

    public void searchSongs(){
        SpotifyService spotify = api.getService();

        spotify.searchTracks(searchText.getText().toString(), new Callback<TracksPager>() {
            @Override
            public void success(TracksPager tracksPager, Response response) {
                Log.d("Album success", tracksPager.toString());
                List<Track> tracks = tracksPager.tracks.items;
                songAdapter.clear();
                for(Track t: tracks) {
                    Song s = new Song(t.name, t.artists.get(0).name, t.id, 0, t.album.images.get(0).url);
                    songAdapter.add(s);
                }
                songAdapter.notifyDataSetChanged();

            }
            @Override
            public void failure(RetrofitError error) {
                Log.d("Album failure", error.toString());
            }
        });
//        spotify.searchAlbums("2dIGnmEIy1WZIcZCFSj6i8", new SortedList.Callback<AlbumsPager>() {
//            @Override
//            public void success(AlbumsPager albumpager, Response response) {
//                Albu
//                Log.d("Album success", albumpager.albums);
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                Log.d("Album failure", error.toString());
//            }
    }

    public void goToSongPool(){
        Intent intent = new Intent(this, SongPoolActivity.class);
        startActivity(intent);

    }
}
