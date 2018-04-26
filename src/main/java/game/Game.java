package game;
import display.Screen;
import exceptions.SnakeBuilderError;
import reporting.GameReport;
import resources.*;
import exceptions.NetworkError;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * @author Ian Laird
 * {@link Game} represents the Game of Snake. It contains a Screen, Snakes,
 *  and the location of the power up.
 */
public abstract class Game {

    protected static final int SCREEN_WIDTH = 800;
    protected static final int SCREEN_HEIGHT = 400;
    private static final Logger LOGGER = Logger.getLogger("Game");

    private SnakeBuilder snakeMaker = new SnakeBuilder();
    protected Snake playerOne = null;
    protected Snake playerTwo = null;
    private int playerOneScore = 0;
    private int playerTwoScore = 0;
    private String playerOneUsername;
    private String playerTwoUsername;

    protected Socket networkSocket = null;
    protected DataOutputStream moveSender = null;
    protected DataInputStream moveReader = null;

    private Screen gameScreen = null;
    protected Cell powerUp = null;

    private boolean gameOver = false;

    /**
     * @author Ian Laird
     * This function initializes the connnection
     * @param hostName name of the server
     * @param portNum port of the server
     * @throws NetworkError if error connecting to the server
     */
    public boolean initConnection(String hostName, int portNum) throws NetworkError {
        //initialize the network
        try {
            initializeSocket(hostName, portNum);
            LOGGER.info("Initialized Socket");
        }catch(IOException e){
            LOGGER.severe("Socket failed to open!");
            throw new NetworkError("Socket Error");
        }
        if(!initNetIn() || !initNetOut()){
            LOGGER.severe("Network initialization failed");
            return false;
            //throw new NetworkError("Network Init error");
        } else {
            return true;
        }
    }

