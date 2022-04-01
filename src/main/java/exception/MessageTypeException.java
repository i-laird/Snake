package exception;

/******************************************************************************
 *
 * MessageTypeException.java
 *
 * author: Ian laird
 *
 * Created 7/29/21
 *
 * © 2021
 *
 ******************************************************************************/

public class MessageTypeException extends Throwable{
    /**
     * constructor
     */
    private MessageTypeException(){
        super();
    }

    /**
     * custom constructor
     * @param s message detailing the cause of the error
     */
    public MessageTypeException(String s){
        super(s);
    }
}
