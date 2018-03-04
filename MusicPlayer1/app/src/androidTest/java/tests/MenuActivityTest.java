package tests;

import android.support.test.rule.ActivityTestRule;
import android.widget.TextView;

import com.example.awong.musicplayer1.MenuActivity;
import com.example.awong.musicplayer1.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by AWong on 2/17/18.
 */

public class MenuActivityTest {

    @Rule
    public ActivityTestRule<MenuActivity> menuActivity = new ActivityTestRule<MenuActivity>(MenuActivity.class);

    private TextView albumTextView;
    private TextView songTextView;
    private TextView flashBackTextView;

    @Before
    public void setup() {
        albumTextView = (TextView) menuActivity.getActivity().findViewById(R.id.albums);
        songTextView = (TextView) menuActivity.getActivity().findViewById(R.id.songs);
        flashBackTextView = (TextView) menuActivity.getActivity().findViewById(R.id.flashback);
    }

    @Test
    public void testAlbumBtn() {
        String value = albumTextView.getText().toString();
        assertEquals("Albums", value);
    }

    @Test
    public void testSongsBtn() {
        String value = songTextView.getText().toString();
        assertEquals("Songs", value);
    }

    /*
    @Test
    public void testFlashBackBtn() {
        String value = flashBackTextView.getText().toString();
        assertEquals("Flashback off", value);
    }
    */

}