    /**
     * @author Andrew Walker
     * This function initializes the game variables
     * @throws IOException if there is an issue recieving or writing the usernames
     */
    public void initGame() throws IOException {
        moveSender.writeUTF(this.playerOneUsername);
        this.playerTwoUsername = moveReader.readUTF();
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
        }catch(SnakeBuilderError e){
            LOGGER.severe("Builder Exception caught");
        }
        LOGGER.info("Snakes were generated");
        resetPowerUp();
    }

    /**
     * @author: Ian Laird
     * This function returns if the game is over
     * @return indicates if the game is over
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * @author Andrew Walker
     * This function restores the game if the users want
     * @param record the record to restore the game from
     * @return if the users want to play again
     */
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
     * @author Ian Laird
     * This records the current game for memento
     * @return record of this Game
     */
    public GameRecord createRecord(){
        return new GameRecord(this);
    }

    /**
     * @author Ian Laird
     * Restoring to old State using memento
     * @param old-The Game record that this Game will be restored to
     */
    public void restoreFromOldState(GameRecord old){
        this.playerOne = Snake.makeSnake(old.getSnakeOneRecord());
        this.playerTwo = Snake.makeSnake(old.getSnakeTwoRecord());
        this.powerUp = old.getPowerUpLocation();
        if(this.gameScreen != null)
            this.gameScreen.setHasBegun(false);
    }

    /**
     * @author Ian Laird
     * This function initializes the Screen for the game
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
     * This function retrieves moves for both players and then performs those moves
     */
    public void movePlayers() throws IOException{
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
            gameScreen.addMessage(playerTwoUsername + " has just eaten a Power-Up");
            gameScreen.addMessage(playerTwoUsername + "'s new score is " + playerTwoScore);
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
     * @author Ian Laird
     * See if the move will kill the player
     * @param otherPlayer the player to test against
     * @param moveTo the other player's move
     * @return if the player is dead after the move
     */
    protected boolean playerDeadAfterMove(Snake otherPlayer, Cell moveTo){
        if(!isMoveInBounds(moveTo)){
            return true;
        }
        return otherPlayer.containsMove(moveTo);
    }

    /**
     * @author Ian Laird
     * This function tests if the move is in bounds
     * @param move the move to see if within the bounds of the Game
     * @return indicates if move is within frame
     */
    protected boolean isMoveInBounds(Cell move){
        return((move.getRow() >= 0) && (move.getRow() < SCREEN_HEIGHT / Cell.getCellSize())
                && (move.getCol() >= 0) && (move.getCol() < SCREEN_WIDTH / (2* Cell.getCellSize())));
    }

    /**
     * @author Ian Laird
     * This function initializes the network input scanner
     * @return indicates success
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
     * This method initializes the printWriter over the network
     * @return indicates success
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
     * @author Ian Laird
     * This should get a move direction from user on this machine
     * It then calculates what the new location will be
     * @return the move generated by the player
     */
    protected Cell getPlayerOneMove(){
        return gameScreen.getDirection().performUpdate(this.playerOne);
    }

    /**
     * @author Ian Laird
     * This should get a move from player 2 from over the network
     * @return player two's move
     */
    protected Cell getPlayerTwoMove() throws IOException{
        return new Cell(moveReader.readInt(), moveReader.readInt());
    }

    /**
     * @author Ian Laird
     * This should send the move over the network
     * @param move the move to send over the network
     */
    protected void sendPlayerOneMove(Cell move) throws IOException{
        moveSender.writeInt(move.getRow());
        moveSender.writeInt(move.getCol());
    }

    /**
     * @author Andrew Walker
     * This function returns player one's score
     * @return player one's score
     */
    public int getScore() {
        return playerOneScore;
    }

    /**
     * @author Ian Laird
     * Method for initialzing the network socket
     * @param hostName the host to connect to
     * @param PortNumber the port to connect over
     * @throws IOException thrown if there is an issue connecting
     */
    protected abstract void initializeSocket(String hostName, int PortNumber) throws IOException;

    /**
     * @author Ian Laird
     * This function resets the power up
     * @throws IOException thrown if there is an issue sending the power up
     */
    protected abstract void resetPowerUp() throws IOException;

    /**
     * @author Ian Laird
     * This function returns the screen width
     * @return screen width
     */
    public static int getScreenWidth() {
        return SCREEN_WIDTH;
    }

    /**
     * @author Ian Laird
     * This function returns player one
     * @return Player 1
     */
    public Snake getPlayerOne() {
        return playerOne;
    }

    /**
     * @author Ian Laird
     * This function returns player two
     * @return Player 2
     */
    public Snake getPlayerTwo() {
        return playerTwo;
    }

    /**
     * @author Ian Laird
     * This function returns the power up
     * @return power up
     */
    public Cell getPowerUp() {
        return powerUp;
    }

    /**
     * @author Andrew Walker
     * This function returns if the game has begun
     * @return if the game has begun
     * @throws IOException thrown if there is an issue sending over the connection
     */
    public boolean hasBegun() throws IOException{

        if(this.gameScreen.isHasBegun()) {
            moveSender.writeBoolean(this.gameScreen.isHasBegun());
            //Move being read means it is time to begin
            moveReader.readBoolean();
            return true;
        }
        else
            return false;
    }

    /**
     * @author Andrew Walker
     * This function adds new game data to the report
     * @param gr the report to add game data to
     */
    public void amendReport(GameReport gr){
        gr.ammend(this);
    }

    /**
     * @author Andrew Walker
     * This function returns the player one score
     * @return
     */
    public int getPlayerOneScore() {
        return playerOneScore;
    }

    /**
     * @author Andrew Walker
     * This function returns the player two score
     * @return
     */
    public int getPlayerTwoScore() {
        return playerTwoScore;
    }

    /**
     * @author Andrew Walker
     * This function returns player one's name
     * @return
     */
    public String getPlayerOneUsername() {
        return playerOneUsername;
    }

    /**
     * @author Andrew Walker
     * This function sets player one's username
     * @param playerOneUsername the name to set as player one's username
     */
    public void setPlayerOneUsername(String playerOneUsername) {
        this.playerOneUsername = playerOneUsername;
    }

    /**
     * @author Andrew Walker
     * This function returns player two's name
     * @return player two's name
     */
    public String getPlayerTwoUsername() {
        return playerTwoUsername;
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
}
