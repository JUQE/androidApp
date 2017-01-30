package com.example.brianofrim.juqe;

import android.os.Debug;
import android.provider.Settings;
import android.util.Log;
import com.spotify.sdk.android.player.Error;

import com.spotify.sdk.android.player.PlaybackState;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;

/**
 * Created by brianofrim on 2017-01-29.
 */

public class playerController{
    private static Player playa;
    private PlaybackState mCurrentPlaybackState;

    public static void setPlayer(Player player){
        playa = player;

    }

    public static Player getPlayer(){
        return  playa;
    }


    public static void playPauseToggle(){
        if (getPlayer().getPlaybackState() != null && getPlayer().getPlaybackState().isPlaying) {
            PausePlayer();
        }else{
            ResumeTrack();
        }
    }


    public static void PausePlayer(){
        playa.pause(mOperationCallback);
    }

    public static void ResumeTrack(){
        playa.resume(mOperationCallback);
    }

//    public static void Enqueue(){
//        playa.a
//    };

    private static final Player.OperationCallback mOperationCallback = new Player.OperationCallback() {
        @Override
        public void onSuccess() {
            logMessage("success");

        }

        @Override
        public void onError(Error error)  {
            logMessage("fail");
        }

    };


//    @Override
//    public void onPlaybackError(Error error) {
//        logMessage("Err: " + error);
//    }

    private static void logMessage(String logStr){
        Log.d("MainActivity",logStr);
    }




}
