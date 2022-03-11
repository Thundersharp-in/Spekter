package thundersharp.aigs.spectre.core.exceptions;

public class ArgumentsMissingException extends Exception {

    public ArgumentsMissingException() {
    }

    public ArgumentsMissingException(String message) {
        super(message);
    }

    public ArgumentsMissingException(String message, Throwable cause) {
        super(message, cause);
    }
}
