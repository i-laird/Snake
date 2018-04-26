package display;

import directions.Direction;
import enums.Direct;
import directions.DirectionFactory;
import resources.Cell;
import resources.Snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Logger;

import static java.awt.Color.BLUE;

/**
 * @author Andrew Walker
 * {@link Screen} is a singleton and controls Swing Graphics
 */
public class Screen extends JFrame implements KeyListener {
    final private static Logger LOGGER = Logger.getLogger("Screen");
    private static Screen thisInstance = null;
    private int width;
    private int height;

    private Direct state = Direct.UP;
    private boolean hasBegun = false;
    private boolean isPlayAgain = false;
    private boolean buttonPressed;

    private Wrapper wrapper;

    /**
     * @author Andrew Walker
     * This method is the singleton constructor for the Screen
     * @param width the width of the frame
     * @param height the height of the frame
     */
    private Screen(int width, int height) {
        this.width = width;
        this.height = height;
        this.setPreferredSize(new Dimension(this.width, this.height));
        this.setResizable(false);
        this.pack();
        this.setBackground(Color.WHITE);
        this.setLocationRelativeTo(null);
        this.setFocusable(true);
        this.requestFocus();
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setTitle("Snake");
        this.addKeyListener(this);
        this.setFocusTraversalKeysEnabled(false);

        LOGGER.info("Initialized Screen");
    }

    /**
     * @author Andrew Walker
     * This function inits the inner wrapper of the components and adds it to the
     * Screen
     */
    public void init(){
        this.wrapper = new Wrapper();
        wrapper.initBoard();
        wrapper.initText();
        super.add(this.wrapper);
        LOGGER.info("Initialized Wrapper");
    }

    /**
     * @author Andrew Walker
     * This function uses the singleton design pattern to return the instance of the screen
     * @return the instance of the Screen
     */
    public static Screen getInstance(int width, int length)
    {
        LOGGER.info("Fetching Instance");
        return (thisInstance == null ? thisInstance = new Screen(width, length) : thisInstance);
    }

    /**
     * @author Andrew Walker
     * This function will simply update the game screen. It itself does not change.
     */
    public void updateScreen() {
        wrapper.update();
        super.repaint();
    }

    /**
     * @author Andrew Walker
     * This function will toggle the display of the screen on
     */
    public void showScreen(){
        super.setVisible(true);
    }

    /**
     * @author Andrew Walker
     * This method is plots certain cell blue for a powerup
     * @param c the Cell to plot
     */
    public void plotPowerUp(Cell c){
        wrapper.getBoard().colorLocation(c.getRow(), c.getCol(), BLUE);
    }

    /**
     * @author Andrew Walker
     * This method returns the width of the frame
     * @return the width
     */
    @Override
    public int getWidth() {
        return width;
    }

    /**
     * @author Andrew Walker
     * This method returns the height of the frame
     * @return the height
     */
    @Override
    public int getHeight() {
        return height;
    }

    /**
     * @author Andrew Walker
     * This method returns the current direction
     * @return direction the player wants to move
     */
    public Direction getDirection(){
        return DirectionFactory.make(state);
    }

    /**
     * @author Andrew Walker
     * This method is plots a snake on the gameBoard
     * @param s the Snake to plot
     */
    public void plotSnake(Snake s){
        if(s.getPrevTail() != null){
            wrapper.getBoard().unColorLocation(s.getPrevTail().getRow(), s.getPrevTail().getCol());
        }
        for(Cell c : s.getSnakeLocations()){
            if(c.getCol() >= 0 && c.getCol() < this.width / (2 * Cell.getCellSize()) && c.getRow() >= 0 && c.getRow() < this.height / Cell.getCellSize())
                wrapper.getBoard().colorLocation(c.getRow(), c.getCol(), s.getColor());
        }
    }

