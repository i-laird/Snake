/**
 * file name: Game.java
 * project: Snake
 * author: Ian Laird & Andrew Walker
 */

package game;

import display.Screen;
import exceptions.NetworkError;
import exceptions.SnakeBuilderError;
import reporting.GameReport;
import resources.Cell;
import resources.Snake;
import resources.SnakeBuilder;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * @author Ian Laird
 * {@link Game} represents the Game of Snake. It contains a Screen, Snakes,
 * and the location of the power up.
 */
public abstract class Game {

    // the width of the screen
    protected static final int SCREEN_WIDTH = 800;

    // the height of the screen
    protected static final int SCREEN_HEIGHT = 400;

    // logger for the game
    private static final Logger LOGGER = Logger.getLogger("Game");

    // this player
    protected Snake playerOne = null;

    // the other player
    protected Snake playerTwo = null;

    // used to communicate with the other player
    protected Socket networkSocket = null;

    // writes moves that the player has made to the other player
    protected DataOutputStream moveSender = null;

    // receives moves that the other player has made
    protected DataInputStream moveReader = null;

    // the location of the fruit
    protected Cell powerUp = null;

    private SnakeBuilder snakeMaker = new SnakeBuilder();

    // score of this player
    private int playerOneScore = 0;

    // score of that player
    private int playerTwoScore = 0;

    // username of this player
    private String playerOneUsername;

    // username of that player
    private String playerTwoUsername;

    // used to actually display the game
    private Screen gameScreen = null;

    // indicates if a player has won
    private boolean gameOver = false;

    /**
     * @return screen width
     * @author Ian Laird
     * This function returns the screen width
     */
    public static int getScreenWidth() {
        return SCREEN_WIDTH;
    }

    /**
     * @param hostName name of the server
     * @param portNum  port of the server
     * @return if the connection was successful
     * This function initializes the connnection
     * @throws NetworkError if error connecting to the server
     * @author Ian Laird
     */
    public boolean initConnection(String hostName, int portNum) throws NetworkError {

        //initialize the network
        try {
            LOGGER.info("initializing socket");
            initializeSocket(hostName, portNum);
            LOGGER.info("Initialized Socket");
        } catch (IOException e) {
            LOGGER.severe("Socket initialization failed");
            throw new NetworkError("Socket initialization failed");
        }
        if (!initNetIn() || !initNetOut()) {
            LOGGER.severe("Network initialization failed");
            return false;
            //throw new NetworkError("Network Init error");
        } else {
            return true;
        }
    }

    /**
     * @throws IOException if there is an issue receiving or writing the usernames
     * @author Andrew Walker
     * This function initializes the game variables
     */
    public void initGame() throws IOException {
        moveSender.writeUTF(this.playerOneUsername);
        this.playerTwoUsername = moveReader.readUTF();
        LOGGER.info("Initialized Username");
        initSnakes();
        initScreen();
    }

    /**
     * @throws IOException if there is an issue receiving or writing the positions
     * @author Ian Laird
     * Initializes the Snakes with independent random positions
     */
    protected void initSnakes() throws IOException {
        LOGGER.info("Initilializing the snakes");
        //First create the two Snakes
        Cell start1 = null, start2 = null;
        do {
            start1 = Cell.createRandom(SCREEN_WIDTH / (2 * Cell.getCellSize()), SCREEN_HEIGHT / Cell.getCellSize());
            moveSender.writeInt(start1.getRow());
            moveSender.writeInt(start1.getCol());
            //Make sure snake 2 is independent of Snake 1
            start2 = new Cell(moveReader.readInt(), moveReader.readInt());
        } while (start1.equals(start2));
        try {
            this.playerOne = snakeMaker.init().setColor(Color.RED).setStart(start1).collectSnakeBuilder();
            this.playerTwo = snakeMaker.init().setColor(Color.GREEN).setStart(start2).collectSnakeBuilder();
        } catch (SnakeBuilderError e) {
            LOGGER.severe("Builder Exception caught");
        }
        LOGGER.info("Snakes were generated");
        resetPowerUp();
    }

