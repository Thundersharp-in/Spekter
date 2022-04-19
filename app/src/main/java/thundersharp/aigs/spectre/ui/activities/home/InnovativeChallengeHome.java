package thundersharp.aigs.spectre.ui.activities.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;

import thundersharp.aigs.spectre.R;

public class InnovativeChallengeHome extends AppCompatActivity {

    RelativeLayout loader,mainContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_innovative_challenge_home);

        loader = findViewById(R.id.loader);
        mainContents = findViewById(R.id.container);

        setPreAnimation(true);

        new Handler().postDelayed(()->{setPreAnimation(false);},2000);
    }

    private synchronized void setPreAnimation(boolean animation) {
        if (animation) {
            mainContents.setVisibility(View.GONE);
            loader.setVisibility(View.VISIBLE);
        } else {
            mainContents.setVisibility(View.VISIBLE);
            loader.setVisibility(View.GONE);
        }
    }
}