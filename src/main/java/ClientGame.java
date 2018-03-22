import java.io.IOException;
import java.net.Socket;

/**
 * Repsonsible for Connecting to a Server.
 * @author: ian Laird
 */
public class ClientGame extends Game {
    private static ClientGame singleGame = null;

    private ClientGame(){}

    public static Game getClientGame(){
        if(singleGame == null)
            singleGame = new ClientGame();
        return  singleGame;
    }

    public void initializeSocket(String hostName, int portNumber) throws IOException{
        networkSocket = new Socket(hostName, portNumber);
    }

    /**
     * Player 2 is the server
     * @return
     */
    protected  Cell getPlayerTwoMove(){

    }

}
