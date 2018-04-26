package game;
import display.Screen;
import exceptions.BuilderException;
import resources.*;
import exceptions.NetworkException;

import java.awt.*;
import java.io.*;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Logger;

/**
 * {@link Game} represents the Game of Snake. It contains a Screen, Snakes,
 *  and the location of the power up.
 * @author Ian Laird
 */
public abstract class Game {

    protected static final int SCREEN_WIDTH = 800;
    protected static final int SCREEN_HEIGHT = 400;
    protected static Logger LOGGER = Logger.getLogger("Game");

    protected SnakeBuilder snakeMaker = new SnakeBuilder();
    protected Snake playerOne = null;
    protected Snake playerTwo = null;
    protected int playerOneScore = 0;
    protected int playerTwoScore = 0;
    private String username;
    private String player2Username;

    protected Socket networkSocket = null;
    //For sending moves over network
    protected DataOutputStream moveSender = null;
    //for receiving moves over network
    protected DataInputStream moveReader = null;

    protected Screen gameScreen = null;

    private boolean gameOver = false;


    //Power up location is communicated over network through server to client
    Cell powerUp = null;
    private boolean hasBegun = false;
    //////////////////////////////////////////////////////////////////////////////////////

    /**
     * @author Ian Laird
     * @param hostName name of the server
     * @param portNum port of the server
     * @throws NetworkException if error connecting to the server
     */
    public boolean initConnection(String hostName, int portNum) throws NetworkException {
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
            return false;
            //throw new NetworkException("Network Init error");
        } else {
            return true;
        }
    }

    public void initGame() throws IOException {
        moveSender.writeUTF(this.username);
        this.player2Username = moveReader.readUTF();
        LOGGER.info("Initialized Username");
        initSnakes();
        initScreen();
    }

    /**
     * @author Ian Laird
     * Initializes the Snakes with independent random positions
     */
    protected void initSnakes() throws IOException{
        //First create the two Snakes
        Cell start1 = null, start2 = null;
        do {
            start1 = Cell.createRandom(SCREEN_WIDTH / (2 * Cell.getCellSize()), SCREEN_HEIGHT / Cell.getCellSize());
            moveSender.writeInt(start1.getRow());
            moveSender.writeInt(start1.getCol());
            //Make sure snake 2 is independent of Snake 1
            start2 = new Cell(moveReader.readInt(), moveReader.readInt());
        }while(start1.equals(start2));
        try {
            this.playerOne = snakeMaker.init().setColor(Color.RED).setStart(start1).collectSnakeBuilder();
            this.playerTwo = snakeMaker.init().setColor(Color.GREEN).setStart(start2).collectSnakeBuilder();
        }catch(BuilderException e){
            LOGGER.severe("Builder Exception caught");
        }
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

    public boolean playAgain(GameRecord record){
        LOGGER.info("playAgain");
        this.gameScreen.addMessage("Would you like to play again? (y/n)");
        while(!this.gameScreen.isButtonPressed()){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.gameScreen.setButtonPressed(false);
        this.gameScreen.setHasBegun(false);
        this.gameOver = false;
        if(gameScreen.isPlayAgain()) {
            boolean player2Status = false;
            try {
                moveSender.writeBoolean(gameScreen.isPlayAgain());
                player2Status = moveReader.readBoolean();
            } catch (IOException e) {
                this.gameScreen.dispose();
                return false;
            }
            if(gameScreen.isPlayAgain() && player2Status) {
                this.restoreFromOldState(record);
                this.gameScreen.addMessage("Press space when you are ready!");
                return true;
            } else {
                this.gameScreen.dispose();
                return false;
            }
        }
        else {
            this.gameScreen.dispose();
            return false;
        }
    }

    /**
     * This records the current game for memento
     *
     * @author Ian Laird
     * @return record of this Game
     */
    public GameRecord createRecord(){
        return new GameRecord(this);
    }

    /**
     * Restoring to old State using memento
     *
     * @author Ian Laird
     * @param old-The Game record that this Game will be restored to
     */
    public void restoreFromOldState(GameRecord old){
        this.playerOne = Snake.makeSnake(old.getSnakeOneRecord());
        this.playerTwo = Snake.makeSnake(old.getSnakeTwoRecord());
        this.powerUp = old.getPowerUpLocation();
        this.hasBegun = false;
    }

    /**
     * initializes the Screen for the game
     * @author Ian Laird
     */
    protected void initScreen(){
        gameScreen = Screen.getInstance(SCREEN_WIDTH, SCREEN_HEIGHT + 50);
        gameScreen.init();
        gameScreen.plotBackground();
        gameScreen.plotPowerUp(powerUp);
        gameScreen.plotSnake(playerOne);
        gameScreen.plotSnake(playerTwo);
        gameScreen.updateScreen();
        gameScreen.showScreen();
        gameScreen.addMessage("Welcome to Snake! The rules are simple and as follows - ");
        gameScreen.addMessage("Use the arrow keys to move and eat the power-ups(blue tiles) to increase your length.");
        gameScreen.addMessage("Don't run into your opponent or the boundaries of the map or you will lose.");
        gameScreen.addMessage("Please press space once you are ready to play.");
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
            gameScreen.addMessage("Sorry you lost");
            LOGGER.info("Sorry you lost");
            this.gameOver = true;
            return;
        }
        //See if player 1 just won
        if(playerDeadAfterMove(playerOne, playerTwoMove)){
            System.out.println(playerTwo.getHeadLocation().getRow() + " " + playerTwo.getHeadLocation().getCol());
            gameOver = true;
            gameScreen.addMessage("Yay you just won");
            LOGGER.info("Yay you just won");
            this.playerOneScore+=5;

            this.gameOver = true;
            return;
        }
        boolean powerUpEaten = false;
        if(powerUp.equals(playerOneMove)){
            playerOne.increaseLength();
            playerOneScore+=1;
            powerUpEaten = true;
            gameScreen.addMessage("You have just eaten a Power-Up");
            gameScreen.addMessage("Your new score is " + playerOneScore);
        }
        if(powerUp.equals(playerTwoMove)){
            playerTwo.increaseLength();
            powerUpEaten = true;
            playerTwoScore+=1;
            gameScreen.addMessage(player2Username + " has just eaten a Power-Up");
            gameScreen.addMessage(player2Username + "'s new score is " + playerTwoScore);
        }
        //get new power up location such that it is not occupied by a resources.Snake
        if(powerUpEaten) {
            resetPowerUp();
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
     * @author Ian Laird
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
     * @author Ian Laird
     * @param move the move to see if within the bounds of the Game
     * @return indicates if move is within frame
     */
    protected boolean isMoveInBounds(Cell move){
        return((move.getRow() >= 0) && (move.getRow() < SCREEN_HEIGHT / Cell.getCellSize())
                && (move.getCol() >= 0) && (move.getCol() < SCREEN_WIDTH / (2* Cell.getCellSize())));
    }

    /**
     * @author Ian Laird
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
     * @author Ian Laird
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
        return gameScreen.getDirection().performUpdate(this.playerOne);
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
        return playerOneScore;
    }

    public String getIP() throws UnknownHostException {
        return Inet4Address.getLocalHost().getHostAddress();
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


    /**
     * @author Ian Laird
     * @return screen width
     */
    public static int getScreenWidth() {
        return SCREEN_WIDTH;
    }

    /**
     * @author Ian Laird
     * @return Player 1
     */
    public Snake getPlayerOne() {
        return playerOne;
    }

    /**
     * @author Ian Laird
     * @return Player 2
     */
    public Snake getPlayerTwo() {
        return playerTwo;
    }

    /**
     * @author Ian Laird
     * @return power up
     */
    public Cell getPowerUp() {
        return powerUp;
    }

    public boolean hasBegun() throws IOException{

        if(this.gameScreen.isHasBegun()) {
            moveSender.writeBoolean(this.gameScreen.isHasBegun());
            boolean player2Status = moveReader.readBoolean();
            return true;
        }
        else
            return false;
    }

    public static void setLOGGER(Logger LOGGER) {
        Game.LOGGER = LOGGER;
    }

    public void setPlayerOne(Snake playerOne) {
        this.playerOne = playerOne;
    }

    public void setPlayerTwo(Snake playerTwo) {
        this.playerTwo = playerTwo;
    }

    public void setPowerUp(Cell powerUp) {
        this.powerUp = powerUp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPlayerOneScore() {
        return playerOneScore;
    }

    public void setPlayerOneScore(int playerOneScore) {
        this.playerOneScore = playerOneScore;
    }

    public int getPlayerTwoScore() {
        return playerTwoScore;
    }

    public void setPlayerTwoScore(int playerTwoScore) {
        this.playerTwoScore = playerTwoScore;
    }

    public void amendReport(GameReport gr){
        gr.ammend(this);
    }

    public String getPlayer2Username(){
        return player2Username;
    }
}
