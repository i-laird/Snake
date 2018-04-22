package game_stuff;
import display.Screen;
import resources.*;
import Enums.Direct;
import exceptions.NetworkException;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * @author: Ian Laird
 */
public abstract class Game {

    //TODO figure out if this is a good size or not
    protected static final int SCREEN_WIDTH = 400;
    protected static final int SCREEN_HEIGHT = 400;
    protected static Logger LOGGER = Logger.getLogger("Game");
    protected Snake playerOne = null;
    protected Snake playerTwo = null;
    protected Screen gameScreen = null;
    protected Socket networkSocket = null;
    protected SnakeBuilder snakeMaker = new SnakeBuilder();
    private int score = 0;
    //For sending moves over network
    protected DataOutputStream moveSender = null;
    //for receiving moves over network
    protected DataInputStream moveReader = null;
    private boolean gameOver = false;

    //Power up location is communicated over network through server to client
    Cell powerUp = null;
    //////////////////////////////////////////////////////////////////////////////////////

    /**
     * @author:Ian Laird
     * @param hostName name of the server
     * @param portNum port of the server
     * @throws NetworkException if error connecting to the server
     */
    public void initGame(String hostName, int portNum) throws NetworkException, IOException {
        //initialize the network
        try {
            initializeSocket(hostName, portNum);
            LOGGER.info("Initialized Socket");
        }catch(IOException e){
            LOGGER.severe("Socket failed to open!");
            throw new NetworkException("Socket Error");
        }
        if(!initNetIn() || !initNetOut()){
            LOGGER.severe("Network initialization failed");
            throw new NetworkException("Network Init error");
        }
        initSnakes();
        initScreen();
    }

    /**
     * @author: Ian Laird
     * Initializes the Snakes with independent random positions
     */
    protected void initSnakes() throws IOException{
        //First create the two Snakes
        Cell start1 = null, start2 = null;
        do {
            start1 = Cell.createRandom(SCREEN_WIDTH / Cell.getCellSize(), SCREEN_HEIGHT / Cell.getCellSize());
            moveSender.writeInt(start1.getRow());
            moveSender.writeInt(start1.getCol());
            //Make sure snake 2 is independent of Snake 1
            start2 = new Cell(moveReader.readInt(), moveReader.readInt());
        }while(start1.equals(start2));
        this.playerOne = snakeMaker.init().setColor(Color.RED).setStart(start1).collectSnakeBuilder();
        this.playerTwo = snakeMaker.init().setColor(Color.GREEN).setStart(start2).collectSnakeBuilder();
        LOGGER.info("Snakes were generated");
        resetPowerUp();
    }

    /**
     * @author: Ian Laird
     * @return indicates if the game is over
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * @author: Ian Laird
     * used to reset state of the game
     */
    public boolean playAgain(boolean player1Status) throws IOException{
        this.gameOver = false;
        moveSender.writeBoolean(player1Status);
        boolean player2Status = moveReader.readBoolean();
        if(player1Status && player2Status) {
            initSnakes();
            resetPowerUp();
            return true;
        } else {
            this.gameScreen.dispose();
            return false;
        }

    }

    /**
     * initializes the Screen for the game
     * @author: Ian Laird
     */
    protected void initScreen(){
        gameScreen = Screen.getInstance(SCREEN_WIDTH, SCREEN_HEIGHT);
        gameScreen.initBoard();
        gameScreen.showScreen();
        gameScreen.plotBackground();
        gameScreen.plotPowerUp(powerUp);
        gameScreen.plotSnake(playerOne);
        gameScreen.plotSnake(playerTwo);
        gameScreen.updateScreen();

    }

