package old.exceptions;

/**
 * @author Ian Laird
 * {@link NetworkError} is an Exception that will be thrown
 * by Game whenever a network error is encountered in i/o or if connection
 * is unexpectedly lost
 */
public class NetworkError extends Throwable {
    /**
     * @author Ian Laird
     * This is the constructor for {@link NetworkError}
     */
    public NetworkError() {
        super();
    }

    /**
     * @param message-the message that accompanies the exception
     * @author Ian Laird
     * This is a custom constructor for {@link NetworkError}
     */
    public NetworkError(String message) {
        super(message);
    }
}
