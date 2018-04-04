package game_stuff;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Is the Server
 * @author: Ian Laird
 */
public class ServerGame extends Game {

    private static ServerGame singleGame = null;

    private ServerGame(){}

    /**
     * @author: Ian Laird
     * @return the singelton of the class
     */
    static Game getServerGame(){
        if(singleGame == null)
            singleGame = new ServerGame();
        return  singleGame;
    }

    /**
     * initializes the network socket
     * @author: Ian Laird
     * @param hostName
     * @param portNumber
     * @throws IOException
     */
    public void initializeSocket(String hostName, int portNumber) throws IOException{
        ServerSocket serverSocket = new ServerSocket(portNumber);
        networkSocket = serverSocket.accept();
    }
}