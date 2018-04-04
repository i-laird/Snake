package resources;

import javax.swing.*;
import java.awt.*;

public class GameBoard extends JPanel {
    private int width;
    private int height;
    private Color[][] plot;
    private Color BACKGROUND = Color.BLACK;

    /**
     * @author: Andrew Walker
     * This method is the constructor for the GameBoard
     * @param width the width of the panel
     * @param height the height of the panel
     */
    public GameBoard(int width, int height){
        this.width = width;
        this.height = height;
        plot = new Color[this.height / Cell.getCellSize()][this.width / Cell.getCellSize()];
    }

    /**
     * @author: Andrew Walker
     * This method is the overriden function to paint the panel
     * @param g Graphics object
     */
    public void paintComponent(Graphics g) {
        for(int x = 0; x < height / Cell.getCellSize(); x++){
            for(int y = 0; y < width / Cell.getCellSize(); y++){
                g.setColor(plot[x][y]);
                g.fillRect(x * Cell.getCellSize(), y * Cell.getCellSize(), height / Cell.getCellSize(), width / Cell.getCellSize());
            }
        }
    }

    /**
     * @author: Andrew Walker
     * This method plots a new color in the master matrix
     * @param row the row of the plot to change
     * @param col the col of the plot to change
     * @param color the Color of the plot to change
     */
    public void colorLocation(int row, int col, Color color){
        this.plot[row][col] = color;
    }

    /**
     * @author: Andrew Walker
     * This method plots the default color in the master matrix
     * @param row the row of the plot to change
     * @param col the col of the plot to change
     */
    public void unColorLocation(int row, int col){
        this.plot[row][col] = BACKGROUND;
    }
}
