package thundersharp.aigs.spectre.core.exceptions;

public class VideoPlayerException extends Exception{
    public VideoPlayerException(String message){
        super(message);
    }

    public VideoPlayerException(Throwable throwable, String message){
        super(message,throwable);
    }
}
