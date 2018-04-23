package exceptions;

/**
 * {@link BuilderException} is an Exception that will be thrown
 * by Snake Builder if action is performed before init.
 *
 * @author Ian Laird
 */
public class BuilderException extends Throwable{
    /**
     * This is the constructor for {@link BuilderException}
     * @author Ian Laird
     */
    public BuilderException(){
        super();
    }

    /**
     * This is a custom constructor for {@link BuilderException}
     * @param message-the message that accompanies the exception
     */
    public BuilderException(String message){
        super(message);
    }
}
