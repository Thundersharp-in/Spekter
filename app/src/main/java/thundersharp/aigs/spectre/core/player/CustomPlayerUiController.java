package thundersharp.aigs.spectre.core.player;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import thundersharp.aigs.spectre.R;

public class CustomPlayerUiController extends AbstractYouTubePlayerListener implements YouTubePlayerFullScreenListener {
    private final View playerUi;

    private ImageView seek10Forward,getSeek10Backward;
    private LinearLayout play_pause_layout;
    private Context context;
    private YouTubePlayer youTubePlayer;
    private YouTubePlayerView youTubePlayerView;
    private RelativeLayout bottom_components_holder;
    private SeekBar seekBar_luminosite;
    private int currentTime=0;
    private Animation toolsAnimation;

    // panel is used to intercept clicks on the WebView, I don't want the user to be able to click the WebView directly.
    private View panel;
    private View progressbar;
    private TextView videoCurrentTimeTextView;
    private TextView videoDurationTextView;
    private ImageView playPauseButton;

    private final YouTubePlayerTracker playerTracker;
    private boolean fullscreen = false;
    int lastVisiblityStatus = View.VISIBLE;

    public CustomPlayerUiController(Context context, View customPlayerUi, YouTubePlayer youTubePlayer, YouTubePlayerView youTubePlayerView) {
        this.playerUi = customPlayerUi;
        this.context = context;
        this.youTubePlayer = youTubePlayer;
        this.youTubePlayerView = youTubePlayerView;

        playerTracker = new YouTubePlayerTracker();
        youTubePlayer.addListener(playerTracker);

        initViews(customPlayerUi);
    }

    private void initViews(View playerUi) {
        toolsAnimation = AnimationUtils.loadAnimation(context,R.anim.fadein);
        panel = playerUi.findViewById(R.id.panel);
        progressbar = playerUi.findViewById(R.id.progressbar);
        videoCurrentTimeTextView = playerUi.findViewById(R.id.video_current_time);
        videoDurationTextView = playerUi.findViewById(R.id.video_duration);
        playPauseButton = playerUi.findViewById(R.id.play_pause_button);
        seekBar_luminosite = playerUi.findViewById(R.id.seekBar_luminosite);
        play_pause_layout = playerUi.findViewById(R.id.play_pause_layout);
        bottom_components_holder = playerUi.findViewById(R.id.bottom_components_holder);
        seek10Forward = playerUi.findViewById(R.id.next_coarse_video);
        getSeek10Backward = playerUi.findViewById(R.id.previous_coarse_video);

        getSeek10Backward.setOnClickListener(n->{
            youTubePlayer.seekTo((currentTime-10));
            youTubePlayer.play();
            Toast.makeText(context, "Rewinded 10 seconds", Toast.LENGTH_SHORT).show();
        });

        seek10Forward.setOnClickListener(n->{
            youTubePlayer.seekTo((currentTime+10));
            youTubePlayer.play();
            Toast.makeText(context, "Forwarded 10 seconds", Toast.LENGTH_SHORT).show();
        });

        seekBar_luminosite.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                youTubePlayer.seekTo(seekBar.getProgress());
                youTubePlayer.play();
                setvisiblity(View.GONE);
            }
        });

        playPauseButton.setOnClickListener( (view) -> {
            if(playerTracker.getState() == PlayerConstants.PlayerState.PLAYING)
                youTubePlayer.pause();
            else youTubePlayer.play();
        });


        panel.setOnClickListener(n->{
            if (lastVisiblityStatus == View.VISIBLE){
                setvisiblity(View.GONE);

            }else {
                setvisiblity(View.VISIBLE);

            }
        });
    }

    private void setvisiblity(int status){

        if (status == View.GONE){
            play_pause_layout.startAnimation(toolsAnimation);
            playPauseButton.startAnimation(toolsAnimation);
            bottom_components_holder.startAnimation(toolsAnimation);

        }else if (status == View.VISIBLE){

            playPauseButton.clearAnimation();
            play_pause_layout.clearAnimation();
            bottom_components_holder.clearAnimation();
        }
        play_pause_layout.setVisibility(status);
        playPauseButton.setVisibility(status);
        bottom_components_holder.setVisibility(status);
        lastVisiblityStatus = status;


    }

    @Override
    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
        progressbar.setVisibility(View.GONE);
    }

    @Override
    public void onStateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState state) {

        if(state == PlayerConstants.PlayerState.PLAYING || state == PlayerConstants.PlayerState.VIDEO_CUED) {
            panel.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
            playPauseButton.setImageResource(R.drawable.ic_baseline_motion_photos_paused_24);
            setvisiblity(View.GONE);
            progressbar.setVisibility(View.GONE);

        }else if(state == PlayerConstants.PlayerState.BUFFERING) {
            panel.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
            progressbar.setVisibility(View.VISIBLE);

        }else if (state == PlayerConstants.PlayerState.PAUSED ){
            panel.setBackgroundColor(ContextCompat.getColor(context, android.R.color.black));
            playPauseButton.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);

            setvisiblity(View.VISIBLE);

            progressbar.setVisibility(View.GONE);
        }


    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCurrentSecond(@NonNull YouTubePlayer youTubePlayer, float second) {
        currentTime = (int) second;
        videoCurrentTimeTextView.setText(secondsToString((int) second));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            seekBar_luminosite.setProgress((int)second,true);
        }else seekBar_luminosite.setProgress((int)second);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onVideoDuration(@NonNull YouTubePlayer youTubePlayer, float duration) {
        videoDurationTextView.setText(secondsToString((int) duration));
        seekBar_luminosite.setMax((int)duration);

    }

    @Override
    public void onYouTubePlayerEnterFullScreen() {
        ViewGroup.LayoutParams viewParams = playerUi.getLayoutParams();
        viewParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        viewParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        playerUi.setLayoutParams(viewParams);
    }

    @Override
    public void onYouTubePlayerExitFullScreen() {
        ViewGroup.LayoutParams viewParams = playerUi.getLayoutParams();
        viewParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        viewParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        playerUi.setLayoutParams(viewParams);
    }

    private String secondsToString(int pTime) {
        return String.format("%02d:%02d", pTime / 60, pTime % 60);
    }



}
