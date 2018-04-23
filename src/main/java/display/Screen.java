package display;

import Enums.Direct;
import resources.Cell;
import resources.Snake;

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
    Wrapper wrapper;
    private Direct direct = Direct.UP;
    protected static Logger LOGGER = Logger.getLogger("Screen");
    private boolean debugging = true;
    private boolean hasBegun = false;

    /**
     * @author: Andrew Walker
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

    public void init(){
        this.wrapper = new Wrapper(this.width, this.height);
        wrapper.initBoard();
        wrapper.initText();
        super.add(this.wrapper);
        LOGGER.info("Initialized Wrapper");
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
        wrapper.update();
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
     * This method is plots certain cell blue for a powerup
     * @param c the Cell to plot
     */
    public void plotPowerUp(Cell c){
        wrapper.getBoard().colorLocation(c.getRow(), c.getCol(), BLUE);
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
        if(s.getPrevTail() != null){
            wrapper.getBoard().unColorLocation(s.getPrevTail().getRow(), s.getPrevTail().getCol());
        }
        for(Cell c : s.getSnakeLocations()){
            wrapper.getBoard().colorLocation(c.getRow(), c.getCol(), s.getColor());
        }
    }

    /**
     * @author: Andrew Walker
     * This method gets the keyPressed and sets the direction
     * @param e the KeyEvent to pull the key from
     */
    @Override
    public void keyTyped(KeyEvent e) {
        if(debugging) System.out.println("keyTyped: " + e.getKeyCode());
        switch(e.getKeyCode()){
            case KeyEvent.VK_RIGHT: if(direct != Direct.LEFT) direct = Direct.RIGHT; break;
            case KeyEvent.VK_LEFT: if(direct != Direct.RIGHT) direct = Direct.LEFT; break;
            case KeyEvent.VK_UP: if(direct != Direct.DOWN) direct = Direct.UP; break;
            case KeyEvent.VK_DOWN: if(direct != Direct.UP) direct = Direct.DOWN; break;
            case KeyEvent.VK_SPACE: if(!hasBegun) hasBegun = true; break;
        }
    }

    /**
     * @author: Andrew Walker
     * This method gets the keyPressed and sets the direction through delegation to keyTyped
     * @param e the KeyEvent to pull the key from
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if(debugging) System.out.println("keyPressed: " + e.getKeyCode());
        switch(e.getKeyCode()){
            case KeyEvent.VK_RIGHT: if(direct != Direct.LEFT) direct = Direct.RIGHT; break;
            case KeyEvent.VK_LEFT: if(direct != Direct.RIGHT) direct = Direct.LEFT; break;
            case KeyEvent.VK_UP: if(direct != Direct.DOWN) direct = Direct.UP; break;
            case KeyEvent.VK_DOWN: if(direct != Direct.UP) direct = Direct.DOWN; break;
            case KeyEvent.VK_SPACE: if(!hasBegun) hasBegun = true; break;
        }
    }

    /**
     * @author: Andrew Walker
     * This method gets the keyPressed and sets the direction through delegation to keyTyped
     * @param e the KeyEvent to pull the key from
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if(debugging) System.out.println("keyReleased: " + e.getKeyCode());
        switch(e.getKeyCode()){
            case KeyEvent.VK_RIGHT: if(direct != Direct.LEFT) direct = Direct.RIGHT; break;
            case KeyEvent.VK_LEFT: if(direct != Direct.RIGHT) direct = Direct.LEFT; break;
            case KeyEvent.VK_UP: if(direct != Direct.DOWN) direct = Direct.UP; break;
            case KeyEvent.VK_DOWN: if(direct != Direct.UP) direct = Direct.DOWN; break;
            case KeyEvent.VK_SPACE: if(!hasBegun) hasBegun = true; break;
        }
    }

    public void plotBackground() {
        wrapper.getBoard().plotBackground();
    }

    public void addMessage(String s) {
        this.wrapper.getTextBox().addText(s);
    }

    public boolean isHasBegun() {
        return this.hasBegun;
    }
}
