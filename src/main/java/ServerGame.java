import games.Game;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Is the Server
 * @author: Ian Laird
 */
public class ServerGame extends Game {

    private static ServerGame singleGame = null;

    private ServerGame(){}

    public static Game getServerGame(){
        if(singleGame == null)
            singleGame = new ServerGame();
        return  singleGame;
    }

    public void initializeSocket(String hostName, int portNumber) throws IOException{
        ServerSocket serverSocket = new ServerSocket(portNumber);
        networkSocket = serverSocket.accept();
    }
}
