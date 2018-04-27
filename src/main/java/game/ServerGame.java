package game;

import resources.Cell;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * @author Ian Laird
 * {@link ClientGame} is a subtype of {@link Game}Repsonsible for hosting a Server.
 */
public class ServerGame extends Game {

    private static ServerGame singleGame = null;

    /**
     * @author Ian Laird
     * This is the private constructor for a ServerGame
     */
    private ServerGame() {
    }

    /**
     * @return the singleton of the class
     * @author Ian Laird
     * This function returns the singleton instance
     */
    static Game getServerGame() {
        if (singleGame == null)
            singleGame = new ServerGame();
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
        ServerSocket serverSocket = new ServerSocket(portNumber);
        networkSocket = serverSocket.accept();
    }

    /**
     * @throws IOException if network error is encountered
     * @author Ian Laird
     * This function reads power up location from the server
     */
    protected void resetPowerUp() throws IOException {
        do {
            powerUp = Cell.createRandom(SCREEN_WIDTH / (2 * Cell.getCellSize()), SCREEN_HEIGHT / Cell.getCellSize());
        } while (playerOne.containsMove(powerUp) || playerTwo.containsMove(powerUp));
        moveSender.writeInt(powerUp.getRow());
        moveSender.writeInt(powerUp.getCol());
    }
}
