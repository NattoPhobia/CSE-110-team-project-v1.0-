package com.example.awong.musicplayer1;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.location.Location;
import android.media.MediaMetadataRetriever;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by parkn on 2/11/2018.
 */

public class Song {
    private Location location;
    private Calendar cal;

    private int priority;
    private int randNum;  //used for priority_queue sorting
    private String fileName;
    private String songTitle;
    private String albumName;
    private String artist;
    private byte[] albumArt;

    //default constructor
    public Song(){
        this.location = null;
        this.cal = null;

        this.priority = 1;
        this.randNum = 0;
        this.fileName = "Unknown";
        this.songTitle = "Unknown";
        this.albumName = "Unknown";
        this.artist = "Unknown";
    }

    public Song(String fileName, String songTitle, String album, String artist, byte[] albumArt) {
        this.location = null;
        this.cal = null;

        this.priority = 1;
        this.randNum = 0;
        this.fileName = fileName;
        this.songTitle = songTitle;
        this.albumName = album;
        this.artist = artist;
        this.albumArt = albumArt;
    }

    public void setLocation(Location loc) { this.location = loc;}

    public void setCal(Calendar c) { this.cal = c;}

    public Calendar getCal() {
        return this.cal;
    }

    public Location getLocation() {
        return location;
    }

    // set the priority lv ranging from 0(disliked) to 2(favorite)
    public void setPriority(int lv){
        this.priority = lv;
    }

    //getter for priority lv
    public int getPriority(){
        return this.priority;
    }

    public void setRandNum(int rand){
        this.randNum = rand;
    }

    public int getRandNum(){
        return this.randNum;
    }

    public String getFileName(){ return this.fileName;}

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSongTitle() {
        return this.songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getArtist() {
        return this.artist;
    }

    public String getAlbum() {
        return this.albumName;
    }

    public byte[] getAlbumArt() {
        return this.albumArt;
    }


    /*
    public void setSongTitle(Context context, String fileName) {
        int song_id = context.getResources().getIdentifier(fileName, "raw", context.getPackageName());
        final AssetFileDescriptor afd = context.getResources().openRawResourceFd(song_id);
        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
        metaRetriever.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        String newSongTitle = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        this.songTitle = newSongTitle;
    }
    */

}
