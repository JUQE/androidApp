package com.example.brianofrim.juqe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jferris on 28/01/17.
 */

public class Venue {
    private String songListName;
    private String name;
    private String code;
    private Song nowPlaying;

    public Venue (String name, String code) {
        this.songListName = code + "_sl";
        this.name = name;
        this.code = code;
    }

    public Venue () {
    }

    public String getSongListName() {
        return songListName;
    }

    public void setSongListName(String songListName) {
        this.songListName = songListName;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }



}
