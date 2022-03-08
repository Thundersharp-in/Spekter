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
                .setCustomUrl("https://rr2---sn-npoldn7e.googlevideo.com/videoplayback?expire=1646766788&ei=ZFYnYuO-HoaohgbP_qeQDw&ip=138.219.75.100&id=o-AEK-JQa8isq64_O6oNcgR_tveo4BzvqBnEXThlukx2wv&itag=135&aitags=133%2C134%2C135%2C136%2C160%2C242%2C243%2C244%2C247%2C278%2C298%2C299%2C302%2C303%2C394%2C395%2C396%2C397%2C398%2C399&source=youtube&requiressl=yes&vprv=1&mime=video%2Fmp4&ns=jBweb9wTNEXb0iMzknVFPUoG&gir=yes&clen=782335&dur=28.633&lmt=1615382575380463&keepalive=yes&fexp=24001373,24007246&c=WEB&txp=5535434&n=bMBMtYO1ibto3A&sparams=expire%2Cei%2Cip%2Cid%2Caitags%2Csource%2Crequiressl%2Cvprv%2Cmime%2Cns%2Cgir%2Cclen%2Cdur%2Clmt&sig=AOq0QJ8wRQIgXty8LZkDS0F8ukBS48rUc-2iPXcIzJN-vYIxDMqTbo4CIQCKhzD1c3nb1G_nezj8w5nq0EqY6dL4ESdrWUrZNDq3FA%3D%3D&redirect_counter=1&cm2rm=sn-5hnese7s&req_id=4a397946c0d0a3ee&cms_redirect=yes&cmsv=e&mh=Hn&mip=49.205.143.234&mm=34&mn=sn-npoldn7e&ms=ltu&mt=1646744944&mv=m&mvi=2&pl=21&lsparams=mh,mip,mm,mn,ms,mv,mvi,pl&lsig=AG3C_xAwRQIgXekK0xFgGLWY4Um3H5DGLz9QqRM8briL7V8u4vagHYQCIQD0G8Dgd3fVwf1-YBVwfCE7TGJ1Tz4S9ZCGJnP7_pR7Xw%3D%3D")
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