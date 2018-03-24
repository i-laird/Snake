package exceptions;

public class NetworkException extends Throwable{
    public NetworkException(){
        super();
    }
    public NetworkException(String message){
        super(message);
    }
}
