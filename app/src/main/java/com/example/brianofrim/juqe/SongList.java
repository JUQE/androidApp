package com.example.brianofrim.juqe;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SNIHostName;

/**
 * Created by brianofrim on 2017-01-28.
 */

public class SongList {


    private String venueCode;
    private List<Song> songPool;
    private Song nowPlaying;
    private Song onDeck;

    SongList(){
    }

    SongList(String venueN){
        this.venueCode = venueN;
        this.songPool = new ArrayList<Song>();
    }

    public List<Song> getSongPool() {
        return songPool;
    }


    public void setSongPool(List<Song> songPool) {
        this.songPool = songPool;
    }

    public String getVenueName() {
        return venueCode;
    }

    public void setVenueName(String venueName) {
        this.venueCode = venueName;
    }

    public void addSong(Song s){
        songPool.add(s);
    }

    public void removeSong(Song s){
        for(Song song: songPool){
            if(s.getURI().equals(song.getURI())){
                songPool.remove(song);
                break;
            }
        }
    }

    public Song getOnDeck() {
        return onDeck;
    }

    public void setOnDeck(Song onDeck) {
        this.onDeck = onDeck;
    }

    public Song getNowPlaying() {
        return nowPlaying;
    }

    public void setNowPlaying(Song nowPlaying) {
        this.nowPlaying = nowPlaying;
    }


}
