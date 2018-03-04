package com.example.awong.musicplayer1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MusicActivity extends AppCompatActivity {

    Button playBtn;
    protected static SeekBar positionBar;
    SeekBar volumeBar;
    TextView elapsedTimeLabel;
    TextView remainingTimeLabel;
    protected static MediaPlayer mediaPlayer;
    protected static int totalTime;
    private Context mContext;
    String songName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_music);

        playBtn = (Button) findViewById(R.id.playBtn);
        elapsedTimeLabel = (TextView) findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel = (TextView) findViewById(R.id.remainingTimeLabel);


        // Get the application context
        mContext = getApplicationContext();

        Button detailsButton = (Button) findViewById(R.id.details);

        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MusicActivity", "onClick: User clicks the details button");
                startActivity(new Intent(MusicActivity.this, Pop.class));
            }
        });

        //Back Button
        Button backBtn = (Button) findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MusicActivity", "onClick: User clicks BACK");
                Intent i = new Intent(MusicActivity.this, MainActivity.class);
                i.putExtra("songName", songName);

                MusicActivity.this.startActivity(i);
                finish();
            }
        });


        Bundle extras = getIntent().getExtras();
        String songName = extras.getString("songName");
        int song_id = this.getResources().getIdentifier(songName, "raw", this.getPackageName());

        //Media Player set up
        mediaPlayer = MenuActivity.musicPlayer.getMediaPlayer();

        mediaPlayer.setLooping(false);
        totalTime = mediaPlayer.getDuration();
        playBtn.setBackgroundResource(R.drawable.stop);
        mediaPlayer.start();

        //Position Bar
        positionBar = (SeekBar) findViewById(R.id.positionBar);
        positionBar.setMax(totalTime);
        positionBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        Log.d("MusicActivity", "onClick: User clicks the music progress seek bar");
                        if (fromUser) {
                            mediaPlayer.seekTo(progress);
                            positionBar.setProgress(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }

        );


        //Volume Bar
        volumeBar = (SeekBar) findViewById(R.id.volumeBar);
        volumeBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        Log.d("MusicActivity", "onClick: User clicks the volume seek bar");
                        float volumeNum = progress / 100f;
                        mediaPlayer.setVolume(volumeNum, volumeNum);

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        //Thread (Update positionBar and timeLabel)
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(mediaPlayer != null) {
                    try {
                        Message msg = new Message();
                        msg.what = mediaPlayer.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {}
                }
            }
        }).start();
    }

    //Handler that handles the seekbar for the position of our song.
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;
            // Update positionBar.
            positionBar.setProgress(currentPosition);

            //Update Labels.
            String elapsedTime = createTimeLabel(currentPosition);
            elapsedTimeLabel.setText(elapsedTime);

            String remainingTime = createTimeLabel(totalTime - currentPosition);
            remainingTimeLabel.setText("- " + remainingTime);
        }
    };

    //Creates the time and remaining time for song.
    public String createTimeLabel(int time) {
        String timeLabel = "";
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;

        timeLabel = min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;

        return timeLabel;
    }

    public void playBtnClick(View view) {
        if(!mediaPlayer.isPlaying()) {
            //Stopping
            mediaPlayer.start();
            Log.d("MusicActivity", "onClick: User clicks the play button");
            playBtn.setBackgroundResource(R.drawable.stop);
        } else {
            //Playing
            mediaPlayer.pause();
            Log.d("MusicActivity", "onClick: User clicks the stop button");
            playBtn.setBackgroundResource(R.drawable.play);
        }

    }
}
