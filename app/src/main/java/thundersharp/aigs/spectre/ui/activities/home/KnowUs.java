package thundersharp.aigs.spectre.ui.activities.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;

import thundersharp.aigs.spectre.R;

public class KnowUs extends AppCompatActivity {

    private RelativeLayout loader,mainContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_know_us);

        loader = findViewById(R.id.container);
        mainContents = findViewById(R.id.mainContents);

        setPreAnimation(true);

        new Handler().postDelayed(() -> setPreAnimation(false),2000);

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