    /**
     * @author Andrew Walker
     * This method gets the keyPressed and sets the direction
     * @param e the KeyEvent to pull the key from
     */
    @Override
    public void keyTyped(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_RIGHT: if(state != Direct.LEFT) state = Direct.RIGHT; break;
            case KeyEvent.VK_LEFT: if(state != Direct.RIGHT) state = Direct.LEFT; break;
            case KeyEvent.VK_UP: if(state != Direct.DOWN) state = Direct.UP; break;
            case KeyEvent.VK_DOWN: if(state != Direct.UP) state = Direct.DOWN; break;
            case KeyEvent.VK_SPACE: if(!hasBegun) hasBegun = true; break;
            case KeyEvent.VK_Y: if(!isPlayAgain) isPlayAgain = true; buttonPressed = true; break;
            case KeyEvent.VK_N: if(isPlayAgain) isPlayAgain = false; buttonPressed = true; break;
            default: state = Direct.UP; break;
        }
    }

    /**
     * @author Andrew Walker
     * This method gets the keyPressed and sets the direction through delegation to keyTyped
     * @param e the KeyEvent to pull the key from
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_RIGHT: if(state != Direct.LEFT) state = Direct.RIGHT; break;
            case KeyEvent.VK_LEFT: if(state != Direct.RIGHT) state = Direct.LEFT; break;
            case KeyEvent.VK_UP: if(state != Direct.DOWN) state = Direct.UP; break;
            case KeyEvent.VK_DOWN: if(state != Direct.UP) state = Direct.DOWN; break;
            case KeyEvent.VK_SPACE: if(!hasBegun) hasBegun = true; break;
            case KeyEvent.VK_Y: if(!isPlayAgain) isPlayAgain = true; buttonPressed = true; break;
            case KeyEvent.VK_N: if(isPlayAgain) isPlayAgain = false; buttonPressed = true; break;
            default: state = Direct.UP; break;
        }
    }

    /**
     * @author Andrew Walker
     * This method gets the keyPressed and sets the direction through delegation to keyTyped
     * @param e the KeyEvent to pull the key from
     */
    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_RIGHT: if(state != Direct.LEFT) state = Direct.RIGHT; break;
            case KeyEvent.VK_LEFT: if(state != Direct.RIGHT) state = Direct.LEFT; break;
            case KeyEvent.VK_UP: if(state != Direct.DOWN) state = Direct.UP; break;
            case KeyEvent.VK_DOWN: if(state != Direct.UP) state = Direct.DOWN; break;
            case KeyEvent.VK_Y: if(!isPlayAgain) isPlayAgain = true; break;
            case KeyEvent.VK_N: if(isPlayAgain) isPlayAgain = false; break;
            default: state = Direct.UP; break;
        }
    }

    /**
     * @author Andrew Walker
     * This function plots the background on the game board
     */
    public void plotBackground() {
        wrapper.getBoard().plotBackground();
    }

    /**
     * @author Andrew Walker
     * This function adds a message to the text box
     * @param s the message to add to the text box
     */
    public void addMessage(String s) {
        this.wrapper.getTextBox().addText(s);
    }

    /**
     * @author Andrew Walker
     * This function returns if the user has indicated they want to begin the game
     * @return if the user has indicated they want to begin the game
     */
    public boolean isHasBegun() {
        return hasBegun;
    }

    /**
     * @author Andrew Walker
     * Resets if the user wants the game to start or not
     * @param hasBegun a boolean dictating if the game should start
     */
    public void setHasBegun(boolean hasBegun) {
        this.hasBegun = hasBegun;
    }

    /**
     * @author Andrew Walker
     * This function returns if the user wants to play again
     * @return if the user wants to play again
     */
    public boolean isPlayAgain() {
        return isPlayAgain;
    }

    /**
     * @author Andrew Walker
     * This function returns if the user has puposefully indicated that they presses a button
     * @return if the user has puposefully indicated that they presses a button
     */
    public boolean isButtonPressed() {
        return buttonPressed;
    }

    /**
     * @author Andrew Walker
     * This function resets if the user purposefully pressed a button
     * @param buttonPressed a boolean dictating if a button has been pressed
     */
    public void setButtonPressed(boolean buttonPressed) {
        this.buttonPressed = buttonPressed;
    }
}
