package exceptions;

/**
 * @author Ian Laird
 * {@link BuilderException} is an Exception that will be thrown
 * by Snake Builder if action is performed before init.
 */
public class BuilderException extends Throwable{

    /**
     * @author Ian Laird
     * This is the constructor for {@link BuilderException}
     */
    public BuilderException(){
        super();
    }

    /**
     * @author Ian Laird
     * This is a custom constructor for {@link BuilderException}
     * @param message-the message that accompanies the exception
     */
    public BuilderException(String message){
        super(message);
    }
}
