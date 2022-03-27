package thundersharp.aigs.spectre.core.exceptions;

public class DataRenderError extends Error{

    public DataRenderError() {
    }

    public DataRenderError(String message) {
        super(message);
    }

    public DataRenderError(String message, Throwable cause) {
        super(message, cause);
    }

    public DataRenderError(Throwable cause) {
        super(cause);
    }

}
