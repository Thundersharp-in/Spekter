package thundersharp.aigs.spectre.core.interfaces;

import android.widget.VideoView;

import thundersharp.aigs.spectre.core.exceptions.VideoPlayerException;

public interface OnVideoReadyCallbacksListener {

    void onVideoPlayBackStarted(VideoView videoView);
    void onVideoCompleated(VideoView videoView);
    void onVideoSeek(VideoView videoView);
    void onVideoError(VideoPlayerException videoPlayerException);

}
