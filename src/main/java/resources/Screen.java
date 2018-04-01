package resources;

import Enums.Color;
import Enums.Direct;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * This class should be a singleton b/c only one screen should exist
 */
//TODO Most of this class is unfinished...We need to implement Swing stuff
    //-Ian
public class Screen extends JPanel {
    private int width;
    private int height;
    private static Screen thisInstance = null;
    private int cellWidth;
    private JFrame frame;
    private GameBoard board;

    /**
     * @author: Andrew Walker
     * Because singleton the constructor is private
     */
    private Screen(int width, int height) {
        this.width = width;
        this.height = height;
        frame = new JFrame("Snake");
        frame.setPreferredSize(new Dimension(width, height));
        frame.setResizable(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setFocusable(true);
        frame.requestFocus();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void initBoard(int cellWidth){
        this.cellWidth = cellWidth;
        this.board = new GameBoard(this.width, this.height, this.cellWidth);
        frame.getContentPane().add(BorderLayout.CENTER, board);
    }

    /**
     * @author: Andrew Walker
     * This function uses the singleton design pattern to return the instance of the screen
     */
    public static Screen getInstance(int width, int length)
    {
        return (thisInstance == null ? thisInstance = new Screen(width, length) : thisInstance);
    }

    /**
     * @author: Ian Laird
     * This function will simply update the game screen. It itself does not change.
     */
    public void updateScreen()
    {

    }

    /**
     * @author: Andrew Walker
     * This function will toggle the display of the screen
     */
    public void showScreen(boolean show){
        frame.setVisible(show);
    }

    public void plotWinScreen(){

    }

    public void plotDefeatScreen(){

    }

    public void plotBackground(){

    }

    public void plotPowerUp(int row, int col){

    }

    @Override
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getCellWidth() {
        return cellWidth;
    }

    public void setCellWidth(int cellWidth) {
        this.cellWidth = cellWidth;
    }

    /**
     *
     * @return direction the player wants to move
     */
    public Direct readMoveFromKeyboard(){
        /*TODO implement so that keystrokes from user are recorded
        This means that the input has to be unbuffered so as to not wait for ENTER
        Unsure how to do this we will have to research -Ian
        */
        return null;
    }
}
