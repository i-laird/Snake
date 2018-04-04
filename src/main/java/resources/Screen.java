package resources;

import Enums.Direct;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Logger;

import static java.awt.Color.BLUE;

/**
 * This class should be a singleton b/c only one screen should exist
 */
public class Screen extends JFrame implements KeyListener {
    private int width;
    private int height;
    private static Screen thisInstance = null;
    private GameBoard board;
    private Direct direct = Direct.UP;
    protected static Logger LOGGER = Logger.getLogger("Screen");

    /**
     * @author: Andrew Walker
     * This method is the singleton constructor for the Screen
     * @param width the width of the frame
     * @param height the height of the frame
     */
    private Screen(int width, int height) {
        this.width = width;
        this.height = height;
        super.setPreferredSize(new Dimension(width + 20, height + 20));
        super.setResizable(true);
        super.pack();
        super.setLocationRelativeTo(null);
        super.setFocusable(true);
        super.requestFocus();
        super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        super.setTitle("Snake");
        LOGGER.info("Initialized Screen");
    }

    /**
     * @author: Andrew Walker
     * This method initializes the gameBoard and adds it to the panel
     */
    public void initBoard(){
        this.board = new GameBoard(this.width, this.height);
        super.getContentPane().add(BorderLayout.CENTER, board);
        LOGGER.info("Initialized Board");
    }

    /**
     * @author: Andrew Walker
     * This function uses the singleton design pattern to return the instance of the screen
     * @return the instance of the Screen
     */
    public static Screen getInstance(int width, int length)
    {
        LOGGER.info("Fetching Instance");
        return (thisInstance == null ? thisInstance = new Screen(width, length) : thisInstance);
    }

    /**
     * @author: Ian Laird
     * This function will simply update the game screen. It itself does not change.
     */
    public void updateScreen() {
        board.repaint();
        super.repaint();
    }

    /**
     * @author: Andrew Walker
     * This function will toggle the display of the screen on
     */
    public void showScreen(){
        super.setVisible(true);
    }

    /**
     * @author: Andrew Walker
     * This function will toggle the display of the screen off
     */
    public void hideScreen(){
        super.setVisible(false);
    }

    public void plotWinScreen(){
        //TODO
    }

    public void plotDefeatScreen(){
        //TODO
    }

    /**
     * @author: Andrew Walker
     * This method is plots the whole screen the default color
     */
    public void plotBackground(){
        for(int x = 0; x < height / Cell.getCellSize(); x++){
            for(int y = 0; y < width / Cell.getCellSize(); y++){
                board.unColorLocation(x,y);
            }
        }
    }

    /**
     * @author: Andrew Walker
     * This method is plots certain cell blue for a powerup
     * @param c the Cell to plot
     */
    public void plotPowerUp(Cell c){
        board.colorLocation(c.getRow(), c.getCol(), BLUE);
    }

    /**
     * @author: Andrew Walker
     * This method returns the width of the frame
     * @return the width
     */
    @Override
    public int getWidth() {
        return width;
    }

    /**
     * @author: Andrew Walker
     * This method sets the width of the frame
     * @param width the new width of the frame
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @author: Andrew Walker
     * This method returns the height of the frame
     * @return the height
     */
    @Override
    public int getHeight() {
        return height;
    }

    /**
     * @author: Andrew Walker
     * This method sets the height of the frame
     * @param height the new height of the frame
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @author: Andrew Walker
     * This method returns the current direction
     * @return direction the player wants to move
     */
    public Direct getDirection(){
        return direct;
    }

    /**
     * @author: Andrew Walker
     * This method is plots a snake on the gameBoard
     * @param s the Snake to plot
     */
    public void plotSnake(Snake s){
        for(Cell c : s.getSnakeLocations()){
            board.colorLocation(c.getRow(), c.getCol(), s.getColor());
        }
    }

    /**
     * @author: Andrew Walker
     * This method gets the keyPressed and sets the direction
     * @param e the KeyEvent to pull the key from
     */
    @Override
    public void keyTyped(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_RIGHT: direct = Direct.RIGHT; break;
            case KeyEvent.VK_LEFT: direct = Direct.LEFT; break;
            case KeyEvent.VK_UP: direct = Direct.UP; break;
            case KeyEvent.VK_DOWN: direct = Direct.DOWN; break;
        }
    }

    /**
     * @author: Andrew Walker
     * This method gets the keyPressed and sets the direction through delegation to keyTyped
     * @param e the KeyEvent to pull the key from
     */
    @Override
    public void keyPressed(KeyEvent e) {
        keyTyped(e);
    }

    /**
     * @author: Andrew Walker
     * This method gets the keyPressed and sets the direction through delegation to keyTyped
     * @param e the KeyEvent to pull the key from
     */
    @Override
    public void keyReleased(KeyEvent e) {
        keyTyped(e);
    }
}
