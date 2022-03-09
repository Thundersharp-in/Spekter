package thundersharp.aigs.spectre.ui.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
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
    private ImageView closeView;
    private VideoPlayer videoPlayer;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        videoHOlder = findViewById(R.id.videro);
        videoView = findViewById(R.id.videoView);
        closeView = findViewById(R.id.closeView);

        videoHOlder.setVisibility(View.INVISIBLE);
        videoHOlder.setOnTouchListener(new OnSwipeTouchListener(this) {

            @Override
            public void onSwipeTop() {
            }

            @Override
            public void onSwipeRight() {
                dismissAdView();
            }

            @Override
            public void onSwipeLeft() {
                dismissAdView();
            }

            @Override
            public void onSwipeBottom() {
                dismissAdView();
            }
        });

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        closeView.setOnClickListener(n->{
            videoPlayer.releasePlayer();
            AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
            alphaAnimation.setDuration(1000);
            alphaAnimation.setRepeatCount(0);
            videoHOlder.setAnimation(alphaAnimation);
            videoHOlder.setVisibility(View.GONE);
        });

        videoPlayer = VideoPlayer
                .createInstance(this)
                .playDefaultResource(false)
                .setCustomUrl("https://images.all-free-download.com/footage_preview/mp4/satellite_with_its_orbit_around_earth_6892430.mp4")
                .playInLoop(true)
                .setVideoView(videoView)
                .addOnVideoReadyCallbacksListener(new OnVideoReadyCallbacksListener() {

                    @Override
                    public void onVideoPlayBackStarted(VideoView videoView) {
                        videoHOlder.setVisibility(View.VISIBLE);
                        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                        alphaAnimation.setDuration(1000);
                        alphaAnimation.setRepeatCount(0);
                        videoHOlder.setAnimation(alphaAnimation);
                    }

                    @Override
                    public void onVideoCompleated(VideoView videoView) {

                    }

                    @Override
                    public void onVideoSeek(VideoView videoView) {

                    }

                    @Override
                    public void onVideoError(VideoPlayerException videoPlayerException) {

                    }
                });

    }

    private void dismissAdView() {

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