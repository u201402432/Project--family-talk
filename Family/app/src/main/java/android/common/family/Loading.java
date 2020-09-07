package android.common.family;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.logging.Handler;

public class Loading extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 2500);


    }
}
