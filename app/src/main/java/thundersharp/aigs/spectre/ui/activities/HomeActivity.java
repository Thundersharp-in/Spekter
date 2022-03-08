package thundersharp.aigs.spectre.ui.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.exceptions.VideoPlayerException;
import thundersharp.aigs.spectre.core.helpers.VideoPlayer;
import thundersharp.aigs.spectre.core.interfaces.OnVideoReadyCallbacksListener;
import thundersharp.aigs.spectre.core.utils.Progressbars;
import thundersharp.aigs.spectre.databinding.ActivityHomeBinding;


public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private CardView videoHOlder;
    private VideoView videoView;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        videoHOlder = (CardView) findViewById(R.id.videro);
        videoView = findViewById(R.id.videoView);

        videoHOlder.setOnTouchListener(new OnSwipeTouchListener(this){

            @Override
            public void onSwipeTop() {
                Toast.makeText(HomeActivity.this, "top", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeRight() {
                Toast.makeText(HomeActivity.this, "right", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeLeft() {
                Toast.makeText(HomeActivity.this, "left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeBottom() {
                Toast.makeText(HomeActivity.this, "bottom", Toast.LENGTH_SHORT).show();
            }
        });


        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


        VideoPlayer
                .createInstance(this)
                .playDefaultResource(false)
                .setCustomUrl("https://rr3---sn-5jucgv5qc5oq-cagr.googlevideo.com/videoplayback?expire=1646737528&ei=GOQmYoa9H8qE0_wP84KIsA0&ip=209.107.204.70&id=o-ACtSaM4sRXasR072ZlJQftOIVrUDK-oaGWEXfytFKImg&itag=135&aitags=133%2C134%2C135%2C136%2C160%2C242%2C243%2C244%2C247%2C278%2C394%2C395%2C396%2C397%2C398&source=youtube&requiressl=yes&vprv=1&mime=video%2Fmp4&ns=0yyZYQbYngpi1U7jKA14UbMG&gir=yes&clen=1156107&dur=44.960&lmt=1627496050116495&keepalive=yes&fexp=24001373,24007246,24162928&c=WEB&txp=5535434&n=z5WfoWKyG2ngfg&sparams=expire%2Cei%2Cip%2Cid%2Caitags%2Csource%2Crequiressl%2Cvprv%2Cmime%2Cns%2Cgir%2Cclen%2Cdur%2Clmt&sig=AOq0QJ8wRgIhAMa-NwhTlXkIIfAGu0zJqb4Q68hYNRHs202YqDXdGQwjAiEAqkKIMxqM3hzK49FoIEUwecCS_j0uCJoi2qhqV6b6nfQ%3D&redirect_counter=1&rm=sn-hp5yz76&req_id=cd0d24d05973a3ee&cms_redirect=yes&ipbypass=yes&mh=48&mip=49.205.143.58&mm=31&mn=sn-5jucgv5qc5oq-cagr&ms=au&mt=1646715678&mv=m&mvi=3&pl=21&lsparams=ipbypass,mh,mip,mm,mn,ms,mv,mvi,pl&lsig=AG3C_xAwRgIhALvBVZoqNN-fP-Bwyv3mw6PQtwpYJmQ4clooxRBtNkIcAiEA5ho4gRySPV0m52c1P7syNIKWPWBQ9hU60Gz957fG-l0%3D")
                .playInLoop(true)
                .setVideoView(videoView)
                .addOnVideoReadyCallbacksListener(new OnVideoReadyCallbacksListener() {

                    @Override
                    public void onVideoPlayBackStarted(VideoView videoView) {
                        Toast.makeText(HomeActivity.this,"Started",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onVideoCompleated(VideoView videoView) {
                        Toast.makeText(HomeActivity.this, "Compleated", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onVideoSeek(VideoView videoView) {

                    }

                    @Override
                    public void onVideoError(VideoPlayerException videoPlayerException) {

                    }
                });

    }


    @Override
    public void onBackPressed() {
        Progressbars.getInstance().displayExitDialog(this);
    }
}

class OnSwipeTouchListener implements View.OnTouchListener {

    private final GestureDetector gestureDetector;

    public OnSwipeTouchListener(Context ctx) {
        gestureDetector = new GestureDetector(ctx, new GestureListener());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();

                if (Math.abs(diffX) > Math.abs(diffY)) {

                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                        result = true;
                    }
                } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                    result = true;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }


    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
    }

    public void onSwipeTop() {
    }

    public void onSwipeBottom() {
    }
}