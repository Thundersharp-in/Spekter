package thundersharp.aigs.spectre.core.helpers;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.VideoView;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.exceptions.VideoPlayerException;
import thundersharp.aigs.spectre.core.interfaces.OnVideoReadyCallbacksListener;

public class VideoPlayer {

    private VideoView videoView;
    private String customUrl;
    private int resourceId;
    private boolean playDefault = true;
    private boolean playInLoop = false;

    private Activity activity;

    private OnVideoReadyCallbacksListener onVideoReadyCallbacksListener;

    public static VideoPlayer createInstance(Activity activity){
        return new VideoPlayer(activity);
    }

    public VideoPlayer(Activity activity){
        this.activity = activity;
    }

    public VideoPlayer setVideoView(VideoView videoView){
        this.videoView = videoView;
        return this;
    }

    public VideoPlayer playInLoop(boolean playInLoop){
        this.playInLoop = playInLoop;
        return this;
    }

    public VideoPlayer setCustomUrl(String url){
        this.customUrl = url;
        return this;
    }

    public VideoPlayer setLocalResource(int resourceID){
        this.resourceId = resourceID;
        return this;
    }

    public VideoPlayer playDefaultResource(boolean play){
        this.playDefault  = play;
        return this;
    }

    public VideoPlayer addOnVideoReadyCallbacksListener(OnVideoReadyCallbacksListener onVideoReadyCallbacksListener){
        this.onVideoReadyCallbacksListener = onVideoReadyCallbacksListener;
        if (videoView != null)
            playVideo();
        else onVideoReadyCallbacksListener.onVideoError(new VideoPlayerException("No video view found !"));
        return this;
    }

    private void playVideo(){
        if (playDefault){
            playDefaultVideo();
        }else if (!playDefault && customUrl == null && resourceId == 0){
            onVideoReadyCallbacksListener.onVideoError(new VideoPlayerException("No video resource found !"));
        }else {
            String url = "";
            if (resourceId != 0)
                url = "android.resource://" +activity.getPackageName()+ "/"+ resourceId;
                else
                url = customUrl;

            playByCustomUrl(url);
        }
    }

    private void playDefaultVideo() {
        videoView.setVideoURI(Uri.parse("android.resource://" +activity.getPackageName()+ "/"+ R.raw.logo));

        //videoView.requestFocus();
        videoView.start();
        videoView.setOnErrorListener((mediaPlayer, i, i1) -> {
            onVideoReadyCallbacksListener.onVideoError(new VideoPlayerException("INTERNAL ERROR"));
            return false;
        });

        videoView.setOnPreparedListener(mp -> {
            float videoRatio = mp.getVideoWidth() / (float) mp.getVideoHeight();
            float screenRatio = videoView.getWidth() / (float)
                    videoView.getHeight();
            float scaleX = videoRatio / screenRatio;
            if (scaleX >= 1f) {
                videoView.setScaleX(scaleX);
            } else {
                videoView.setScaleY(1f / scaleX);
            }

            onVideoReadyCallbacksListener.onVideoPlayBackStarted(videoView);
        });

        videoView.setOnCompletionListener(mediaPlayer -> {
            if (playInLoop) videoView.start();
            onVideoReadyCallbacksListener.onVideoCompleated(videoView);
        });


    }

    private void playByCustomUrl(String customUrl) {
        videoView.setVideoURI(Uri.parse(customUrl));

        videoView.requestFocus();
        videoView.start();
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                onVideoReadyCallbacksListener.onVideoError(new VideoPlayerException("INTERNAL ERROR"));
                return false;
            }
        });
        videoView.setOnPreparedListener(mp -> {
            float videoRatio = mp.getVideoWidth() / (float) mp.getVideoHeight();
            float screenRatio = videoView.getWidth() / (float)
                    videoView.getHeight();
            float scaleX = videoRatio / screenRatio;
            if (scaleX >= 1f) {
                videoView.setScaleX(scaleX);
            } else {
                videoView.setScaleY(1f / scaleX);
            }
            onVideoReadyCallbacksListener.onVideoPlayBackStarted(videoView);
        });

        videoView.setOnCompletionListener(mediaPlayer -> {
            if (playInLoop) videoView.start();
            onVideoReadyCallbacksListener.onVideoCompleated(videoView);
        });

    }

    public void releasePlayer(){
        videoView.stopPlayback();
        videoView.clearFocus();
    }

}
