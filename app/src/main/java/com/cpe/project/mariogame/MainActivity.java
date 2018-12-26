package com.cpe.project.mariogame;

import android.content.pm.ActivityInfo;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    GameView gameView;


    MediaPlayer mediaPlayer;
    static SoundPool soundPool;
    static ArrayList<Integer> soundsID;

    static boolean ready = false;
    static float vol, maxVol;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        soundsID = new ArrayList<>();
        gameView = new GameView(this);
        mediaPlayer = MediaPlayer.create(this, R.raw.world_sound);

        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 100);
        soundPool.setRate(1,-2.0f);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                ready = true;
            }
        });


        setContentView(gameView);
        soundsID.add(soundPool.load(getApplicationContext(), R.raw.smb_mariodie, 1));
        soundsID.add(soundPool.load(getApplicationContext(), R.raw.smb3_coin, 1));
        soundsID.add(soundPool.load(getApplicationContext(), R.raw.smb3_jump, 1));
        soundsID.add(soundPool.load(getApplicationContext(), R.raw.smb3_lost_suit, 1));
        soundsID.add(soundPool.load(getApplicationContext(), R.raw.smb_breakblock, 1));
        soundsID.add(soundPool.load(getApplicationContext(), R.raw.smb3_tail, 1));
        soundsID.add(soundPool.load(getApplicationContext(), R.raw.smb3_level_clear, 1));
    }

    public static void playSound(int id) {
        if (ready) {
            soundPool.play(soundsID.get(id),1, 1 , 1, 0, 1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        gameView.pause();
        if (mediaPlayer != null)
            mediaPlayer.release();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gameView.destroy();
        if (mediaPlayer != null)
            mediaPlayer.release();
    }
}
