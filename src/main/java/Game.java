import javafx.util.Pair;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Logger;

public abstract class Game {
    Logger LOGGER = Logger.getLogger("Game");

    protected Snake playerOne;

    protected Snake playerTwo;

    protected Screen gameScreen;

    protected Socket networkSocket = null;

    //For sending mopves over network
    private PrintWriter moveSender = null;

    //for receiving moves over network
    private Scanner moveReader = null;

    private boolean gameOver = false;

    public boolean isGameOver() {
        return gameOver;
    }

    public void playAgain() {
        this.gameOver = false;
    }

    public void MovePlayers(){
        playerOne.moveLocation(getPlayerOneMove());
        playerTwo.moveLocation(getPlayerTwoMove());
        playerOne.drawSnake(gameScreen);
        playerTwo.drawSnake(gameScreen);
    }

    /**
     * This should get the move for home Machine from user on this machine
     * @return
     */
    protected abstract Pair<Integer, Integer> getPlayerOneMove();

    /**
     * This should get a move for other player from over the network
     * @return
     */
    protected abstract Pair<Integer, Integer> getPlayerTwoMove();

    public void initNetwork(String hostName, int portNum){
        try {
            initializeSocket(hostName, portNum);
        }catch(IOException e){
            LOGGER.severe("Socket failed to open!");
            return;
        }
        if(!initNetIn() || !initNetOut()){
            LOGGER.severe("Network initialization failed");
        }
    }

    public abstract void initializeSocket(String hostName, int PortNumber) throws IOException;

    protected boolean initNetIn(){
        try{
        moveReader = new Scanner( new BufferedReader(
                new InputStreamReader(networkSocket.getInputStream())));
        }catch(IOException e){
            LOGGER.severe("Network input stream unable to be opened!");
            return false;
        }
        return true;
    }

    protected boolean initNetOut(){
        try {
            moveSender = new PrintWriter(networkSocket.getOutputStream(), true);
        }
        catch(IOException e){
            LOGGER.severe("network output stream unable to be initialized!");
            return false;
        }
        return true;
    }

}