    /**
     * @return indicates if the game is over
     * @author: Ian Laird
     * This function returns if the game is over
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * @param record the record to restore the game from
     * @return if the users want to play again
     * @author Andrew Walker
     * This function restores the game if the users want
     */
    public boolean playAgain(GameRecord record) {
        LOGGER.info("playAgain");
        this.gameScreen.addMessage("Would you like to play again? (y/n)");
        while (!this.gameScreen.isButtonPressed()) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.gameScreen.setButtonPressed(false);
        this.gameScreen.setHasBegun(false);
        this.gameOver = false;
        if (gameScreen.isPlayAgain()) {
            boolean player2Status = false;
            try {
                moveSender.writeBoolean(gameScreen.isPlayAgain());
                player2Status = moveReader.readBoolean();
            } catch (IOException e) {
                this.gameScreen.dispose();
                return false;
            }
            if (gameScreen.isPlayAgain() && player2Status) {
                this.restoreFromOldState(record);
                this.gameScreen.addMessage("Press space when you are ready!");
                return true;
            } else {
                this.gameScreen.dispose();
                return false;
            }
        } else {
            this.gameScreen.dispose();
            return false;
        }
    }

    /**
     * @return record of this Game
     * @author Ian Laird
     * This records the current game for memento
     */
    public GameRecord createRecord() {
        return new GameRecord(this);
    }

    /**
     * @param old-The Game record that this Game will be restored to
     * @author Ian Laird
     * Restoring to old State using memento
     */
    public void restoreFromOldState(GameRecord old) {
        this.playerOne = Snake.makeSnake(old.getSnakeOneRecord());
        this.playerTwo = Snake.makeSnake(old.getSnakeTwoRecord());
        this.powerUp = old.getPowerUpLocation();
        if (this.gameScreen != null)
            this.gameScreen.setHasBegun(false);
    }

