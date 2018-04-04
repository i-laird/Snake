package resources;

import Enums.Color;
import Enums.Direct;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static java.awt.Color.BLUE;

/**
 * This class should be a singleton b/c only one screen should exist
 */
//TODO Most of this class is unfinished...We need to implement Swing stuff
    //-Ian
public class Screen extends JFrame {
    private int width;
    private int height;
    private static Screen thisInstance = null;
    //private JFrame frame;
    private GameBoard board;

    /**
     * @author: Andrew Walker
     * Because singleton the constructor is private
     */
    private Screen(int width, int height) {
        this.width = width;
        this.height = height;
        //super = new JFrame("Snake");
        super.setPreferredSize(new Dimension(width, height));
        super.setResizable(true);
        super.pack();
        super.setLocationRelativeTo(null);
        super.setFocusable(true);
        super.requestFocus();
        super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void initBoard(){
        this.board = new GameBoard(this.width, this.height);
        super.getContentPane().add(BorderLayout.CENTER, board);
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

    public void plotBackground(){
        for(int x = 0; x < height / Cell.getCellSize(); x++){
            for(int y = 0; y < width / Cell.getCellSize(); y++){
                board.unColorLocation(x,y);
            }
        }
    }

    public void plotPowerUp(Cell c){
        board.colorLocation(c.getRow(), c.getCol(), BLUE);
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

    public void plotSnake(Snake s){
        for(Cell c : s.getSnakeLocations()){
            board.colorLocation(c.getRow(), c.getCol(), s.getColor());
        }
    }
}
