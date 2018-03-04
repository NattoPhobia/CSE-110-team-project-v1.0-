package com.example.awong.musicplayer1;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.location.Location;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    ListView listView;

    ListAdapter adapter;
    protected static String fileName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button menuButton = (Button) findViewById(R.id.menuButton);
        Button popBtn = (Button) findViewById(R.id.popupMain);

        if(MenuActivity.musicPlayer != null) {
            popBtn.setText(popBtn.getText() + " " + MenuActivity.musicPlayer.getSongTitle());
        }

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainActivity", "onClick: User clicks the MENU button");
                Intent i = new Intent(MainActivity.this, MenuActivity.class);
                MainActivity.this.startActivity(i);
            }
        });

        popBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainActivity", "onClick: User clicks Current Playing:");

                if (!MenuActivity.musicPlayer.getCurrentSong().equals("")) {
                    String songTitle = MenuActivity.musicPlayer.getSongTitle();
                    fileName = MenuActivity.musicPlayer.getCurrentSong();

                    MenuActivity.musicPlayer.popUp(fileName, songTitle, MainActivity.this, MusicActivity.class);
                }
            }
        });

        listView = (ListView) findViewById(R.id.listView);


        if(MenuActivity.listTitles != null) {
            listView.setAdapter(new MyListAdapter(this, R.layout.list_item, MenuActivity.listTitles));


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.d("MainActivity", "onClick: User clicks a song");
                    fileName = MenuActivity.listFileNames.get(i);
                    String songTitle = MenuActivity.listTitles.get(i);

                    //Getting Data and Location for Flashback
                    Calendar currentCalendar = Calendar.getInstance();
                    Song currentSongObj = MenuActivity.songMap.get(songTitle);
                    currentSongObj.setCal(currentCalendar);

//                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//                    String time = sdf.format(currentCalendar.getTime());
//                    System.out.println("Current Date " + time);

                    MenuActivity.gps = new GPSTracker(MainActivity.this);

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

                    if(MenuActivity.songMap.get(songTitle).getPriority() != 0) {
                        MenuActivity.musicPlayer.popUp(fileName, songTitle, MainActivity.this, MusicActivity.class);
                    }
                }
            });
        }

    }
    private class MyListAdapter extends ArrayAdapter<String>{
        private int layout;
        private MyListAdapter(Context context, int resource, List<String> objects){
            super(context, resource, objects);
            layout = resource;
        }

        @Override
        public  View getView(final int position, View convertView, ViewGroup parent){
            ViewHolder mainViewHolder = null;

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layout, parent, false);
            final ViewHolder viewHolder = new ViewHolder();

            viewHolder.title =  (TextView) convertView.findViewById(R.id.list_item_text);

            viewHolder.button = (Button) convertView.findViewById(R.id.list_item_btn);

            viewHolder.button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    String clickedSong = getItem(position);
                    Song clickedSongObj = MenuActivity.songMap.get(clickedSong);


                    if(viewHolder.button.getText().equals("+")){
                        Log.d("MainActivity", "onClick: User clicks + ");
                        viewHolder.button.setText("✓");
                        clickedSongObj.setPriority(2);


                    }

                    else if(viewHolder.button.getText().equals("✓")){
                        Log.d("MainActivity", "onClick: User clicks ✓ ");
                        viewHolder.button.setText("X");
                        clickedSongObj.setPriority(0);
                        if(MenuActivity.musicPlayer.getMediaPlayer() != null) {
                            MenuActivity.musicPlayer.getMediaPlayer().stop();
                        }

                    }
                    else{
                        Log.d("MainActivity", "onClick: User clicks X ");
                        viewHolder.button.setText("+");
                        clickedSongObj.setPriority(1);
                    }

                    System.out.println(clickedSongObj.getPriority());
                }


            });

            String currTitle = getItem(position);
            Song currSongObj= MenuActivity.songMap.get(currTitle);




            if(currSongObj.getPriority() == 0)
                viewHolder.button.setText("X");
            else if (currSongObj.getPriority() == 1)
                viewHolder.button.setText("+");
            else
                viewHolder.button.setText("✓");

            convertView.setTag(viewHolder);
            mainViewHolder = (ViewHolder) convertView.getTag();
            mainViewHolder.title.setText(currTitle);


            return convertView;
        }
    }
    public class ViewHolder{
        TextView title;
        Button button;
    }
}




