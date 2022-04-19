package thundersharp.aigs.spectre.ui.activities.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;

import thundersharp.aigs.spectre.R;

public class UpcommingEventsHome extends AppCompatActivity {

    private RelativeLayout contents,loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcomming_events_home);

        contents = findViewById(R.id.contents);
        loader = findViewById(R.id.loader);

        setPreAnimation(true);

        new Handler().postDelayed(() -> {
            setPreAnimation(false);
        }, 2000);
    }

    private synchronized void setPreAnimation(boolean animation) {
        if (animation) {
            contents.setVisibility(View.GONE);
            loader.setVisibility(View.VISIBLE);
        } else {
            contents.setVisibility(View.VISIBLE);
            loader.setVisibility(View.GONE);
        }
    }
}