    /**
     * @author: Ian Laird
     * retrieves moves for both players and then performs those moves
     */
    public void MovePlayers() throws IOException{
        Cell playerOneMove = getPlayerOneMove();

        //Send the move read from keyboard over TCP
        sendPlayerOneMove(playerOneMove);

        //read other players move over TCp
        Cell playerTwoMove = getPlayerTwoMove();

        //See if player one  just lost
        //Seeing if playerOne cell exists in Player 2!!!
        if(playerDeadAfterMove(playerTwo, playerOneMove)){
            System.out.println(playerOne.getHeadLocation().getRow() + " " + playerOne.getHeadLocation().getCol());
            gameOver = true;
            gameScreen.plotDefeatScreen();
            LOGGER.info("Sorry you lost");
            this.gameOver = true;
            return;
        }
        //See if player 1 just won
        if(playerDeadAfterMove(playerOne, playerTwoMove)){
            System.out.println(playerTwo.getHeadLocation().getRow() + " " + playerTwo.getHeadLocation().getCol());
            gameOver = true;
            gameScreen.plotWinScreen();
            LOGGER.info("Yay you just won");
            this.score+=5;

            this.gameOver = true;
            return;
        }
        boolean powerUpEaten = false;
        if(powerUp.equals(playerOneMove)){
            playerOne.increaseLength();
            score+=1;
            powerUpEaten = true;
        }
        if(powerUp.equals(playerTwoMove)){
            playerTwo.increaseLength();
            powerUpEaten = true;
        }
        //get new power up location such that it is not occupied by a resources.Snake
        if(powerUpEaten) {
            resetPowerUp();
            //do {
              //  powerUp = Cell.createRandom(gameScreen.getWidth(), gameScreen.getHeight());
            //}while(playerOne.containsMove(powerUp) || playerTwo.containsMove(powerUp));
        }

        // Moves the snakes
        playerOne.moveLocation(playerOneMove);
        playerTwo.moveLocation(playerTwoMove);

        // Update the screen
        gameScreen.plotBackground();
        gameScreen.plotPowerUp(powerUp);
        gameScreen.plotSnake(playerOne);
        gameScreen.plotSnake(playerTwo);
        gameScreen.updateScreen();
    }

    /**
     * See if the move will kill the player
     * @param otherPlayer
     * @return
     */
    protected boolean playerDeadAfterMove(Snake otherPlayer, Cell moveTo){
        if(!isMoveInBounds(moveTo)){
            return true;
        }
        return otherPlayer.containsMove(moveTo);
    }

    /**
     * @author:Ian Laird
     * @param move the move to see if within the bounds of the Game
     * @return indicates if move is within frame
     */
    protected boolean isMoveInBounds(Cell move){
        return((move.getRow() >= 0) && (move.getRow() < SCREEN_HEIGHT / Cell.getCellSize())
                && (move.getCol() >= 0) && (move.getCol() < SCREEN_WIDTH / Cell.getCellSize()));
    }

    /**
     * @author: Ian Laird
     * @return indiactes success
     * This function initializes the network input scanner
     */
    protected boolean initNetIn(){
        //I chose to use Data Input Stream instead of Reader, because it should be more efficient
        try{
            moveReader = new DataInputStream((networkSocket.getInputStream()));
        }catch(IOException e){
            LOGGER.severe("Network input stream unable to be opened!");
            return false;
        }
        return true;
    }


    /**
     * @author: Ian Laird
     * @return indicates success
     * This method initializes the printWriter over the network
     */
    protected boolean initNetOut(){
        //I chose to use Data Output Stream instead of PrintWriter because it should be more efficient
        try {
            moveSender = new DataOutputStream(networkSocket.getOutputStream());
        }
        catch(IOException e){
            LOGGER.severe("network output stream unable to be initialized!");
            return false;
        }
        return true;
    }

    /**
     * This should get a move direction from user on this machine
     * It then calculates what the new location will be
     * @return the move generated by the player
     */
    protected Cell getPlayerOneMove(){
        Direct readMove = gameScreen.getDirection();
        //Calculate new position based on direction read from the keyboard
        switch (readMove){
            case UP:
               return new Cell(playerOne.getHeadLocation().getRow() - 1, playerOne.getHeadLocation().getCol());
            case DOWN:
                return new Cell(playerOne.getHeadLocation().getRow() + 1, playerOne.getHeadLocation().getCol());
            case LEFT:
                return new Cell(playerOne.getHeadLocation().getRow(), playerOne.getHeadLocation().getCol() - 1);
            case RIGHT:
                return new Cell(playerOne.getHeadLocation().getRow(), playerOne.getHeadLocation().getCol() + 1);
            default:
                return new Cell(playerOne.getHeadLocation().getRow(), playerOne.getHeadLocation().getCol());
        }
    }

    /**
     * This should get a move from player 2 from over the network
     * @return
     */
    protected Cell getPlayerTwoMove() throws IOException{
        return new Cell(moveReader.readInt(), moveReader.readInt());
    }

    /**
     * This should send the move over the network
     * @param move
     */
    protected void sendPlayerOneMove(Cell move) throws IOException{
        moveSender.writeInt(move.getRow());
        moveSender.writeInt(move.getCol());
    }
    public int getScore() {
        return score;
    }


    ////////////////////////////////////////////////////////////////

    /**
     * Method for initialzing the network socket
     * @param hostName
     * @param PortNumber
     * @throws IOException
     */
    protected abstract void initializeSocket(String hostName, int PortNumber) throws IOException;

    protected abstract void resetPowerUp() throws IOException;

}
