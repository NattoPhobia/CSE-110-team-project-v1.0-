

//Anthony changed2
//Shenghao change 222

package com.example.awong.musicplayer1;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.example.awong.musicplayer1.Album;
import com.example.awong.musicplayer1.AlbumActivity;
import com.example.awong.musicplayer1.MainActivity;
import com.example.awong.musicplayer1.MusicPlayer;
import com.example.awong.musicplayer1.R;
import com.example.awong.musicplayer1.Song;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class MenuActivity extends AppCompatActivity {
    protected static List<String> listTitles;   //for display purpose
    protected static List<String> listFileNames;
    protected static List<String> albumList;
    protected static HashMap<String, Album> albumMap;
    protected static HashMap<String, Song> songMap;  //Map that stores all song objects
    protected static MusicPlayer musicPlayer;

    private static final int REQUEST_CODE_PERMISSION = 2;
    private static String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String TAG = "MenuActivity";

    protected static GPSTracker gps;

    protected static boolean flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission}, REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition will
                //execute every time, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "onCreate: User deny permission for location acess!");
        }

        Button albums = (Button) findViewById(R.id.albums);
        Button songs = (Button) findViewById(R.id.songs);
        ToggleButton flashback = (ToggleButton) findViewById(R.id.flashback);


        //Asking permission for Location services.
        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != MockPackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{mPermission}, REQUEST_CODE_PERMISSION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "onCreate: User denies permission");
        }


        songs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this, MainActivity.class);
                Log.d(TAG, "onClick: User clicks the SONGS button");
                MenuActivity.this.startActivity(i);
            }
        });

        albums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this, AlbumActivity.class);
                Log.d(TAG, "onClick: User clicks the ALBUMS button");
                MenuActivity.this.startActivity(i);
            }
        });

        flashback.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d(TAG, "onClick: User clicks the FLASHBACK button");
                if (b) {  //Flashback mode
                    gps = new GPSTracker(MenuActivity.this);

                    FlashbackPlayer fbp = new FlashbackPlayer(gps.getLocation(), songMap);
                    //populate the flashback playlist

                    //fbp.populatePlaylist();


                    PriorityQueue<Song> playlist = fbp.getPlaylist();

                    fbp.populatePlaylist();

                    if (playlist.isEmpty()) {
                        Log.d(TAG, "onCheckedChanged: Flashback playlist is empty!");
                        Toast.makeText(MenuActivity.this, "No songs have ever been played here before!", Toast.LENGTH_SHORT).show();
                    } else {
                        List<String> listOfSongs = new ArrayList<String>();
                        System.out.println("playlist length :   " + playlist.size());
                        while (!playlist.isEmpty()) {
                            Song temp = playlist.poll();
                            listOfSongs.add(temp.getSongTitle());
                        }

                        System.out.println("list of songs length:   " + listOfSongs.size());
                        ListPlayer lp = new ListPlayer(listOfSongs, MenuActivity.this);
                        lp.playCurrentList();
                        Intent i = new Intent(MenuActivity.this, MusicActivity.class);
                        i.putExtra("songName", musicPlayer.getSongTitle());
                        MenuActivity.this.startActivity(i);
                    }

                } else {  //Normal mode
                    //destroy the musicplayer
                    if (musicPlayer.getMediaPlayer() != null && musicPlayer.getMediaPlayer().isPlaying()) {
                        musicPlayer.getMediaPlayer().stop();
                    }
                }
            }
        });


        if (!flag) {
            musicPlayer = new MusicPlayer();
            listTitles = new ArrayList<>();
            listFileNames = new ArrayList<>();
            albumList = new ArrayList<>();
            albumMap = new HashMap<>();
            songMap = new HashMap<>();

            Field[] fields = R.raw.class.getFields();
            for (int i = 0; i < fields.length; i++) {
                String fileName = fields[i].getName();
                listFileNames.add(fileName);

                // Get albumName and songTitle
                int song_id = this.getResources().getIdentifier(fileName, "raw", this.getPackageName());
                final AssetFileDescriptor afd = getResources().openRawResourceFd(song_id);
                MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
                metaRetriever.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                String albumName = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
                String songTitle = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                String artist = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                byte[] albumArt = metaRetriever.getEmbeddedPicture();


                //Populating albums and songs in albums
                if (!albumList.contains(albumName)) {
                    albumList.add(albumName);
                    Album newAlbum = new Album(albumName);
                    newAlbum.addSong(fileName);
                    newAlbum.addSongTitle(songTitle);
                    albumMap.put(albumName, newAlbum);
                } else {
                    albumMap.get(albumName).addSong(fileName);
                    albumMap.get(albumName).addSongTitle(songTitle);
                }

                //Populating songs in song map
                Song newSong = new Song(fileName, songTitle, albumName, artist, albumArt);
                songMap.put(songTitle, newSong);


                flag = true;
                listTitles.add(songTitle);
            }


        }

    }
}
