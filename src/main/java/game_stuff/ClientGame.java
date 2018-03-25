package game_stuff;

import java.io.IOException;
import java.net.Socket;

/**
 * Repsonsible for Connecting to a Server.
 * @author: ian Laird
 */
public class ClientGame extends Game {
    private static ClientGame singleGame = null;

    private ClientGame(){}

    /**
     * @author: Ian Laird
     * @return the singleton of the class
     */
    static Game getClientGame(){
        if(singleGame == null)
            singleGame = new ClientGame();
        return  singleGame;
    }

    /**
     * overriden function for initializing the network socket
     * @param hostName
     * @param portNumber
     * @throws IOException
     */
    public void initializeSocket(String hostName, int portNumber) throws IOException{
        networkSocket = new Socket(hostName, portNumber);
    }
}
