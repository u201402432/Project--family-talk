package android.common.family;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class GalleryActivity extends FragmentActivity {
    Button but_board,but_calendar,but_home,but_set,but_talk;
    private BackPressCloseSystem backPressCloseSystem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(Window.FEATURE_NO_TITLE, Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gallery);

        but_board = (Button)findViewById(R.id.board);
        but_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GalleryActivity.this, GalleryActivity.class));
            }
        });

        but_calendar = (Button)findViewById(R.id.calendar);
        but_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GalleryActivity.this, CalendarActivity.class));
            }
        });

        but_set = (Button)findViewById(R.id.setting);
        but_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GalleryActivity.this, SettingActivity.class));
            }
        });

        but_talk = (Button)findViewById(R.id.talk);
        but_talk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GalleryActivity.this, TalkActivity.class));
            }
        });

        but_home = (Button)findViewById(R.id.home);
        but_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GalleryActivity.this, MainActivity.class));
            }
        });

        backPressCloseSystem = new BackPressCloseSystem(this);
    }
    @Override
    public void onBackPressed() {
        backPressCloseSystem.onBackPressed();
    }
}
