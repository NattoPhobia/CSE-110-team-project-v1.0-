package tests;

import android.support.test.rule.ActivityTestRule;
import android.widget.TextView;

import com.example.awong.musicplayer1.AlbumSongs;
import com.example.awong.musicplayer1.MusicActivity;
import com.example.awong.musicplayer1.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Tom on 2/18/2018.
 */

public class MusicActivityTest {
    @Rule
    public ActivityTestRule<MusicActivity> musicActivity = new ActivityTestRule<MusicActivity>(MusicActivity.class);

    //private TextView albumSongsTextView;
    private TextView musicText;


    @Before
    public void setup() {
        musicText = (TextView) musicActivity.getActivity().findViewById(R.id.backBtn);
    }

    @Test
    public void testMenuBtn() {
        String s = musicText.getText().toString();
        assertEquals("Go Back",s);
    }

}
