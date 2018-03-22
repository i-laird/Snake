import javafx.util.Pair;

import java.io.PrintWriter;
import java.util.Scanner;

public abstract class Game {

    protected Snake playerOne;

    protected Snake playerTwo;

    protected Screen gameScreen;

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

    public abstract void setUpGame(String networkStuff);

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

}