    /**
     * @author Ian Laird
     * This function initializes the Screen for the game
     */
    protected void initScreen() {
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
     * @throws IOException if there is an issue receiving or writing the positions
     * @author Ian Laird
     * This function retrieves moves for both players and then performs those moves
     */
    public void movePlayers() throws IOException {
        Cell playerOneMove = getPlayerOneMove();

        //Send the move read from keyboard over TCP
        sendPlayerOneMove(playerOneMove);

        //read other players move over TCp
        Cell playerTwoMove = getPlayerTwoMove();

        //See if player one  just lost
        //Seeing if playerOne cell exists in Player 2!!!
        if (playerDeadAfterMove(playerTwo, playerOneMove)) {
            System.out.println(playerOne.getHeadLocation().getRow() + " " + playerOne.getHeadLocation().getCol());
            gameOver = true;
            gameScreen.addMessage("Sorry you lost");
            LOGGER.info("Sorry you lost");
            this.gameOver = true;
            return;
        }
        //See if player 1 just won
        if (playerDeadAfterMove(playerOne, playerTwoMove)) {
            System.out.println(playerTwo.getHeadLocation().getRow() + " " + playerTwo.getHeadLocation().getCol());
            gameOver = true;
            gameScreen.addMessage("Yay you just won");
            LOGGER.info("Yay you just won");
            this.playerOneScore += 5;

            this.gameOver = true;
            return;
        }
        boolean powerUpEaten = false;
        if (powerUp.equals(playerOneMove)) {
            playerOne.increaseLength();
            playerOneScore += 1;
            powerUpEaten = true;
            gameScreen.addMessage("You have just eaten a Power-Up");
            gameScreen.addMessage("Your new score is " + playerOneScore);
        }
        if (powerUp.equals(playerTwoMove)) {
            playerTwo.increaseLength();
            powerUpEaten = true;
            playerTwoScore += 1;
            gameScreen.addMessage(playerTwoUsername + " has just eaten a Power-Up");
            gameScreen.addMessage(playerTwoUsername + "'s new score is " + playerTwoScore);
        }
        //get new power up location such that it is not occupied by a resources.Snake
        if (powerUpEaten) {
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
     * @param otherPlayer the player to test against
     * @param moveTo      the other player's move
     * @return if the player is dead after the move
     * @author Ian Laird
     * See if the move will kill the player
     */
    protected boolean playerDeadAfterMove(Snake otherPlayer, Cell moveTo) {
        if (!isMoveInBounds(moveTo)) {
            return true;
        }
        return otherPlayer.containsMove(moveTo);
    }

    /**
     * @param move the move to see if within the bounds of the Game
     * @return indicates if move is within frame
     * @author Ian Laird
     * This function tests if the move is in bounds
     */
    protected boolean isMoveInBounds(Cell move) {
        return ((move.getRow() >= 0) && (move.getRow() < SCREEN_HEIGHT / Cell.getCellSize())
                && (move.getCol() >= 0) && (move.getCol() < SCREEN_WIDTH / (2 * Cell.getCellSize())));
    }

    /**
     * @return indicates success
     * @author Ian Laird
     * This function initializes the network input scanner
     */
    protected boolean initNetIn() {
        //I chose to use Data Input Stream instead of Reader, because it should be more efficient
        try {
            moveReader = new DataInputStream((networkSocket.getInputStream()));
        } catch (IOException e) {
            LOGGER.severe("Network input stream unable to be opened!");
            return false;
        }
        return true;
    }

    /**
     * @return indicates success
     * @author Ian Laird
     * This method initializes the printWriter over the network
     */
    protected boolean initNetOut() {
        //I chose to use Data Output Stream instead of PrintWriter because it should be more efficient
        try {
            moveSender = new DataOutputStream(networkSocket.getOutputStream());
        } catch (IOException e) {
            LOGGER.severe("network output stream unable to be initialized!");
            return false;
        }
        return true;
    }

    /**
     * @return the move generated by the player
     * @author Ian Laird
     * This should get a move direction from user on this machine
     * It then calculates what the new location will be
     */
    protected Cell getPlayerOneMove() {
        return gameScreen.getDirection().performUpdate(this.playerOne);
    }

    /**
     * @return player two's move
     * @throws IOException if there is an issue receiving the position
     * @author Ian Laird
     * This should get a move from player 2 from over the network
     */
    protected Cell getPlayerTwoMove() throws IOException {
        return new Cell(moveReader.readInt(), moveReader.readInt());
    }

    /**
     * @param move the move to send over the network
     * @throws IOException if there is an issue writing the positions
     * @author Ian Laird
     * This should send the move over the network
     */
    protected void sendPlayerOneMove(Cell move) throws IOException {
        moveSender.writeInt(move.getRow());
        moveSender.writeInt(move.getCol());
    }

    /**
     * @return player one's score
     * @author Andrew Walker
     * This function returns player one's score
     */
    public int getScore() {
        return playerOneScore;
    }

    /**
     * @param hostName   the host to connect to
     * @param PortNumber the port to connect over
     * @throws IOException thrown if there is an issue connecting
     * @author Ian Laird
     * Method for initialzing the network socket
     */
    protected abstract void initializeSocket(String hostName, int PortNumber) throws IOException;

    /**
     * @throws IOException thrown if there is an issue sending the power up
     * @author Ian Laird
     * This function resets the power up
     */
    protected abstract void resetPowerUp() throws IOException;

    /**
     * @return Player 1
     * @author Ian Laird
     * This function returns player one
     */
    public Snake getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(Snake playerOne) {
        this.playerOne = playerOne;
    }

    /**
     * @return Player 2
     * @author Ian Laird
     * This function returns player two
     */
    public Snake getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(Snake playerTwo) {
        this.playerTwo = playerTwo;
    }

    /**
     * @return power up
     * @author Ian Laird
     * This function returns the power up
     */
    public Cell getPowerUp() {
        return powerUp;
    }

    public void setPowerUp(Cell powerUp) {
        this.powerUp = powerUp;
    }

    /**
     * @return if the game has begun
     * @throws IOException thrown if there is an issue sending over the connection
     * @author Andrew Walker
     * This function returns if the game has begun
     */
    public boolean hasBegun() throws IOException {

        if (this.gameScreen.isHasBegun()) {
            moveSender.writeBoolean(this.gameScreen.isHasBegun());
            //Move being read means it is time to begin
            moveReader.readBoolean();
            return true;
        } else
            return false;
    }

    /**
     * @param gr the report to add game data to
     * @author Andrew Walker
     * This function adds new game data to the report
     */
    public void amendReport(GameReport gr) {
        gr.ammend(this);
    }

    /**
     * @return Player One's score
     * @author Andrew Walker
     * This function returns the player one score
     */
    public int getPlayerOneScore() {
        return playerOneScore;
    }

    /**
     * @return Player Two's score
     * @author Andrew Walker
     * This function returns the player two score
     */
    public int getPlayerTwoScore() {
        return playerTwoScore;
    }

    /**
     * @return Player One's username
     * @author Andrew Walker
     * This function returns player one's name
     */
    public String getPlayerOneUsername() {
        return playerOneUsername;
    }

    /**
     * @param playerOneUsername the name to set as player one's username
     * @author Andrew Walker
     * This function sets player one's username
     */
    public void setPlayerOneUsername(String playerOneUsername) {
        this.playerOneUsername = playerOneUsername;
    }

    /**
     * @return player two's name
     * @author Andrew Walker
     * This function returns player two's name
     */
    public String getPlayerTwoUsername() {
        return playerTwoUsername;
    }
}
