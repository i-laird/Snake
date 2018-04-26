package game;

import resources.Cell;

import java.io.IOException;
import java.net.Socket;

/**
 * @author Ian Laird
 * {@link ClientGame} is a subtype of {@link Game}Repsonsible for Connecting to a Server.
 */
public class ClientGame extends Game {
    private static ClientGame singleGame = null;

    /**
     * @author Ian Laird
     * This is the private constructor for a ClientGame
     */
    private ClientGame(){}

    /**
     * @author Ian Laird
     * This function returns the singleton instance
     * @return the singleton of the class
     */
    static Game getClientGame(){
        if(singleGame == null)
            singleGame = new ClientGame();
        return  singleGame;
    }

    /**
     * @author Ian Laird
     * This function is the overriden function for initializing the network socket
     * @param hostName the ip address of the host
     * @param portNumber the port number of the host's computer
     * @throws IOException if network error is encountered
     */
    public void initializeSocket(String hostName, int portNumber) throws IOException{
        networkSocket = new Socket(hostName, portNumber);
    }

    /**
     * @author Ian Laird
     * This function reads power up location from the server
     * @throws IOException if network error is encountered
     */
    protected void resetPowerUp() throws IOException{
        powerUp = new Cell(moveReader.readInt(), moveReader.readInt());
    }
}
