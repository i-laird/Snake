package exceptions;

/**
 * @author Ian Laird
 * {@link NetworkException} is an Exception that will be thrown
 * by Game whenever a network error is encountered in i/o or if connection
 * is unexpectedly lost
 */
public class NetworkException extends Throwable{
    /**
     * @author Ian Laird
     * This is the constructor for {@link NetworkException}
     */
    public NetworkException(){
        super();
    }

    /**
     * @author Ian Laird
     * This is a custom constructor for {@link NetworkException}
     * @param message-the message that accompanies the exception
     */
    public NetworkException(String message){
        super(message);
    }
}
