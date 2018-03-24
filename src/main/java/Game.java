import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * @author: Ian Laird
 */
public abstract class Game {

    //TODO figure out if this is a goos size or not
    protected static final int CELLSIZE = 10;
    protected static final int SCREEN_WIDTH = 400;
    protected static final int SCREEN_HEIGHT = 400;

    protected static Logger LOGGER = Logger.getLogger("Game");

    protected Snake playerOne = null;
    protected Snake playerTwo = null;
    protected Screen gameScreen = null;
    protected Socket networkSocket = null;

    //For sending mopves over network
    protected PrintWriter moveSender = null;

    //for receiving moves over network
    protected Scanner moveReader = null;

    private boolean gameOver = false;

    Cell powerUp = Cell.createRandom(gameScreen.getWidth(), gameScreen.getHeight());

    //////////////////////////////////////////////////////////////////////////////////////

    public void initGame(String hostName, int portNum) throws NetworkException{
        //First create the two Snakes
        Cell start1 = null, start2 = null;
        start1 = Cell.createRandom(SCREEN_WIDTH, SCREEN_HEIGHT);
        do{
            start2 = Cell.createRandom(SCREEN_WIDTH, SCREEN_HEIGHT);
        }while(start1.equals(start2));
        this.playerOne = new Snake(start1);
        this.playerTwo = new Snake(start2);
        LOGGER.info("Snakes were generated");
        //Now initialize the network
        try {
            initializeSocket(hostName, portNum);
        }catch(IOException e){
            LOGGER.severe("Socket failed to open!");
            throw new NetworkException("Socket Error");
        }
        if(!initNetIn() || !initNetOut()){
            LOGGER.severe("Network initialization failed");
            throw new NetworkException("Network Init error");
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void playAgain() {
        this.gameOver = false;
    }

    public void initScreen(){
        gameScreen = Screen.getInstance(SCREEN_WIDTH, SCREEN_HEIGHT);
    }
    public void MovePlayers(){
        Cell playerOneMove, playerTwoMove;
        playerOneMove = getPlayerOneMove();
        //Send the move read from keyboard over TCP
        sendPlayerOneMove(playerOneMove);
        //read other players move over TCp
        playerTwoMove = getPlayerTwoMove();
        //See if player one  just lost
        //Seeing if playerOne cell exists in Player 2!!!
        if(playerDeadAfterMove(playerTwo, playerOneMove)){
            gameOver = true;
            gameScreen.plotDefeatScreen();
            //Should this return IDK yet
        }
        //See if player 1 just won
        if(playerDeadAfterMove(playerOne, playerTwoMove)){
            gameOver = true;
            gameScreen.plotWinScreen();
        }
        boolean resetPowerUp = false;
        if(powerUp.equals(playerOneMove)){
            playerOne.increaseLength();
            resetPowerUp = true;
        }
        if(powerUp.equals(playerTwoMove)){
            playerTwo.increaseLength();
            resetPowerUp = true;
        }
        //get new power up location such that it is not occupied by a Snake
        if(resetPowerUp) {
            do {
                powerUp = Cell.createRandom(gameScreen.getWidth(), gameScreen.getHeight());
            }while(playerOne.snakeCoverMove(powerUp) || playerTwo.snakeCoverMove(powerUp));
        }
        gameScreen.plotBackground();
        gameScreen.plotPowerUp(powerUp.getRow(), powerUp.getCol());
        playerOne.moveLocation(playerOneMove);
        playerTwo.moveLocation(playerTwoMove);
        playerOne.drawSnake(gameScreen, CELLSIZE);
        playerTwo.drawSnake(gameScreen, CELLSIZE);
    }

    /**
     * See if the move will kill the player
     * @param otherPlayer
     * @return
     */
    protected boolean playerDeadAfterMove(Snake otherPlayer, Cell moveTo){
        boolean moveKills = false;
        moveKills = otherPlayer.snakeCoverMove(moveTo);
        if(moveKills) {
            return true;
        }
        return isMoveInBounds(moveTo);
    }

    /**
     * @author:Ian Laird
     * @param move the move to see if within the bounds of the Game
     * @return indicates if move is within frame
     */
    protected boolean isMoveInBounds(Cell move){
        return((move.getRow() < SCREEN_HEIGHT / CELLSIZE)
                && (move.getCol() < SCREEN_WIDTH / CELLSIZE));
    }

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

    /**
     * This should get a move direction from user on this machine
     * It then calculates what the new location will be
     * @return the move generated by the player
     */
    protected Cell getPlayerOneMove(){
        Direct readMove = gameScreen.readMoveFromKeyboard();
        switch (readMove){
            case UP:
               return new Cell(playerOne.getHeadLocation().getRow() + 1, playerOne.getHeadLocation().getCol());
            case DOWN:
                return new Cell(playerOne.getHeadLocation().getRow() - 1, playerOne.getHeadLocation().getCol());
            case LEFT:
                return new Cell(playerOne.getHeadLocation().getRow(), playerOne.getHeadLocation().getCol() - 1);
            default:
                return new Cell(playerOne.getHeadLocation().getRow(), playerOne.getHeadLocation().getCol() + 1);
        }
    }

    /**
     * This should get a move from player 2 from over the network
     * @return
     */
    protected Cell getPlayerTwoMove(){
        return new Cell(moveReader.nextInt(), moveReader.nextInt());
    }

    /**
     * This should send the move over the network
     * @param move
     */
    protected void sendPlayerOneMove(Cell move){
        moveSender.println(move.getRow());
        moveSender.println(move.getCol());
    }

    ////////////////////////////////////////////////////////////////

    protected abstract void initializeSocket(String hostName, int PortNumber) throws IOException;

}
