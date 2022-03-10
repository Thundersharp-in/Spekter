package thundersharp.aigs.spectre.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.NestedScrollView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;

import thundersharp.aigs.spectre.R;

public class ExhibitionHome extends AppCompatActivity {

    private AppCompatButton book;
    private NestedScrollView mainContents;
    private RelativeLayout preAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibition_home);

        book = findViewById(R.id.book);
        mainContents = findViewById(R.id.mainScroll);
        preAnimation = findViewById(R.id.mainContainer);

        setPreAnimation(true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setPreAnimation(false);
            }
        },4000);

    }

    private synchronized void setPreAnimation(boolean animation){
        if (animation){
            book.setVisibility(View.GONE);
            mainContents.setVisibility(View.GONE);
            preAnimation.setVisibility(View.VISIBLE);
        }else {
            book.setVisibility(View.VISIBLE);
            mainContents.setVisibility(View.VISIBLE);
            preAnimation.setVisibility(View.GONE);
        }
    }
}