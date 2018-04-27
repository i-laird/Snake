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
    private ClientGame() {
    }

    /**
     * @return the singleton of the class
     * @author Ian Laird
     * This function returns the singleton instance
     */
    static Game getClientGame() {
        if (singleGame == null)
            singleGame = new ClientGame();
        return singleGame;
    }

    /**
     * @param hostName   the ip address of the host
     * @param portNumber the port number of the host's computer
     * @throws IOException if network error is encountered
     * @author Ian Laird
     * This function is the overriden function for initializing the network socket
     */
    public void initializeSocket(String hostName, int portNumber) throws IOException {
        networkSocket = new Socket(hostName, portNumber);
    }

    /**
     * @throws IOException if network error is encountered
     * @author Ian Laird
     * This function reads power up location from the server
     */
    protected void resetPowerUp() throws IOException {
        powerUp = new Cell(moveReader.readInt(), moveReader.readInt());
    }
}
