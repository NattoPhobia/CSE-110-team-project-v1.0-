package com.example.awong.musicplayer1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.location.Location;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.Calendar;
import java.util.List;
import com.example.awong.musicplayer1.MenuActivity;

/**
 * Created by jeremyjiang on 2/18/18.
 */

public class ListPlayer {
    List<String> listOfSongTitles;
    int count = 0;
    MediaPlayer mediaPlayer;
    Context context;


    public ListPlayer (List<String> listOfSongTitles, Context context) {
        this.listOfSongTitles = listOfSongTitles;
        this.context = context;
    }

    public void playCurrentList() {
        if (MenuActivity.musicPlayer != null && MenuActivity.musicPlayer.getMediaPlayer() != null){
            if (MenuActivity.musicPlayer.getMediaPlayer().isPlaying()) {
                MenuActivity.musicPlayer.getMediaPlayer().stop();

            }
        }



        mediaPlayer = new MediaPlayer();

        if(MenuActivity.musicPlayer != null) { //
            MenuActivity.musicPlayer.setMediaPlayer(mediaPlayer);
            String songTitle = listOfSongTitles.get(count);
            MenuActivity.musicPlayer.setSongTitle(songTitle);
            Song songObject = MenuActivity.songMap.get(songTitle);
            MainActivity.fileName = songObject.getFileName();
            MenuActivity.musicPlayer.setCurrentSong(songObject.getFileName());

            while (songObject.getPriority() == 0 && count < listOfSongTitles.size()-1) {
                count++;
                MenuActivity.musicPlayer.setMediaPlayer(mediaPlayer);
                songTitle = listOfSongTitles.get(count);
                MenuActivity.musicPlayer.setSongTitle(songTitle);
                songObject = MenuActivity.songMap.get(songTitle);
                MainActivity.fileName = songObject.getFileName();
                MenuActivity.musicPlayer.setCurrentSong(songObject.getFileName());
            }

            if(songObject.getPriority() == 0 &&
                    songObject == MenuActivity.songMap.get(listOfSongTitles.get(listOfSongTitles.size()-1))){
                MenuActivity.musicPlayer.getMediaPlayer().pause();
            }

            int resourceId = getSongId(count);

            AssetFileDescriptor assetFileDescriptor = context.getResources().openRawResourceFd(resourceId);
            try {
                MenuActivity.musicPlayer.getMediaPlayer().setDataSource(assetFileDescriptor);
                MenuActivity.musicPlayer.getMediaPlayer().prepare();
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            MenuActivity.musicPlayer.getMediaPlayer().start();
            Calendar currentCalendar = Calendar.getInstance();
            Song currentSongObj = MenuActivity.songMap.get(songTitle);
            currentSongObj.setCal(currentCalendar);

            MenuActivity.gps = new GPSTracker(context);

            // check if GPS enabled
            if(MenuActivity.gps.canGetLocation()){

                Location loc = MenuActivity.gps.getLocation();
                currentSongObj.setLocation(loc);
                // \n is for new line
            }else{
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                MenuActivity.gps.showSettingsAlert();
            }
            count++;


            MenuActivity.musicPlayer.getMediaPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (count < listOfSongTitles.size()) {
                        mp.stop();
                        mp.reset();

                        System.out.println("count     " + count);


                        MenuActivity.musicPlayer.setMediaPlayer(mediaPlayer);
                        String songTitle = listOfSongTitles.get(count);
                        MenuActivity.musicPlayer.setSongTitle(songTitle);
                        Song songObject = MenuActivity.songMap.get(songTitle);
                        MainActivity.fileName = songObject.getFileName();
                        MenuActivity.musicPlayer.setCurrentSong(songObject.getFileName());

                        while (songObject.getPriority() == 0 && count < listOfSongTitles.size()-1) {
                            count++;
                            MenuActivity.musicPlayer.setMediaPlayer(mediaPlayer);
                            songTitle = listOfSongTitles.get(count);
                            MenuActivity.musicPlayer.setSongTitle(songTitle);
                            songObject = MenuActivity.songMap.get(songTitle);
                            MainActivity.fileName = songObject.getFileName();
                            MenuActivity.musicPlayer.setCurrentSong(songObject.getFileName());
                        }

                        if(songObject.getPriority() == 0 &&
                                songObject == MenuActivity.songMap.get(listOfSongTitles.get(listOfSongTitles.size()-1))){
                            System.out.println("LAST SONG IS PLAYING");
                            mp.pause();
                        }

                        int resourceId = getSongId(count);
                        System.out.println("res     " + resourceId);

                        AssetFileDescriptor assetFileDescriptor = context.getResources().openRawResourceFd(resourceId);
                        try {
                            mp.setDataSource(assetFileDescriptor);
                            mp.prepare();
                        } catch (Exception e) {
                            System.out.println(e.toString());
                        }

                        mp.start();

                        Calendar currentCalendar = Calendar.getInstance();
                        Song currentSongObj = MenuActivity.songMap.get(songTitle);
                        currentSongObj.setCal(currentCalendar);

                        MenuActivity.gps = new GPSTracker(context);

                        // check if GPS enabled
                        if(MenuActivity.gps.canGetLocation()){

                            Location loc = MenuActivity.gps.getLocation();
                            currentSongObj.setLocation(loc);
                            // \n is for new line
                        }else{
                            // can't get location
                            // GPS or Network is not enabled
                            // Ask user to enable GPS/network in settings
                            MenuActivity.gps.showSettingsAlert();
                        }



                        //USED FOR SETTING PROGRESS IN MUSIC ACTIVITY FOR EACH SONG
                        MusicActivity.totalTime = MenuActivity.musicPlayer.getMediaPlayer().getDuration();
                        if (MusicActivity.positionBar != null) {
                            MusicActivity.positionBar.setMax(MenuActivity.musicPlayer.getMediaPlayer().getDuration());
                        }
                        //USED FOR SETTING PROGRESS IN MUSIC ACTIVITY FOR EACH SONG

                        count++;
                    }
                }
            });
        } //

    }


    public int getSongId(int count) {
        String songTitle = listOfSongTitles.get(count);
        Song song = MenuActivity.songMap.get(songTitle);
        String songName = song.getFileName();
        int song_id = context.getResources().getIdentifier(songName, "raw",
                context.getPackageName());
        return song_id;
    }

}

