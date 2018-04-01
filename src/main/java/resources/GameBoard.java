package resources;

import javax.swing.*;
import java.awt.*;

public class GameBoard extends JPanel {
    private int width;
    private int height;
    private int tiles;

    public GameBoard(int width, int height, int tiles){
        this.width = width;
        this.height = height;
        this.tiles = tiles;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        for(int x = 0; x < height / tiles; x++){
            for(int y = 0; y < width / tiles; y++){
                g.drawRect(x * tiles, y * tiles, height / tiles, width / tiles);
            }
        }
    }

    /**
     * this function should color the rectangle at a certain location in the game the desired color.
     * This indicates that the player currently has visited there
     */
    public void colorLocation(int row, int col, Enums.Color color){

    }

    /**
     * This function should restore a block to default color indicating that the player no longer covers
     * that space.
     */
    public void unColorLocation(int row, int col){
        //This might not be a necessary function
        //COuld just plot background and redraw snake instead
        //Probably Unncessary
    }
}
