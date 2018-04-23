package game_stuff;

import resources.Cell;

import java.io.IOException;
import java.net.Socket;

/**
 * {@link ClientGame} is a subtype of {@link Game}Repsonsible for Connecting to a Server.
 * @author ian Laird
 */
public class ClientGame extends Game {
    private static ClientGame singleGame = null;

    /**
     * @author Ian Laird
     */
    private ClientGame(){}

    /**
     * @author Ian Laird
     * @return the singleton of the class
     */
    static Game getClientGame(){
        if(singleGame == null)
            singleGame = new ClientGame();
        return  singleGame;
    }

    /**
     * overriden function for initializing the network socket
     * @param hostName-the ip address of the host
     * @param portNumber-the port number of the host's computer
     * @throws IOException-if network error is encountered
     */
    public void initializeSocket(String hostName, int portNumber) throws IOException{
        networkSocket = new Socket(hostName, portNumber);
    }

    /**
     * reads power up location from the server
     * @author Ian Laird
     * @throws IOException-if network error is encountered
     */
    protected void resetPowerUp() throws IOException{
        powerUp = new Cell(moveReader.readInt(), moveReader.readInt());
    }
}
