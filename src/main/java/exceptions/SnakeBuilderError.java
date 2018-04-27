package exceptions;

/**
 * @author Ian Laird
 * {@link SnakeBuilderError} is an Exception that will be thrown
 * by Snake Builder if action is performed before init.
 */
public class SnakeBuilderError extends Throwable {

    /**
     * @author Ian Laird
     * This is the constructor for {@link SnakeBuilderError}
     */
    public SnakeBuilderError() {
        super();
    }

    /**
     * @param message-the message that accompanies the exception
     * @author Ian Laird
     * This is a custom constructor for {@link SnakeBuilderError}
     */
    public SnakeBuilderError(String message) {
        super(message);
    }
}
