package exceptions;

/**
 * {@link NetworkException} is an Exception that will be thrown
 * by Game whenever a network error is encountered in i/o or if connection
 * is unexpectedly lost
 * 
 * @author Ian Laird
 */
public class NetworkException extends Throwable{
    /**
     * This is the constructor for {@link NetworkException}
     * @author Ian Laird
     */
    public NetworkException(){
        super();
    }

    /**
     * This is a custom constructor for {@link NetworkException}
     * @param message-the message that accompanies the exception
     */
    public NetworkException(String message){
        super(message);
    }
}
