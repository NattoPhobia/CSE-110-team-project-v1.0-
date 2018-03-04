package com.example.awong.musicplayer1;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AlbumActivity extends AppCompatActivity {
    ListView listView2;
    List<String> list;
    ListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        Button menuButton2 = (Button) findViewById(R.id.menuButton2);
        Button popBtnAlbum = (Button) findViewById(R.id.popupAlbum);

        if(MenuActivity.musicPlayer != null) {
            popBtnAlbum.setText(popBtnAlbum.getText() + " " + MenuActivity.musicPlayer.getSongTitle());
        }
        menuButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("AlbumActivity", "onClick: User clicks MENU button");
                Intent i = new Intent(AlbumActivity.this, MenuActivity.class);
                AlbumActivity.this.startActivity(i);
            }
        });

        popBtnAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MenuActivity.musicPlayer.getCurrentSong().equals("")) {
                    Log.d("AlbumActivity", "onClick: User clicks Currently Playing:");
                    String songTitle = MenuActivity.musicPlayer.getSongTitle();
                    String fileName = MenuActivity.musicPlayer.getCurrentSong();

                    MenuActivity.musicPlayer.popUp(fileName, songTitle, AlbumActivity.this, MusicActivity.class);
                }
            }
        });

        listView2 = (ListView )findViewById(R.id.listView2);


        if(MenuActivity.albumList != null) {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, MenuActivity.albumList);
            listView2.setAdapter(adapter);

            listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.d("AlbumActivity", "onClick: User clicks an album");
                    String albumName = MenuActivity.albumList.get(i);
                    Intent intent = new Intent(AlbumActivity.this, AlbumSongs.class);
                    intent.putExtra("albumName", albumName);
                    startActivity(intent);

                }
            });
        }

    }
}
